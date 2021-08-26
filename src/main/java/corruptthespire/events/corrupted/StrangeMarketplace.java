package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class StrangeMarketplace extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:StrangeMarketplace";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static final int GOLD = 40;
    public static final int A15_GOLD = 60;

    private final AbstractRelic relic;
    private final int gold;

    private int screenNum = 0;

    public StrangeMarketplace() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relic = RelicLibrary.getRelic(Cor.returnRandomCorruptedRelicKey());
        this.gold = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLD : GOLD;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic.name, this.gold), this.relic);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Buy
                        AbstractDungeon.player.loseGold(this.gold);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic);
                        logMetricObtainRelicAtCost(ID, "Buy", this.relic, this.gold);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Leave
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
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