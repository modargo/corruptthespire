package corruptthespire.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class FlexibleCostDiscoveryAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final CardType cardType = null;
    private final Integer costReduction;

    public FlexibleCostDiscoveryAction(Integer costReduction) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.costReduction = costReduction;
    }

    public void update() {
        ArrayList<AbstractCard> generatedCards = this.generateCardChoices(this.cardType);

        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], this.cardType != null);
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    AbstractCard disCard2 = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        disCard.upgrade();
                        disCard2.upgrade();
                    }

                    if (this.costReduction == null) {
                        disCard.setCostForTurn(0);
                        disCard2.setCostForTurn(0);
                    }
                    else if (this.costReduction > 0 && disCard.cost > 0){
                        int newCost = Math.max(disCard.cost - this.costReduction, 0);
                        if (newCost != disCard.cost) {
                            disCard.cost = newCost;
                            disCard.costForTurn = newCost;
                            disCard.isCostModified = true;
                            disCard2.cost = newCost;
                            disCard2.costForTurn = newCost;
                            disCard2.isCostModified = true;
                        }
                    }
                    disCard.current_x = -1000.0F * Settings.xScale;
                    disCard2.current_x = -1000.0F * Settings.xScale + AbstractCard.IMG_HEIGHT_S;
                    if (this.amount == 1) {
                        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        } else {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        }
                    } else if (AbstractDungeon.player.hand.size() + this.amount <= BaseMod.MAX_HAND_SIZE) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard2, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    } else if (AbstractDungeon.player.hand.size() == BaseMod.MAX_HAND_SIZE - 1) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard2, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard2, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }

                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }

                this.retrieveCard = true;
            }
        }

        this.tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices(CardType type) {
        ArrayList<AbstractCard> derp = new ArrayList<>();

        while(derp.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp;
            if (type == null) {
                tmp = AbstractDungeon.returnTrulyRandomCardInCombat();
            } else {
                tmp = AbstractDungeon.returnTrulyRandomCardInCombat(type);
            }

            for (AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }

            if (!dupe) {
                derp.add(tmp.makeCopy());
            }
        }

        return derp;
    }
}
