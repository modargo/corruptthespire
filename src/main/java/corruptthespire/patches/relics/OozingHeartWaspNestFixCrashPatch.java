package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch(cls = "com.megacrit.cardcrawl.mod.replay.relics.WaspNest", method = "onLoseHp", optional = true)
public class OozingHeartWaspNestFixCrashPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> fixCrash(Object __instance, int damageAmount) {
        if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.getMonsters() == null) {
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
