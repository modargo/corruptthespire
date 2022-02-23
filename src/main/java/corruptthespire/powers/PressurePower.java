package corruptthespire.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import corruptthespire.CorruptTheSpire;

public class PressurePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:Pressure";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int ADDITIONAL_COST = 1;

    public PressurePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.updateDescription();
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void onPlayerCardDraw(AbstractCard c) {
        this.increaseCost(c);
    }

    public void onEndTurnAfterDiscard() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (!c.retain && !c.selfRetain && !AbstractDungeon.player.hasPower(EquilibriumPower.POWER_ID)) {
                this.increaseCost(c);
            }
        }
    }

    private void increaseCost(AbstractCard c) {
        if (c.cost >= 0) {
            c.setCostForTurn(Math.max(c.cost, c.costForTurn) + ADDITIONAL_COST);
        }
    }
}
