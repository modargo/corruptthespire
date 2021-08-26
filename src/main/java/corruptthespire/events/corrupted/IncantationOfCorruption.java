package corruptthespire.events.corrupted;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.CorruptedCardUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class IncantationOfCorruption extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:IncantationOfCorruption";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int MAX_HEALTH = 3;
    private static final int A15_MAX_HEALTH = 2;

    private final int maxHealth;

    private int screenNum = 0;

    public IncantationOfCorruption() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.maxHealth = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH : MAX_HEALTH;

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.maxHealth));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Use
                        ArrayList<AbstractCard> cardsTransformed = new ArrayList<>();
                        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                            if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                                cardsTransformed.add(c);
                            }
                        }
                        for (AbstractCard c : cardsTransformed) {
                            AbstractDungeon.player.masterDeck.removeCard(c);
                        }
                        ArrayList<AbstractCard> cardsObtained = CorruptedCardUtil.getRandomCorruptedCards(cardsTransformed.size());
                        for (AbstractCard c : cardsObtained) {
                            float x = MathUtils.random(0.1F, 0.9F) * (float)Settings.WIDTH;
                            float y = MathUtils.random(0.2F, 0.8F) * (float)Settings.HEIGHT;
                            AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(c, x, y));
                        }
                        logMetricTransformCardsAtCost("ID", "Use", cardsTransformed.stream().map(c -> c.cardID).collect(Collectors.toCollection(ArrayList::new)), cardsObtained.stream().map(c -> c.cardID).collect(Collectors.toCollection(ArrayList::new)), 0);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Disenchant
                        AbstractDungeon.player.increaseMaxHp(this.maxHealth, true);
                        logMetricMaxHPGain(ID, "Disenchant", this.maxHealth);

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
}