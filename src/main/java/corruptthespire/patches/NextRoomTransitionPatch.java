package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import corruptthespire.Cor;
import corruptthespire.map.CorruptMap;
import corruptthespire.relics.corrupted.MaskOfNightmares;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
public class NextRoomTransitionPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void AddCorruption(AbstractDungeon __instance, SaveFile saveFile) {
        //Due to how boss nodes are constructed on the fly (when the boss node is clicked or the save file is loaded)
        //for most acts, it's most reliable to check and set whether the room is corrupted here, right before it's used
        //Conveniently, this also handles double bosses in act 3 and any other odd case like that
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            CorruptedField.corrupted.set(AbstractDungeon.getCurrMapNode(), CorruptMap.isBossCorrupted());
        }
        if (CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
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
