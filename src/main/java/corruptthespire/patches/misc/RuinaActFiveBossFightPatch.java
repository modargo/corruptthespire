package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.actions.IncreaseInvincibleAction;

@SpirePatch(cls = "ruina.monsters.theHead.Zena", method = "transitionToPhase2", optional = true)
@SpirePatch(cls = "ruina.monsters.theHead.Baral", method = "transitionToPhase2", optional = true)
public class RuinaActFiveBossFightPatch {
    public static class ApplyStartOfCombatHooksForRuinaActFiveBossFightPatch {
        @SpirePostfixPatch
        public static void applyStartOfCombatHooksForRuinaActFiveBossFight(AbstractMonster __instance) {
            AbstractDungeon.actionManager.addToBottom(new IncreaseInvincibleAction(__instance));
        }
    }

    // Since the true final boss fight against Baral and Zena is an event room (see TheHead), fight corruptions aren't
    // applied. While that oddity could also be fixed, unsure whether it's a good idea or not. Haven't done it yet.
    // Would also require going through all the fight corruption code to make it work for event rooms. Doable, annoying.
}
