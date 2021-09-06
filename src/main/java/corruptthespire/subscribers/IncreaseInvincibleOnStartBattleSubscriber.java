package corruptthespire.subscribers;

import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.actions.IncreaseInvincibleAction;

public class IncreaseInvincibleOnStartBattleSubscriber implements OnStartBattleSubscriber {
    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if (AbstractDungeon.actNum == 4 && room instanceof MonsterRoomBoss) {
            AbstractDungeon.actionManager.addToBottom(new IncreaseInvincibleAction());
        }
    }
}
