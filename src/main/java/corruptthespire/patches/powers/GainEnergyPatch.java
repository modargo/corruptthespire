package corruptthespire.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.monsters.MonsterUtil;
import corruptthespire.powers.RestlessPower;

import java.util.List;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "gainEnergy"
)
public class GainEnergyPatch {
    @SpirePostfixPatch
    public static void CallRestlessPower(AbstractPlayer __instance, int amount) {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(RestlessPower.POWER_ID);
        for (AbstractPower p : powers) {
            ((RestlessPower)p).onGainEnergy(amount);
        }
    }
}