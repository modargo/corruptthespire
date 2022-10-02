package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.monsters.Harbinger;

import java.text.MessageFormat;

public class AstralCorePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:AstralCore";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int STRENGTH_DEGEN = 1;

    public AstralCorePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], Harbinger.ASTRAL_CORE_DAMAGE_THRESHOLD, this.amount, STRENGTH_DEGEN);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractPower strengthPower = this.owner.getPower(StrengthPower.POWER_ID);
        if (strengthPower != null && strengthPower.amount > 0) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -STRENGTH_DEGEN)));
        }
    }

    public void trigger() {
        this.flash();
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, PowerUtil.gainStrengthBuff(this.owner, this.amount)));
        }
        else {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
        }
    }
}
