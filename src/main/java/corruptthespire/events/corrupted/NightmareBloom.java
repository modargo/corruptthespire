package corruptthespire.events.corrupted;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class NightmareBloom extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:NightmareBloom";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int RELIC_CORRUPTION = 5;
    private static final int A15_RELIC_CORRUPTION = 10;
    private static final int UPGRADE_ALL_CORRUPTION = 50;
    private static final int GOLD_CORRUPTION = 30;
    private static final int GOLD = 666;

    private final AbstractRelic relic;
    private final int relicCorruption;

    private int screenNum = 0;

    public NightmareBloom() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relic = RelicLibrary.getRelic(Cor.returnRandomCorruptedRelicKey());
        this.relicCorruption = AbstractDungeon.ascensionLevel >= 15 ? A15_RELIC_CORRUPTION : RELIC_CORRUPTION;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic.name, this.relicCorruption), this.relic);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], UPGRADE_ALL_CORRUPTION));
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], GOLD, GOLD_CORRUPTION));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Goods
                        Cor.addCorruption(this.relicCorruption);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        logMetricObtainRelic(ID, "Goods", this.relic);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Power
                        Cor.addCorruption(UPGRADE_ALL_CORRUPTION);
                        List<String> upgradedCards = new ArrayList<>();
                        int effectCount = 0;

                        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                            if (c.canUpgrade()) {
                                effectCount++;
                                if (effectCount <= 20) {
                                    float x = MathUtils.random(0.1F, 0.9F) * (float)Settings.WIDTH;
                                    float y = MathUtils.random(0.2F, 0.8F) * (float)Settings.HEIGHT;
                                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                                }

                                upgradedCards.add(c.cardID);
                                c.upgrade();
                                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                            }
                        }
                        logMetricUpgradeCards(ID, "Power", upgradedCards);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Wealth
                        Cor.addCorruption(GOLD_CORRUPTION);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
                        AbstractDungeon.player.gainGold(GOLD);
                        logMetricGainGold(ID, "Wealth", GOLD);

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
