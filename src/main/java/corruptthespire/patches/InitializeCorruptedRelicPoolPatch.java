package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
public class InitializeCorruptedRelicPoolPatch {
    @SpirePostfixPatch
    public static void InitializeCorruptedRelicPool(AbstractDungeon __instance) {
        Cor.fillCorruptedRelicPool(false);
    }
}
