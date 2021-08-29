package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.EnablePriceOfKnowledgeAction;

public class PriceOfKnowledgePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:PriceOfKnowledge";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean active = true;
    private boolean justApplied = true;

    public PriceOfKnowledgePower(AbstractCreature owner, int amount) {
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
    public void atStartOfTurn() {
        this.active = false;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.addToBot(new EnablePriceOfKnowledgeAction(this));
    }

    @Override
    public void onCardDraw(AbstractCard c) {
        if (this.active) {
            this.addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
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
