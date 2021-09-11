package corruptthespire.subscribers;

import basemod.interfaces.PostDrawSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.monsters.MonsterUtil;
import corruptthespire.powers.ThoughtStealerPower;
import corruptthespire.powers.ThoughtfulPower;

import java.util.List;

public class TriggerThoughtStealerPostDrawSubscriber implements PostDrawSubscriber {
    @Override
    public void receivePostDraw(AbstractCard card) {
        List<AbstractPower> powers1 = MonsterUtil.getMonsterPowers(ThoughtStealerPower.POWER_ID);
        for (AbstractPower p : powers1) {
            ((ThoughtStealerPower)p).onPlayerCardDraw();
        }
        List<AbstractPower> powers2 = MonsterUtil.getMonsterPowers(ThoughtfulPower.POWER_ID);
        for (AbstractPower p : powers2) {
            ((ThoughtfulPower)p).onPlayerCardDraw();
        }
    }
}
