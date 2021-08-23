package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import corruptthespire.Cor;

public class GainCorruptionAction extends AbstractGameAction {
    public GainCorruptionAction(int amount) {
        this.amount = amount;
        this.startDuration = this.duration = 0.0F;

        this.actionType = ActionType.SPECIAL;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            Cor.addCorruption(this.amount);
        }

        this.tickDuration();
    }
}
