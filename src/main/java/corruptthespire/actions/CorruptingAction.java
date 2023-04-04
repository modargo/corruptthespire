package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import corruptthespire.Cor;

public class CorruptingAction extends AbstractGameAction {
    public static int CorruptingCardCount = 0;

    public CorruptingAction(int amount) {
        this.amount = amount;
        this.startDuration = this.duration = 0.0F;

        this.actionType = ActionType.SPECIAL;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            CorruptingCardCount++;
            if (CorruptingCardCount > getThreshold()) {
                Cor.addCorruption(this.amount);
            }
        }

        this.tickDuration();
    }

    private static int getThreshold() {
        return Cor.corruption > 200 ? 3
                : Cor.corruption > 100 ? 2 :
                1;
    }
}
