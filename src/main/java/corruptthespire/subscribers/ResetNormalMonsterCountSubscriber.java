package corruptthespire.subscribers;

import basemod.interfaces.StartActSubscriber;
import corruptthespire.Cor;
import corruptthespire.map.CorruptMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResetNormalMonsterCountSubscriber implements StartActSubscriber {
    @Override
    public void receiveStartAct() {
        Cor.flags.normalMonsterCount = 0;
    }
}
