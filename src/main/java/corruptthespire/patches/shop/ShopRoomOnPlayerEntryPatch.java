package corruptthespire.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import corruptthespire.corruptions.shop.ShopCorruptionDistribution;
import corruptthespire.corruptions.shop.ShopCorruptionType;
import corruptthespire.patches.CorruptedField;

@SpirePatch(clz = ShopRoom.class, method = "onPlayerEntry")
@SpirePatch(cls = "downfall.rooms.HeartShopRoom", method = "onPlayerEntry", optional = true)
public class ShopRoomOnPlayerEntryPatch {
    @SpirePrefixPatch
    public static void determineCorruptionType(ShopRoom __instance) {
        if (CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
            ShopCorruptionType corruptionType = new ShopCorruptionDistribution().roll();
            ShopCorruptionTypeField.corruptionType.set(__instance, corruptionType);
        }
    }
}
