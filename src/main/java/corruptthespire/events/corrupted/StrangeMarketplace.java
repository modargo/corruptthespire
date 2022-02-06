package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.shrines.Lab;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.potions.PotionUtil;

import java.text.MessageFormat;

// This extends Lab to piggyback off the special logic in ProceedButton
public class StrangeMarketplace extends Lab {
    public static final String ID = "CorruptTheSpire:StrangeMarketplace";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static final int GOLD = 40;
    public static final int A15_GOLD = 60;
    public static final int POTIONS = 3;
    public static final int A15_POTIONS = 2;

    private final AbstractRelic relic;
    private final int gold;
    private final int potions;

    private int screenNum = 0;

    public StrangeMarketplace() {
        super();
        this.imageEventText.clear();
        this.roomEventText.clear();
        this.title = NAME;
        this.body = DESCRIPTIONS[0];
        this.imageEventText.loadImage(IMG);
        this.noCardsInRewards = true;

        this.relic = Cor.returnRandomCorruptedRelic();
        this.gold = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLD : GOLD;
        this.potions = AbstractDungeon.ascensionLevel >= 15 ? A15_POTIONS : POTIONS;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic.name, this.gold), this.relic);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.potions));
    }

    @Override
    public void onEnterRoom() {}

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
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Sample
                        logMetric(ID, "Sample");

                        AbstractDungeon.getCurrRoom().rewards.clear();
                        for (int i = 0; i < this.potions; i++) {
                            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(PotionUtil.getRandomCorruptedPotion(AbstractDungeon.miscRng)));
                        }

                        GenericEventDialog.hide();
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        AbstractDungeon.combatRewardScreen.open();
                        this.screenNum = 1;
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }
}