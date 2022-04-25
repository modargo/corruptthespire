package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.relics.chaotic.HandOfMidas;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = MonsterRoomElite.class, method = "dropReward")
public class HandOfMidasPatch {
    public static class ReplaceRelicWithGoldExprEditor extends ExprEditor {
        private static int callCount = 0;

        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            String className = methodCall.getClassName();
            if ((className.equals(AbstractRoom.class.getName()) || className.equals(MonsterRoomElite.class.getName())) && methodCall.getMethodName().equals("addRelicToRewards")) {
                if (callCount == 0) {
                    methodCall.replace(String.format("{ if(!%1$s.handleEliteReward(this)) { $proceed($$); } }", HandOfMidas.class.getName()));
                }
                callCount++;
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor getReplaceRelicWithGoldExprEditor() {
        return new ReplaceRelicWithGoldExprEditor();
    }
}
