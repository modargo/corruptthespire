package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.powers.ThoughtfulPower;

import java.util.List;

public class EnableThoughtfulAction extends AbstractGameAction {
    private final List<AbstractPower> powers;

    public EnableThoughtfulAction(List<AbstractPower> powers) {
        this.startDuration = this.duration = 0.0F;
        this.actionType = ActionType.SPECIAL;
        this.powers = powers;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            for (AbstractPower power : this.powers) {
                ((ThoughtfulPower)power).active = true;
            }
        }

        this.tickDuration();
    }
}
