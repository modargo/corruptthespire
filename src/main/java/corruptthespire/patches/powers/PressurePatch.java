package corruptthespire.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.monsters.MonsterUtil;
import corruptthespire.powers.PressurePower;

import java.util.List;

public class PressurePatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class ResetCardPlayCountPatch {
        @SpirePrefixPatch
        public static void resetCardPlayCountPatch(AbstractPlayer __instance) {
            List<AbstractPower> powers = MonsterUtil.getMonsterPowers(PressurePower.POWER_ID);
            for (AbstractPower p : powers) {
                ((PressurePower)p).atPlayerTurnStart();
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class CanPlayPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> checkCanPlay(AbstractCard __instance) {
            List<AbstractPower> powers = MonsterUtil.getMonsterPowers(PressurePower.POWER_ID);
            for (AbstractPower p : powers) {
                if(!((PressurePower)p).canPlayCard()) {
                    return SpireReturn.Return(false);
                }
            }

            return SpireReturn.Continue();
        }
    }
}
