package corruptthespire.patches.misc;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import com.megacrit.cardcrawl.screens.mainMenu.TabBarListener;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlphabetizeColorTabBarPatch {
    private static final Logger logger = LogManager.getLogger(AlphabetizeColorTabBarPatch.class.getName());

    public static boolean colorTabsSorted = false;
    private static Map<AbstractCard.CardColor, ColorInfo> colorInfos = new HashMap<>();

    @SpirePatch(clz = ColorTabBarFix.Render.class, method = "Insert")
    public static class RecordTabNames {
        public static class RecordTabNamesExprEditor extends ExprEditor {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(FontHelper.class.getName()) && methodCall.getMethodName().equals("renderFontCentered")) {
                    methodCall.replace(String.format("{ if(!%1$s.colorTabsSorted) { %1$s.recordTabNames(((%2$s.ModColorTab)%2$s.Fields.modTabs.get(i)).color, tabName, playerClass); }; $proceed($$); }", AlphabetizeColorTabBarPatch.class.getName(), ColorTabBarFix.class.getName()));
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor getRecordTabNamesExprEditor() {
            return new RecordTabNamesExprEditor();
        }
    }

    public static void recordTabNames(AbstractCard.CardColor color, String colorName, AbstractPlayer.PlayerClass playerClass) {
        if (color != null && colorName != null) {
            logger.info("Recording tab name: " + color.name() + ", " + colorName + ", " + (playerClass == null ? "null" : playerClass.name()));
            colorInfos.put(color, new ColorInfo(colorName, playerClass != null));
        }
    }

    private static class ColorInfo {
        public final String colorName;
        public final boolean isCharacterColor;

        private ColorInfo(String colorName, boolean isCharacterColor) {
            this.colorName = colorName;
            this.isCharacterColor = isCharacterColor;
        }
    }

    @SpirePatch(clz = ColorTabBarFix.Render.class, method = "Insert")
    public static class SortAndRenderAgain {
        @SpirePostfixPatch
        public static void sortAndRenderAgain(ColorTabBar __instance, SpriteBatch sb, float y, ColorTabBar.CurrentTab curTab) {
            if (colorTabsSorted) {
                return;
            }
            List<ColorTabBarFix.ModColorTab> modTabs = ReflectionHacks.getPrivateStatic(ColorTabBarFix.Fields.class, "modTabs");
            List<AbstractCard.CardColor> missingColors = modTabs.stream().map(t -> t.color).filter(c -> !colorInfos.containsKey(c)).collect(Collectors.toList());
            if (missingColors.size() > 0) {
                logger.info("Can't sort mod color tabs because some colors are missing from colorInfos. Colors missing: " + missingColors.stream().map(Enum::name).collect(Collectors.joining(", ")));
                return;
            }
            modTabs.sort((t1, t2) -> {
                ColorInfo c1 = colorInfos.get(t1.color);
                ColorInfo c2 = colorInfos.get(t2.color);
                if (c1.isCharacterColor != c2.isCharacterColor) {
                    return Boolean.compare(c1.isCharacterColor, c2.isCharacterColor);
                }
                return c1.colorName.compareTo(c2.colorName);
            });
            colorTabsSorted = true;

            // We call the mod tab rendering method again so that the new order is rendered over the old order
            logger.info("Re-rendering mod color tabs after sorting");
            ColorTabBarFix.Render.Insert(__instance, sb, y, curTab);
        }
    }

    @SpirePatch(clz = ColorTabBarFix.Ctor.class, method = "Postfix")
    public static class ResetFields {
        @SpirePostfixPatch
        public static void resetFields(ColorTabBar __instance, TabBarListener delegate) {
            colorTabsSorted = false;
            colorInfos = new HashMap<>();
        }
    }
}
