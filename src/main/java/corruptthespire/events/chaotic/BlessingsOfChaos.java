package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.relics.chaotic.ChthonicBlessing;
import corruptthespire.relics.chaotic.GnosticBlessing;

import java.text.MessageFormat;

public class BlessingsOfChaos extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:BlessingsOfChaos";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final float HP_PERCENT = 0.20F;
    private static final float A15_HP_PERCENT = 0.27F;

    private final AbstractRelic relic1;
    private final AbstractRelic relic2;
    private final int hpLoss;

    private int screenNum = 0;

    public BlessingsOfChaos() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relic1 = new GnosticBlessing();
        this.relic2 = new ChthonicBlessing();
        this.hpLoss = (int)((float)AbstractDungeon.player.maxHealth * (AbstractDungeon.ascensionLevel >= 15 ? A15_HP_PERCENT : HP_PERCENT));

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic1.name), this.relic1);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.relic2.name), this.relic2);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.hpLoss));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Power
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic1);

                        logMetricObtainRelic(ID, "Power", this.relic1);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Glory
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic2);

                        logMetricObtainRelic(ID, "Glory", this.relic1);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Decline
                        AbstractDungeon.player.damage(new DamageInfo(null, this.hpLoss));
                        logMetricTakeDamage(ID, "Decline", this.hpLoss);

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