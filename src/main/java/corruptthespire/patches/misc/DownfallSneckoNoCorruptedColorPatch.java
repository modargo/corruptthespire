package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import corruptthespire.cards.corrupted.CorruptedCardColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

// This patch targets a method used in SneckoMod.dualClassChoice, because patching SneckoMod directly
// causes it to crash. This is due to the logic in SneckoMod.autoAddCards, which loads SneckoMod's own source code files
@SpirePatch(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { CardGroup.CardGroupType.class })
public class DownfallSneckoNoCorruptedColorPatch {
    private static final Logger logger = LogManager.getLogger(DownfallSneckoNoCorruptedColorPatch.class.getName());
    private static CardGroup colorChoices = null;

    @SpirePostfixPatch
    public static void noCorruptedColor(CardGroup cardGroup) {
        if (Loader.isModLoaded("downfall")) {
            if (colorChoices == null) {
                try {
                    Class<?> clz = Class.forName("sneckomod.SneckoMod");
                    Field f = clz.getField("colorChoices");
                    colorChoices = (CardGroup)f.get(null);
                } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if (colorChoices != null) {
                if (colorChoices.group.removeIf(c -> c.color == CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED)) {
                    logger.info("Removed Corrupted color from Snecko color choices");
                }
            }
        }
    }
}
