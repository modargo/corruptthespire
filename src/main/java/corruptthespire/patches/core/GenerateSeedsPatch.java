package corruptthespire.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Config;
import corruptthespire.Cor;
import corruptthespire.CorruptionFlags;
import corruptthespire.events.CorruptedEventUtil;

import java.util.ArrayList;

@SpirePatch(
    clz = AbstractDungeon.class,
    method = "generateSeeds"
)
public class GenerateSeedsPatch {
    @SpirePostfixPatch
    public static void InitializeCorruptionRng() {
        Cor.active = Config.active();
        Cor.resetRng(Settings.seed, null);
        // These could go somewhere else, but this is as good a place as any to put it
        Cor.corruption = 0;
        Cor.flags = new CorruptionFlags();
        Cor.corruptedEventList = new ArrayList<>(CorruptedEventUtil.getEnabledCorruptedEvents().keySet());
        Cor.corruptedEventList.sort(String::compareTo);
        Cor.corruptionPerFloor = new ArrayList<>();
    }
}