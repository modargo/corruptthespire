package corruptthespire.events.corrupted;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.CorruptedCardUtil;

import java.text.MessageFormat;

public class ForbiddenLibrary extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:ForbiddenLibrary";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int CARDS = 6;
    private static final int A15_CARDS = 5;
    private static final float HEAL_PERCENT = 0.45F;
    private static final float A15_HEAL_PERCENT = 0.33F;

    private final int cards;
    private final int heal;

    private int screenNum = 0;

    public ForbiddenLibrary() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.cards = AbstractDungeon.ascensionLevel >= 15 ? A15_CARDS : CARDS;
        this.heal = (int)((float)AbstractDungeon.player.maxHealth * (AbstractDungeon.ascensionLevel >= 15 ? A15_HEAL_PERCENT : HEAL_PERCENT));

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.cards));
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.heal));
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            logMetricObtainCard(ID, "Study", c);
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Study
                        this.getCardsAndOpenSelectScreen(this.cards);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[MathUtils.random(1, 3)]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Rest
                        AbstractDungeon.player.heal(this.heal);
                        logMetricHeal(ID, "Rest", this.heal);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
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

    private void getCardsAndOpenSelectScreen(int cards) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.group = CorruptedCardUtil.getRandomCorruptedCards(cards);
        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[3], false);
    }
}