package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_M;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.corruptions.fight.FightCorruption;

@SpirePatch(
        clz = AbstractRelic.class,
        method = "onSpawnMonster"
)
public class ApplyCorruptionsAndCorruptionHealthIncreaseOnSpawnMonsterPatch {
    // This patch is a bit of a hack -- there are custom spawn actions out there, so we can't catch
    // all monster spawning by looking at SpawnMonsterAction or ReviveMonsterAction
    // Instead, we rely on the fact that all such actions need to call AbstractRelic.onSpawnMonster
    // That method is called for each relic, and we need to translate that into one call per monster
    // Since that sequence of calls happens once and is always for one monster at a time, we achieve
    // this by tracking the current monster
    // Another way to do this would be in MonsterGroup.addMonster, with some set of checks
    private static AbstractMonster currentMonster = null;

    @SpirePrefixPatch
    public static void ApplyCorruptionsOnSpawnMonster(AbstractRelic __instance, AbstractMonster m) {
        if (currentMonster != m) {
            //For Darklings, we don't want to re-apply the health increase when they respawn
            //We also exclude the slimes that come from splitting, since the parent slime already got a health increaase
            if (!m.halfDead && !isSlime(m)) {
                Cor.applyCorruptionHealthIncrease(m);
            }
            if (FightCorruption.shouldApplyCorruptions()) {
                FightCorruption.applyOnSpawnMonsterCorruptions(m);
            }
            currentMonster = m;
        }
    }

    private static boolean isSlime(AbstractMonster m) {
        return m.id.equals(SpikeSlime_L.ID) || m.id.equals(AcidSlime_L.ID) || m.id.equals(SpikeSlime_M.ID) || m.id.equals(AcidSlime_M.ID);
    }
}
