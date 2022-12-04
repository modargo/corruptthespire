package corruptthespire.patches.monsters;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.Cor;
import corruptthespire.CorruptionFlags;
import corruptthespire.monsters.Encounters;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractRoom.class, method = "update", paramtypez = {})
public class NoRewardsForWarAndFearExtraBossFightPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<Void> noRewardsForWarAndFearExtraBossFightPatch(AbstractRoom __instance) {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && AbstractDungeon.lastCombatMetricKey.equals(Encounters.WAR_AND_FEAR) && Cor.flags.warAndFear == CorruptionFlags.WarAndFear.EXTRA_BOSS) {
            __instance.monsters.updateAnimations();
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.InstanceOfMatcher(TheEnding.class);
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
