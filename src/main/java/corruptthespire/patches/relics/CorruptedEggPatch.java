package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.relics.corrupted.CorruptedEgg;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class CorruptedEggPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class GetRewardCardsPatch {
        public static class GetRewardCardsPatchExprEditor extends ExprEditor {
            @Override
            public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                if (fieldAccess.getClassName().equals(AbstractDungeon.class.getName()) && fieldAccess.getFieldName().equals("cardUpgradedChance") && fieldAccess.isReader()) {
                    fieldAccess.replace(String.format("{ $_ = %1$s.modifyUpgradeChance(%2$s.cardUpgradedChance); }", CorruptedEgg.class.getName(), AbstractDungeon.class.getName()));
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor getRewardCardsPatch() {
            return new GetRewardCardsPatchExprEditor();
        }
    }

    @SpirePatch(clz = Soul.class, method = "obtain")
    public static class SoulObtainCardPatch {
        @SpirePostfixPatch
        public static void callAfterObtainCard(Soul __instance, AbstractCard card) {
            CorruptedEgg.afterObtainCard(card);
        }
    }
}
