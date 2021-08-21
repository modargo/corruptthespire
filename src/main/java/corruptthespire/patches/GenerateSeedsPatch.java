package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import corruptthespire.Cor;

@SpirePatch(
    clz = AbstractDungeon.class,
    method = "generateSeeds"
)
public class GenerateSeedsPatch {
    @SpirePostfixPatch
    public static void InitializeCorruptionRng() {
        Cor.rng = new Random(Settings.seed);
        // This could go somewhere else, but this is as good a place as any to put it
        Cor.corruption = 0;
    }
}