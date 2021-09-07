package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class MindsEye extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:MindsEye";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int MAX_HEALTH = 6;
    private static final int A15_MAX_HEALTH = 4;

    private final AbstractRelic relic;
    private final int maxHealth;

    private int screenNum = 0;

    public MindsEye() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relic = new PrismaticShard();
        this.maxHealth = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH : MAX_HEALTH;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic.name), this.relic);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], maxHealth));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Epiphany
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.relic);
                        AbstractDungeon.shopRelicPool.remove(this.relic.relicId);
                        logMetricObtainRelic(ID, "Epiphany", this.relic);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Reject
                        AbstractDungeon.player.increaseMaxHp(this.maxHealth, true);
                        logMetricMaxHPGain(ID, "Reject", this.maxHealth);

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