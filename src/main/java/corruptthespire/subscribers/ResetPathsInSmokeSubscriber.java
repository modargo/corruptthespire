package corruptthespire.subscribers;

import basemod.interfaces.PreStartGameSubscriber;
import corruptthespire.patches.event.PathsInTheSmokePatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResetPathsInSmokeSubscriber implements PreStartGameSubscriber {
    private static final Logger logger = LogManager.getLogger(ResetPathsInSmokeSubscriber.class.getName());
    @Override
    public void receivePreStartGame() {
        if (PathsInTheSmokePatch.isActive) {
            logger.warn("PathsInSmokePatch.isActive was true. Resetting it to false.");
            PathsInTheSmokePatch.isActive = false;
        }
    }
}
