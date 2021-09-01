package corruptthespire.patches.fight;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;

@SpirePatch(clz = AbstractDungeon.class, method = "getMonsterForRoomCreation")
public class CountNormalMonstersPatch {
    @SpirePrefixPatch
    public static void countNormalMonsters() {
        Cor.flags.normalMonsterCount++;
    }
}
