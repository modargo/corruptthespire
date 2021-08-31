package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import corruptthespire.powers.PriceOfKnowledgePower;

public class EnablePriceOfKnowledgeAction extends AbstractGameAction {
    private final PriceOfKnowledgePower power;

    public EnablePriceOfKnowledgeAction(PriceOfKnowledgePower power) {
        this.startDuration = this.duration = 0.0F;
        this.actionType = ActionType.SPECIAL;
        this.power = power;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            power.active = true;
        }

        this.tickDuration();
    }
}
