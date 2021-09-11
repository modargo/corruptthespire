package corruptthespire.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.actions.EnableThoughtfulAction;
import corruptthespire.monsters.MonsterUtil;
import corruptthespire.powers.ThoughtfulPower;

import java.util.List;

@SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnPostDrawRelics")
public class ActivateThoughtfulPowerPatch {
    @SpirePrefixPatch
    public static void activeThoughtfulPower(AbstractPlayer __instance) {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(ThoughtfulPower.POWER_ID);
        if (!powers.isEmpty()) {
            AbstractDungeon.actionManager.addToBottom(new EnableThoughtfulAction(powers));
        }
    }
}
