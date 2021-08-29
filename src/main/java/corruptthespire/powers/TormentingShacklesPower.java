package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;

public class TormentingShacklesPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:TormentingShackles";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int ADDITIONAL_COST = 1;

    private boolean justApplied = true;

    public TormentingShacklesPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.cost >= 0) {
            card.setCostForTurn(Math.max(card.cost, card.costForTurn) + ADDITIONAL_COST);
        }
    }

    @Override
    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            } else {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            }
        }
    }
}
