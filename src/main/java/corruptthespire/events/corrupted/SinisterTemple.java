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
import corruptthespire.relics.corrupted.AbstractCorruptedRelic;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class SinisterTemple extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:SinisterTemple";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final float DISCOUNT = 0.9F;
    private static final float A15_DISCOUNT = 1.0F;
    private static final float RANGE = 0.1F;

    private final AbstractRelic relic1;
    private final AbstractRelic relic2;
    private final int gold;
    private final AbstractRelic rewardRelic;

    private int screenNum = 0;

    public SinisterTemple() {
        super(NAME, DESCRIPTIONS[0], IMG);

        ArrayList<AbstractRelic> commonRelics = AbstractDungeon.player.relics.stream().filter(r -> r.tier == AbstractRelic.RelicTier.COMMON).collect(Collectors.toCollection(ArrayList::new));
        if (commonRelics.size() < 2) {
            throw new RuntimeException("Sinister Temple event requires at least two common relics");
        }
        Collections.shuffle(commonRelics, AbstractDungeon.miscRng.random);
        this.relic1 = commonRelics.get(0);
        this.relic2 = commonRelics.get(1);
        float discount = AbstractDungeon.ascensionLevel >= 15 ? A15_DISCOUNT : DISCOUNT;
        this.gold = AbstractDungeon.miscRng.random((int)((discount - RANGE) * AbstractCorruptedRelic.CORRUPTED_RELIC_PRICE), (int)(discount * AbstractCorruptedRelic.CORRUPTED_RELIC_PRICE));
        this.rewardRelic = Cor.returnRandomCorruptedRelic();

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.rewardRelic.name, this.relic1.name), this.rewardRelic);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.rewardRelic.name, this.relic2.name), this.rewardRelic);
        if (AbstractDungeon.player.gold >= this.gold) {
            imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.rewardRelic.name, this.gold), this.rewardRelic);
        }
        else {
            imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.gold), true);
        }
        imageEventText.setDialogOption(OPTIONS[3]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Relic
                        AbstractDungeon.player.loseRelic(this.relic1.relicId);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.rewardRelic);
                        logMetricRelicSwap(ID, "Relic", this.rewardRelic, this.relic1);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Relic
                        AbstractDungeon.player.loseRelic(this.relic2.relicId);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.rewardRelic);
                        logMetricRelicSwap(ID, "Relic", this.rewardRelic, this.relic2);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Gold
                        AbstractDungeon.player.loseGold(this.gold);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.rewardRelic);
                        logMetric(ID, "Gold", null, null, null, null, Collections.singletonList(this.rewardRelic.relicId), null, null, 0, 0, 0, 0, 0, this.gold);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 3: // Gold
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
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