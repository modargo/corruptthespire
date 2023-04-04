package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import corruptthespire.Cor;

public class CorruptingAction extends AbstractGameAction {
    private static final int MAX_CARDS = 5;
    private static final int CORRUPTION_PER_CARD = 50;

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
        return Math.min((Cor.corruption / CORRUPTION_PER_CARD) + 1, MAX_CARDS);
    }
}
