package corruptthespire.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import corruptthespire.corruptions.shop.ShopCorruptionType;

@SpirePatch(
        clz = ShopRoom.class,
        method = SpirePatch.CLASS
)
public class ShopCorruptionTypeField {
    public static final SpireField<ShopCorruptionType> corruptionType = new SpireField<>(() -> null);
}
