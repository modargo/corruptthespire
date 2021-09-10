package corruptthespire.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.shop.ShopScreen;
import corruptthespire.corruptions.shop.ShopCorruption;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

@SpirePatch(clz = ShopScreen.class, method = "purchaseCard")
public class ShopCourierPatch {
    public static class ShopCourierPatchExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(ArrayList.class.getName()) && methodCall.getMethodName().equals("set")) {
                methodCall.replace(String.format("{ if (%1$s.handleCourier(this, hoveredCard, c)) { $_ = hoveredCard; } else { $_ = $proceed($$); } }", ShopCorruption.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor shopCourierPatch() {
        return new ShopCourierPatchExprEditor();
    }
}
