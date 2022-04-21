package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.relics.FragmentOfCorruption;

@SpirePatch(cls = "eatyourbeets.cards.animator.series.Fate.Gilgamesh", method = "OnRelicObtained", optional = true)
public class AnimatorGilgameshOnRelicObtainPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> animatorGilgameshOnRelicObtainPatch(Object __instance, AbstractRelic relic, Object trigger) {
        if (relic.relicId.equals(FragmentOfCorruption.ID)) {
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
