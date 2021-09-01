package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import corruptthespire.Cor;
import corruptthespire.relics.corrupted.BlackCard;

import java.util.ArrayList;

public class BlackCardPatch {
    @SpirePatch(clz = ShopScreen.class, method = "init")
    public static class AdjustPrice {
        @SpirePostfixPatch
        public static void adjustPrice(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            __instance.applyDiscount((100 - 30) / 100.0F, true);
        }
    }

    @SpirePatch(clz = ShopRoom.class, method = "onPlayerEntry")
    public static class AddCorruption {
        @SpirePrefixPatch
        public static void addCorruption(ShopRoom __instance) {
            Cor.addCorruption(BlackCard.CORRUPTION);
        }

    }
}
