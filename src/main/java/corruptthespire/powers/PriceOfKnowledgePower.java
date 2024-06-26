package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
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

    public void onEmptyHand() {
        if (this.active) {
            AbstractPower noDrawPower = new NoDrawPower(AbstractDungeon.player);
            // Because this is an extension of the Price of Knowledge debuff that is already on the player,
            // we want this to bypass artifact and always be applied
            noDrawPower.type = PowerType.BUFF;
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, noDrawPower));
        }
    }

    @Override
    public void atEndOfRound() {
        this.active = false;
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            } else {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            }
        }
        // Just in case no draw lingered (which it can if other mods mess with timing stuff), get rid of it
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, NoDrawPower.POWER_ID));
    }
}
