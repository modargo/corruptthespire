package corruptthespire.subscribers;

import basemod.interfaces.StartActSubscriber;
import corruptthespire.map.CorruptMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResetIsBossCorruptedSubscriber implements StartActSubscriber {
    private static final Logger logger = LogManager.getLogger(ResetIsBossCorruptedSubscriber.class.getName());
    @Override
    public void receiveStartAct() {
        logger.info("Starting new act, resetting boss corrupted marker");
        CorruptMap.setIsBossCorrupted(false);
    }
}
