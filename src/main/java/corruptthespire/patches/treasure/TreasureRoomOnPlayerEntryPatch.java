package corruptthespire.patches.treasure;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import corruptthespire.corruptions.treasure.TreasureCorruption;
import corruptthespire.corruptions.treasure.TreasureCorruptionDistribution;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;
import corruptthespire.events.TreasureWardensEvent;
import corruptthespire.patches.CorruptedField;

@SpirePatch(clz = TreasureRoom.class, method = "onPlayerEntry")
public class TreasureRoomOnPlayerEntryPatch {
    @SpirePrefixPatch
    public static void DetermineCorruptionType(TreasureRoom __instance) {
        if (CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
            TreasureCorruptionType corruptionType = new TreasureCorruptionDistribution().roll();
            TreasureCorruptionTypeField.corruptionType.set(__instance, corruptionType);
        }
    }

    @SpirePostfixPatch
    public static void SwitchToTreasureWardensEvent(TreasureRoom __instance) {
        if(TreasureCorruptionTypeField.corruptionType.get(__instance) == TreasureCorruptionType.Wardens) {
            TreasureCorruption.handleTreasureWardens(__instance);
        }
    }
}
