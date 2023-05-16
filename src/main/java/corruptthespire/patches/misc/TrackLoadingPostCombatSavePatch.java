package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
public class TrackLoadingPostCombatSavePatch {
    public static boolean isLoadingPostCombatSave = false;

    @SpirePrefixPatch
    public static void resetPre(AbstractDungeon __instance) {
        TrackLoadingPostCombatSavePatch.isLoadingPostCombatSave = false;
    }

    @SpireInsertPatch(locator = Locator.class, localvars = { "isLoadingPostCombatSave" })
    public static void record(AbstractDungeon __instance, boolean isLoadingPostCombatSave) {
        TrackLoadingPostCombatSavePatch.isLoadingPostCombatSave = isLoadingPostCombatSave;
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "onPlayerEntry");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }

    @SpirePostfixPatch
    public static void resetPost(AbstractDungeon __instance) {
        TrackLoadingPostCombatSavePatch.isLoadingPostCombatSave = false;
    }
}

