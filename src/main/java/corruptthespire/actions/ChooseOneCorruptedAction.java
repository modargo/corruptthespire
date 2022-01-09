package corruptthespire.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import corruptthespire.cards.corrupted.CorruptedCardUtil;

import java.util.ArrayList;

public class ChooseOneCorruptedAction extends AbstractGameAction {
    private final boolean zeroCost;
    private boolean retrieveCard = false;

    public ChooseOneCorruptedAction() {
        this(1, false);
    }

    public ChooseOneCorruptedAction(int amount, boolean zeroCost) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.zeroCost = zeroCost;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), CardRewardScreen.TEXT[1], false);
            tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                ArrayList<AbstractCard> cards = new ArrayList<>();
                for (int i = 0; i < this.amount; i++) {
                    AbstractCard c = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (AbstractDungeon.player.hasPower(MasterRealityPower.POWER_ID)) {
                        c.upgrade();
                    }
                    if (this.zeroCost) {
                        c.setCostForTurn(0);
                    }
                    c.current_x = -1000.0F * Settings.xScale;
                    cards.add(c);
                }

                int handSize = AbstractDungeon.player.hand.size();
                for (int i = 0; i < cards.size(); i++) {
                    if (handSize + i + 1 <= BaseMod.MAX_HAND_SIZE) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(cards.get(i), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                    else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(cards.get(i), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        return CorruptedCardUtil.getRandomCorruptedCardsInCombat(3);
    }
}
