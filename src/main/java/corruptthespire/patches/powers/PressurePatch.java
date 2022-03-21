package corruptthespire.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.monsters.MonsterUtil;
import corruptthespire.powers.PressurePower;

import java.util.List;

public class PressurePatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class AtPlayerTurnStartPatch {
        @SpirePrefixPatch
        public static void resetCardPlayCountPatch(AbstractPlayer __instance) {
            List<AbstractPower> powers = MonsterUtil.getMonsterPowers(PressurePower.POWER_ID);
            for (AbstractPower p : powers) {
                ((PressurePower)p).atPlayerTurnStart();
            }
        }
    }

    @SpirePatch(clz = RestoreRetainedCardsAction.class, method = "update")
    public static class ApplyEndTurnCostIncreasePatch {
        @SpirePrefixPatch
        public static void applyCostIncrease(RestoreRetainedCardsAction __instance) {
            List<AbstractPower> powers = MonsterUtil.getMonsterPowers(PressurePower.POWER_ID);
            for (AbstractPower p : powers) {
                ((PressurePower)p).onEndTurnAfterDiscard();
            }
        }
    }
}
