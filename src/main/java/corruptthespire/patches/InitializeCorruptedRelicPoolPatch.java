package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import corruptthespire.Cor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
public class InitializeCorruptedRelicPoolPatch {
    @SpirePostfixPatch
    public static void InitializeCorruptedRelicPool(AbstractDungeon __instance) {
        Cor.fillCorruptedRelicPool(false);
    }
}
