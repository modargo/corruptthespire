package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.patches.relics.BottledPrismPatch;
import corruptthespire.rewards.CorruptedCardReward;

import java.text.MessageFormat;
import java.util.Collections;

public class CorruptedShrine extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:CorruptedShrine";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int CARDS = 3;
    private static final int A15_CARDS = 2;

    private final int cards;

    private int screenNum = 0;

    public CorruptedShrine() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        this.cards = AbstractDungeon.ascensionLevel >= 15 ? A15_CARDS : CARDS;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.cards));
        imageEventText.setDialogOption(OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    public void onEnterRoom() {
        CardCrawlGame.music.playTempBGM("SHRINE");
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy();
            c.inBottleFlame = false;
            c.inBottleLightning = false;
            c.inBottleTornado = false;
            BottledPrismPatch.InBottledPrismField.inBottlePrism.set(c, false);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            logMetricObtainCards(ID, "Pray", Collections.singletonList(c.cardID));
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Beseech
                        logMetric(ID, "Beseech");
                        this.showCardReward(this.cards);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Pray
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, OPTIONS[3], false, false, false, false);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Leave
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
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

    private void showCardReward(int numRewards) {
        AbstractDungeon.getCurrRoom().rewards.clear();
        for(int i = 0; i < numRewards; ++i) {
            AbstractDungeon.getCurrRoom().rewards.add(new CorruptedCardReward());
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }
}