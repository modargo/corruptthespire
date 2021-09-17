package corruptthespire.patches.treasure;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.corruptions.treasure.TreasureCorruption;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = AbstractChest.class, method = "open")
public class AbstractChestOpenPatch {
    @SpirePrefixPatch
    public static void handleExtraRewards(AbstractChest __instance, boolean bossChest) {
        TreasureCorruption.handleExtraRewards();
    }

    public static class AbstractChestOpenPatchExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractRoom.class.getName()) && methodCall.getMethodName().equals("addRelicToRewards")) {
                methodCall.replace(String.format("{ if (!%1$s.handleRelic($1)) { $proceed($$); } }", TreasureCorruption.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor abstractChestOpenPatch() {
        return new AbstractChestOpenPatchExprEditor();
    }
}
