package corruptthespire.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.shop.ShopScreen;
import corruptthespire.corruptions.shop.ShopScreenServiceInfo;

@SpirePatch(
        clz = ShopScreen.class,
        method = SpirePatch.CLASS
)
public class ShopScreenServiceInfoField {
    public static final SpireField<ShopScreenServiceInfo> serviceInfo = new SpireField<>(() -> null);
}
