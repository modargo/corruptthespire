package corruptthespire.subscribers;

import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.Cor;
import corruptthespire.map.CorruptMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CorruptionHealthIncreaseOnStartBattleSubscriber implements OnStartBattleSubscriber {
    public static final Logger logger = LogManager.getLogger(CorruptMap.class.getName());
    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        logger.info("Starting battle, in subscriber CorruptionHealthIncreaseOnStartBattleSubscriber");
        for (AbstractMonster m : room.monsters.monsters) {
            m.increaseMaxHp((m.maxHealth * Cor.getCorruptionDamageMultiplierPercent()) / 100, false);
        }
    }
}
