package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import corruptthespire.Cor;
import corruptthespire.map.CorruptMap;
import corruptthespire.relics.corrupted.MaskOfNightmares;
import javassist.CtBehavior;

public class NextRoomTransitionHandleCorruptionPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class NextRoomTransitionResetRngPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void ResetRng(AbstractDungeon __instance, SaveFile saveFile) {
            //We reset the corruption RNG (by reinstantiating it with the same seed and counter) to avoid reload instability,
            //which has shown up as a recurring issue; trying to track it down revealed that the corruption RNG counter
            //was the same, but the number produced by a RNG call was different. The only explanation is that something
            //about the state of the underlying randomization stream isn't being captured by the counter. Given how Megacrit's
            //Random implementation is just a wrapper and how there are conditions where requesting values can pull from
            //the underlying randomization stream more than once, this is theoretically possible, and no solution other
            //than this has been reliable.
            Cor.rng = new Random(Settings.seed, Cor.rng.counter);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(Random.class);
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class NextRoomTransitionAddCorruptionPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void AddCorruption(AbstractDungeon __instance, SaveFile saveFile) {
            //Due to how boss nodes are constructed on the fly (when the boss node is clicked or the save file is loaded)
            //for most acts, it's most reliable to check and set whether the room is corrupted here, right before it's used
            //Conveniently, this also handles double bosses in act 3 and any other odd case like that
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                CorruptedField.corrupted.set(AbstractDungeon.getCurrMapNode(), CorruptMap.isBossCorrupted());
            }
            boolean isLoadingPostCombatSave = CardCrawlGame.loadingSave && saveFile != null && saveFile.post_combat;
            if (!isLoadingPostCombatSave && CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
                Cor.addCorruption(AbstractDungeon.getCurrRoom());
                AbstractRelic relic = AbstractDungeon.player.getRelic(MaskOfNightmares.ID);
                if (relic != null) {
                    ((MaskOfNightmares)relic).onEnterCorruptedRoom(AbstractDungeon.getCurrRoom());
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "onPlayerEntry");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
