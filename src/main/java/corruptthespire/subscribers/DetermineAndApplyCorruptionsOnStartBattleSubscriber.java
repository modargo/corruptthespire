package corruptthespire.subscribers;

import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.corruptions.fight.FightCorruption;

public class DetermineAndApplyCorruptionsOnStartBattleSubscriber implements OnStartBattleSubscriber {
    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if (FightCorruption.shouldApplyCorruptions()) {
            FightCorruption.determineCorruptions(room);
            FightCorruption.applyStartOfBattleCorruptions();
            for (AbstractMonster m : room.monsters.monsters) {
                FightCorruption.applyOnSpawnMonsterCorruptions(m);
            }
        }
    }
}
