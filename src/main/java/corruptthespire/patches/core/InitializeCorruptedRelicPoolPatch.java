package corruptthespire.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
public class InitializeCorruptedRelicPoolPatch {
    @SpirePostfixPatch
    public static void InitializeCorruptedRelicPool(AbstractDungeon __instance) {
        if (AbstractDungeon.floorNum < 1) {
            Cor.fillCorruptedRelicPool(false);
        }
    }
}
