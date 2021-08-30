package corruptthespire.subscribers;

import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.Cor;

public class ApplyCorruptionHealthIncreaseOnStartBattleSubscriber implements OnStartBattleSubscriber {
    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        for (AbstractMonster m : room.monsters.monsters) {
            Cor.applyCorruptionHealthIncrease(m);
        }
    }
}