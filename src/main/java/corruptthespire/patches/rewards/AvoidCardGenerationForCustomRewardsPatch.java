package corruptthespire.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import corruptthespire.rewards.AbstractCorruptTheSpireReward;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = RewardItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {})
public class AvoidCardGenerationForCustomRewardsPatch {
    public static class AvoidCardGenerationForCustomRewardsPatchExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractDungeon.class.getName()) && methodCall.getMethodName().equals("getRewardCards")) {
                methodCall.replace(String.format("{ $_ = this instanceof %1$s ? null : $proceed($$); }", AbstractCorruptTheSpireReward.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor getAvoidCardGenerationForCustomRewardsPatch() {
        return new AvoidCardGenerationForCustomRewardsPatchExprEditor();
    }
}
