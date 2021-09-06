package corruptthespire.subscribers;

import basemod.interfaces.StartActSubscriber;
import corruptthespire.Cor;

public class ResetNormalMonsterCountSubscriber implements StartActSubscriber {
    @Override
    public void receiveStartAct() {
        Cor.flags.normalMonsterCount = 0;
    }
}
