package corruptthespire.subscribers;

import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.actions.CorruptingAction;

public class ResetCorruptingCardCountOnStartBattleSubscriber implements OnStartBattleSubscriber {
    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        CorruptingAction.CorruptingCardCount = 0;
    }
}
