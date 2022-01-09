package corruptthespire.patches.potions;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.random.Random;
import corruptthespire.potions.PotionUtil;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilterOutCorruptedPotionsPatch {
    @SpirePatch(clz = PotionHelper.class, method = "getRandomPotion", paramtypez = {})
    @SpirePatch(clz = PotionHelper.class, method = "getRandomPotion", paramtypez = {Random.class})
    public static class FilterOutCorruptedPotionsExprEditor extends ExprEditor {
        @Override
        public void edit(FieldAccess fieldAccess) throws CannotCompileException {
            if (fieldAccess.getClassName().equals(PotionHelper.class.getName()) && fieldAccess.getFieldName().equals("potions")) {
                fieldAccess.replace(String.format("{ $_ = %1$s.filterOutCorruptedPotions(potions); }", FilterOutCorruptedPotionsPatch.class.getName()));
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor filterOutCorruptedRarityPotions() {
            return new FilterOutCorruptedPotionsExprEditor();
        }
    }

    public static ArrayList<String> filterOutCorruptedPotions(ArrayList<String> potions) {
        return new ArrayList<>(potions).stream()
            .filter(potionId -> !PotionUtil.corruptedPotionIds.contains(potionId))
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
