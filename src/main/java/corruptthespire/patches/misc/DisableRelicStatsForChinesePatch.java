package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;

// This exists because relic stats were added after the mod was translated to Chinese (ZHS), so the entries in the
// relic description array that relic stats expects aren't there
@SpirePatch(cls = "relicstats.RelicStats", method = "registerCustomStats", optional = true)
public class DisableRelicStatsForChinesePatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> disableRelicStatsForChinese(String relicId, Object hasCustomStats) {
        if (Settings.language == Settings.GameLanguage.ZHS && relicId.startsWith("CorruptTheSpire:")) {
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
