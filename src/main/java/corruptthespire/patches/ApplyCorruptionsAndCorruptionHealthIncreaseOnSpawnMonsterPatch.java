package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_M;
import corruptthespire.Cor;
import corruptthespire.corruptions.fight.FightCorruption;

public class ApplyCorruptionsAndCorruptionHealthIncreaseOnSpawnMonsterPatch {
    //We patch the addMonster method (and its variants) as the best way of catching spawned monsters
    //This is better than patching SpawnMonsterAction because there may be spawning of monsters through other actions
    //(SummonGremlinAction in the base game, and who knows what else in other mods?)
    //If anyone writes code like AbstractDungeon.getCurrRoom().monsters.monsters.add (instead of calling addMonster),
    //this patch will miss the spawned monsters... but that's less of a problem than other ways of doing this.
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "addMonster",
            paramtypez = {int.class, AbstractMonster.class}
    )
    public static class AddMonsterPatch {
        @SpirePostfixPatch
        public static void handle(MonsterGroup __instance, int index, AbstractMonster m) {
            applyCorruptionsOnSpawnMonster(m);
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "addMonster",
            paramtypez = {AbstractMonster.class}
    )
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "addSpawnedMonster",
            paramtypez = {AbstractMonster.class}
    )
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "add",
            paramtypez = {AbstractMonster.class}
    )
    public static class AddMonsterVariantsPatch {
        @SpirePostfixPatch
        public static void handle(MonsterGroup __instance, AbstractMonster m) {
            applyCorruptionsOnSpawnMonster(m);
        }
    }

    private static void applyCorruptionsOnSpawnMonster(AbstractMonster m) {
        //For Darklings, we don't want to re-apply the health increase when they respawn
        //We also exclude the slimes that come from splitting, since the parent slime already got a health increase
        if (!m.halfDead && !isSlime(m)) {
            Cor.applyCorruptionHealthIncrease(m);
        }
        if (FightCorruption.shouldApplyCorruptions()) {
            FightCorruption.applyOnSpawnMonsterCorruptions(m);
        }
    }

    private static boolean isSlime(AbstractMonster m) {
        return m.id.equals(SpikeSlime_L.ID) || m.id.equals(AcidSlime_L.ID) || m.id.equals(SpikeSlime_M.ID) || m.id.equals(AcidSlime_M.ID);
    }
}
