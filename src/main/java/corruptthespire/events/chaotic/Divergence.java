package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.CardUtil;

import java.text.MessageFormat;

public class Divergence extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:Divergence";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int CARDS_CLASS = 25;
    private static final int A15_CARDS_CLASS = 20;
    private static final int CARDS_ANY = 30;
    private static final int A15_CARDS_ANY = 25;

    private final int cardsClass;
    private final int cardsAny;
    private String choice;

    private int screenNum = 0;

    public Divergence() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.cardsClass = AbstractDungeon.ascensionLevel >= 15 ? A15_CARDS_CLASS : CARDS_CLASS;
        this.cardsAny = AbstractDungeon.ascensionLevel >= 15 ? A15_CARDS_ANY : CARDS_ANY;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.cardsClass));
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.cardsAny));
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            logMetricObtainCard(ID, this.choice, c);
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Focus
                        this.choice = "Focus";
                        this.getCardsAndOpenSelectScreen(this.cardsClass, false);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Reject
                        this.choice = "Broaden";
                        this.getCardsAndOpenSelectScreen(this.cardsAny, true);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    private void getCardsAndOpenSelectScreen(int cards, boolean anyColor) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(int i = 0; i < cards; ++i) {
            AbstractCard card = (anyColor ? CardUtil.getOtherColorCard(AbstractDungeon.rollRarity()) : AbstractDungeon.getCard(AbstractDungeon.rollRarity())).makeCopy();
            boolean containsDupe = true;

            while(containsDupe) {
                containsDupe = false;

                for (AbstractCard c : group.group) {
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
                        break;
                    }
                }
            }

            if (group.contains(card)) {
                i--;
            } else {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(card);
                }
                group.addToBottom(card);
            }
        }

        for (AbstractCard c : group.group) {
            UnlockTracker.markCardAsSeen(c.cardID);
        }

        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[3], false);
    }
}