package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class AncientLaboratory extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:AncientLaboratory";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static final int DAMAGE = 3;
    public static final int A15_DAMAGE = 5;

    private final int damage;
    private final AbstractRelic corruptedRelic;

    private int screenNum = 0;

    public AncientLaboratory() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.damage = AbstractDungeon.ascensionLevel >= 15 ? A15_DAMAGE : DAMAGE;
        this.corruptedRelic = Cor.returnRandomCorruptedRelic();

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.corruptedRelic.name, this.damage), this.corruptedRelic);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.damage));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Corruption
                        AbstractDungeon.player.damage(new DamageInfo(null, this.damage));
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.corruptedRelic);
                        logMetricObtainRelicAndDamage(ID, "Corruption", this.corruptedRelic, this.damage);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Rummage
                        AbstractDungeon.player.damage(new DamageInfo(null, this.damage));
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                        logMetricObtainRelicAndDamage(ID, "Rummage", relic, this.damage);

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