package corruptthespire.patches.fight;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.corruptions.fight.FightCorruption;

import java.util.ArrayList;

@SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry")
@SpirePatch(clz = MonsterRoomElite.class, method = "onPlayerEntry")
@SpirePatch(clz = MonsterRoomBoss.class, method = "onPlayerEntry")
public class ApplySpawnEnemyCorruptionsPatch {
    @SpirePostfixPatch
    public static void applySpawnEnemyCorruptions(AbstractRoom __instance) {
        if (FightCorruption.shouldApplyCorruptions()) {
            ArrayList<AbstractMonster> monsters = FightCorruption.getExtraMonsterCorruptions();
            for (AbstractMonster m : monsters) {
                __instance.monsters.addMonster(0, m);
                m.init();
            }
        }
    }
}
