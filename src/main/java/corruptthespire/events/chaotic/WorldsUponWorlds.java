package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.relics.elite.ShimmeringFan;
import corruptthespire.rewards.CorruptedCardReward;

import java.text.MessageFormat;

public class WorldsUponWorlds extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:WorldsUponWorlds";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static final int CORRUPTION_REDUCTION = 5;
    public static final int A15_CORRUPTION_REDUCTION = 3;
    private static final int NUM_REWARDS = 1;

    private final AbstractRelic relic;
    private final int corruptionReduction;

    private int screenNum = 0;

    public WorldsUponWorlds() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        this.relic = new ShimmeringFan();
        this.corruptionReduction = AbstractDungeon.ascensionLevel >= 15 ? A15_CORRUPTION_REDUCTION : CORRUPTION_REDUCTION;

        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
            logMetricCardRemoval(ID, "Second", c);
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.relic.name, this.corruptionReduction), this.relic);
                this.imageEventText.setDialogOption(OPTIONS[2]);
                this.imageEventText.setDialogOption(OPTIONS[3]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // First
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic);
                        Cor.addCorruption(-this.corruptionReduction);
                        logMetricObtainRelic(ID, "First", this.relic);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Second
                        logMetricIgnored(ID);
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                            AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[4], false, false, false, true);
                        }

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Third
                        this.showCardReward();
                        logMetric(ID, "Third");

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    private void showCardReward() {
        AbstractDungeon.getCurrRoom().rewards.clear();
        for(int i = 0; i < NUM_REWARDS; ++i) {
            AbstractDungeon.getCurrRoom().rewards.add(new CorruptedCardReward());
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }
}
