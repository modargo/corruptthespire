package corruptthespire.patches.treasure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class RewardItemSapphireKeyRelicLinkPatch {
    public static String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:TreasureCorruption").TEXT;

    @SpirePatch(clz = RewardItem.class, method = "render")
    public static class SapphireKeyTextPatch {
        public static class SapphireKeyTextPatchPatchExprEditor extends ExprEditor {
            @Override
            public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                if (fieldAccess.getClassName().equals(AbstractRelic.class.getName()) && fieldAccess.getFieldName().equals("name")) {
                    fieldAccess.replace(String.format("{ $_ = $0 != null ? $0.name : %1$s.TEXT[1]; }", RewardItemSapphireKeyRelicLinkPatch.class.getName()));
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor SapphireKeyPatch() {
            return new SapphireKeyTextPatch.SapphireKeyTextPatchPatchExprEditor();
        }
    }

    @SpirePatch(clz = RewardItem.class, method = "render")
    public static class GoldTextPatch {
        @SpirePostfixPatch
        public static void addRelicLinkTip(RewardItem __instance, SpriteBatch sb) {
            if (__instance.type == RewardItem.RewardType.GOLD
                && __instance.hb.hovered
                && __instance.relicLink != null
                && __instance.relicLink.type == RewardItem.RewardType.SAPPHIRE_KEY) {
                TipHelper.renderGenericTip(360.0F * Settings.scale, (float)InputHelper.mY + 50.0F * Settings.scale, RewardItem.TEXT[7], RewardItem.TEXT[8] + FontHelper.colorString(RewardItem.TEXT[6] + RewardItem.TEXT[9], "y"));
            }
        }
    }

    @SpirePatch(clz = RewardItem.class, method = "claimReward")
    public static class ClaimRewardPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> ignoreSapphireKeyWhenGoldClaimed(RewardItem __instance) {
            if(__instance.type == RewardItem.RewardType.GOLD && __instance.relicLink != null) {
                __instance.relicLink.isDone = true;
                __instance.relicLink.ignoreReward = true;
                if (__instance.ignoreReward) {
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
