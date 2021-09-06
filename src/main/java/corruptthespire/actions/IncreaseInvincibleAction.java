package corruptthespire.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import corruptthespire.Cor;

public class IncreaseInvincibleAction extends AbstractGameAction {
    public IncreaseInvincibleAction() {
        this.startDuration = this.duration = 0.0F;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                AbstractPower power = m.getPower(InvinciblePower.POWER_ID);
                if (power != null) {
                    int maxAmt = ReflectionHacks.getPrivate(power, InvinciblePower.class, "maxAmt");
                    int newMaxAmt = (maxAmt * Cor.getCorruptionDamageMultiplierPercent()) / 100;
                    ReflectionHacks.setPrivate(power, InvinciblePower.class, "maxAmt", newMaxAmt);
                    power.amount = newMaxAmt;
                    power.updateDescription();
                }
            }
        }

        this.tickDuration();
    }
}
