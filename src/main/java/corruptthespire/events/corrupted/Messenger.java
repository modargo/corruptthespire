package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.CelestialAegis;

import java.text.MessageFormat;
import java.util.Collections;

public class Messenger extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:Messenger";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final float MAX_HP_PERCENT = 0.20F;
    private static final float A15_MAX_HP_PERCENT = 0.25F;

    private final AbstractCard card;
    private final int maxHpLoss;

    private int screenNum = 0;

    public Messenger() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.card = new CelestialAegis();
        this.maxHpLoss = (int)((float)AbstractDungeon.player.maxHealth * (AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HP_PERCENT : MAX_HP_PERCENT));

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.card.name, this.maxHpLoss), this.card);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Accept
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHpLoss);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));

                        logMetricObtainCardsLoseMapHP(ID, "Accept", Collections.singletonList(card.cardID), this.maxHpLoss);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Rest
                        logMetricIgnored(ID);

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