package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.CorruptionFlags;

import java.text.MessageFormat;

public class Ascent extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:Ascent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static final int HP_AND_MAX_HP_LOSS = 4;
    public static final int A15_HP_AND_MAX_HP_LOSS = 6;

    private int screenNum = 0;

    private final int hpAndMaxHpLoss;

    public Ascent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.hpAndMaxHpLoss = AbstractDungeon.ascensionLevel >= 15 ? A15_HP_AND_MAX_HP_LOSS : HP_AND_MAX_HP_LOSS;

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.hpAndMaxHpLoss));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Ascend
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        Cor.flags.warAndFear = CorruptionFlags.WarAndFear.EXTRA_BOSS;
                        logMetricObtainRelic(ID, "Ascend", relic);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Bargain
                        Cor.flags.warAndFear = CorruptionFlags.WarAndFear.REPLACE_BOSS;
                        logMetric(ID, "Bargain");

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Bypass
                        AbstractDungeon.player.damage(new DamageInfo(null, this.hpAndMaxHpLoss));
                        AbstractDungeon.player.decreaseMaxHealth(this.hpAndMaxHpLoss);
                        logMetric(ID, "Bypass", null, null, null, null, null, null, null, this.hpAndMaxHpLoss, 0, this.hpAndMaxHpLoss, 0, 0, 0);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
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
