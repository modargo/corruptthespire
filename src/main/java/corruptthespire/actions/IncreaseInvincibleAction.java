package corruptthespire.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import corruptthespire.Cor;

public class IncreaseInvincibleAction extends AbstractGameAction {
    private final AbstractMonster monster;

    public IncreaseInvincibleAction() {
        this(null);
    }

    public IncreaseInvincibleAction(AbstractMonster monster) {
        this.monster = monster;
        this.startDuration = this.duration = 0.0F;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.monster == null) {
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    increaseInvincible(m);
                }
            }
            else {
                increaseInvincible(this.monster);
            }
        }

        this.tickDuration();
    }

    private void increaseInvincible(AbstractMonster m) {
        AbstractPower power = m.getPower(InvinciblePower.POWER_ID);
        if (power != null) {
            int maxAmt = ReflectionHacks.getPrivate(power, InvinciblePower.class, "maxAmt");
            int newMaxAmt = maxAmt + (maxAmt * Cor.getCorruptionDamageMultiplierPercent()) / 100;
            ReflectionHacks.setPrivate(power, InvinciblePower.class, "maxAmt", newMaxAmt);
            power.amount = newMaxAmt;
            power.updateDescription();
        }
    }

}
