package corruptthespire.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import corruptthespire.rewards.AbstractCorruptTheSpireReward;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(clz = RewardItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {})
public class AvoidCardGenerationForCustomRewardsPatch {
    private static final Logger logger = LogManager.getLogger(AvoidCardGenerationForCustomRewardsPatch.class.getName());
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

    @SpirePostfixPatch
    public static void forDebugging(RewardItem __instance) {
        logger.info("[RewardItem] After RewardItem(). AbstractDungeon.cardRng.counter: " + AbstractDungeon.cardRng.counter);
        logger.info("[RewardItem] RewardItem.getClass().getName(): " + __instance.getClass().getName());
    }
}
