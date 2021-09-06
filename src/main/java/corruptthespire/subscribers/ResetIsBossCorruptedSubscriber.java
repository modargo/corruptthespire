package corruptthespire.subscribers;

import basemod.interfaces.StartActSubscriber;
import corruptthespire.map.CorruptMap;

public class ResetIsBossCorruptedSubscriber implements StartActSubscriber {
    @Override
    public void receiveStartAct() {
        CorruptMap.setIsBossCorrupted(false);
    }
}
