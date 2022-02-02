package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.rewards.CorruptedCardReward;

import java.text.MessageFormat;

public class TheDevice extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:TheDevice";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static final int GOLD = 350;
    public static final int A15_GOLD = 300;
    public static final int CORRUPTED_CARDS = 3;
    public static final int A15_CORRUPTED_CARDS = 2;

    private final int gold;
    private final int corruptedCards;

    private int screenNum = 0;

    public TheDevice() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        this.gold = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLD : GOLD;
        this.corruptedCards = AbstractDungeon.ascensionLevel >= 15 ? A15_CORRUPTED_CARDS : CORRUPTED_CARDS;

        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.gold, this.corruptedCards));
                this.imageEventText.setDialogOption(OPTIONS[2]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Take
                        Cor.flags.brokeDevice = true;
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
                        AbstractDungeon.player.gainGold(GOLD);
                        logMetricGainGold(ID, "Take", this.gold);

                        this.showCardReward(this.corruptedCards);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Leave
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screenNum = 2;
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

    private void showCardReward(int numRewards) {
        AbstractDungeon.getCurrRoom().rewards.clear();
        for(int i = 0; i < numRewards; ++i) {
            AbstractDungeon.getCurrRoom().rewards.add(new CorruptedCardReward());
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }
}
