package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.relics.chaotic.ShimmeringFan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

public class WorldsUponWorlds extends AbstractImageEvent {
    public static final Logger logger = LogManager.getLogger(WorldsUponWorlds.class.getName());
    public static final String ID = "CorruptTheSpire:WorldsUponWorlds";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private final AbstractRelic relic;

    private int screenNum = 0;

    public WorldsUponWorlds() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        this.relic = new ShimmeringFan();

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
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.relic.name), this.relic);
                this.imageEventText.setDialogOption(OPTIONS[2]);
                this.imageEventText.setDialogOption(OPTIONS[3]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // First
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic);
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
                        this.showCardReward(1);
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

    private void showCardReward(int numRewards) {
        AbstractDungeon.getCurrRoom().rewards.clear();
        for(int i = 0; i < numRewards; ++i) {
            RewardItem reward = CorruptedCardUtil.getCorruptedCardReward();
            AbstractDungeon.getCurrRoom().addCardReward(reward);
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }
}
