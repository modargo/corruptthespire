package corruptthespire.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.powers.DiabolicForcePower;

@SpirePatch(cls = "channeler.powers.ForcePower", method = "trigger", optional = true)
public class DiabolicForcePatch {
    @SpirePostfixPatch
    public static void triggerDiabolicForce(AbstractPower __instance) {
        DiabolicForcePower p = (DiabolicForcePower)AbstractDungeon.player.getPower(DiabolicForcePower.POWER_ID);
        if (p != null) {
            p.onForceTrigger(__instance.amount);
        }
    }
}
