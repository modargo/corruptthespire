package corruptthespire.subscribers;

import basemod.interfaces.StartActSubscriber;
import corruptthespire.Cor;
import corruptthespire.map.CorruptMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResetNormalMonsterCountSubscriber implements StartActSubscriber {
    private static final Logger logger = LogManager.getLogger(ResetNormalMonsterCountSubscriber.class.getName());
    @Override
    public void receiveStartAct() {
        logger.info("Starting new act, resetting normal monster count");
        Cor.flags.normalMonsterCount = 0;
    }
}
