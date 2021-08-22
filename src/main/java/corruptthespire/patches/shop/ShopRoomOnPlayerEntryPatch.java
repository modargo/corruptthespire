package corruptthespire.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import corruptthespire.corruptions.shop.ShopCorruptionDistribution;
import corruptthespire.corruptions.shop.ShopCorruptionType;
import corruptthespire.corruptions.treasure.TreasureCorruptionDistribution;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;
import corruptthespire.patches.CorruptedField;
import corruptthespire.patches.treasure.TreasureCorruptionTypeField;

@SpirePatch(clz = ShopRoom.class, method = "onPlayerEntry")
public class ShopRoomOnPlayerEntryPatch {
    @SpirePrefixPatch
    public static void determineCorruptionType(ShopRoom __instance) {
        if (CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
            ShopCorruptionType corruptionType = new ShopCorruptionDistribution().roll();
            ShopCorruptionTypeField.corruptionType.set(__instance, corruptionType);
        }
    }
}
