package corruptthespire.events.special;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class TreasureWardensEventRoom extends EventRoom {
    @Override
    public void endBattle() {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onChestOpen(false);
        }

        super.endBattle();

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (!r.relicId.equals("hubris:Towel")) {
                r.onChestOpenAfter(false);
            }
        }
    }
}
