package corruptthespire.patches.core;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import corruptthespire.Cor;
import corruptthespire.CorruptionFlags;
import corruptthespire.monsters.Encounters;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;

public class WarAndFearPatch {
    @SpirePatch2(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = { String.class, AbstractPlayer.class })
    @SpirePatch2(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = { String.class, AbstractPlayer.class, SaveFile.class })
    @SpirePatch2(cls = "actlikeit.patches.GetDungeonPatches$getDungeonThroughProgression", method = "Postfix", paramtypez = { AbstractDungeon.class, CardCrawlGame.class, String.class, AbstractPlayer.class }, optional = true)
    @SpirePatch2(cls = "actlikeit.patches.GetDungeonPatches$getDungeonThroughSavefile", method = "Postfix", paramtypez = { AbstractDungeon.class, CardCrawlGame.class, String.class, AbstractPlayer.class, SaveFile.class }, optional = true)
    public static class ReplaceActTwoBossFightPatch {
        @SpirePostfixPatch
        public static AbstractDungeon ReplaceActTwoBossFight(AbstractDungeon __result) {
            if (Cor.flags.warAndFear == CorruptionFlags.WarAndFear.REPLACE_BOSS && __result != null) {
                ReflectionHacks.privateMethod(AbstractDungeon.class, "setBoss", String.class).invoke(__result, Encounters.WAR_AND_FEAR);
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = ProceedButton.class,
            method = "update"
    )
    public static class SpecialActTwoBossFightPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> SpecialActTwoBossFight(ProceedButton __instance) throws InvocationTargetException, IllegalAccessException {
            if (justFoughtSpecialBoss()) {
                // It would also be natural for this to go in NextRoomTransitionRecordCorruptionFlagsPatch,
                // but the logic in this method executes first, and in the extra boss case we need to catch
                // that we already fought War and Fear before we override the next room to be War and Fear
                Cor.flags.warAndFear = CorruptionFlags.WarAndFear.FOUGHT;
                // We also need to go to the treasure room ourselves, since this call is normally in a check
                // for being on the combat reward screen. We could also patch that logic in ProceedButton but
                // this seemed easier
                ReflectionHacks.getCachedMethod(ProceedButton.class, "goToTreasureRoom").invoke(__instance);
            }
            else if (shouldFightSpecialExtraBoss()) {
                AbstractDungeon.bossKey = getSpecialBoss();
                CardCrawlGame.music.fadeOutBGM();
                CardCrawlGame.music.fadeOutTempBGM();
                MapRoomNode node = new MapRoomNode(-1, 15);
                node.room = new MonsterRoomBoss();
                AbstractDungeon.bossList.add(0, AbstractDungeon.bossKey);
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.nextRoomTransitionStart();
                __instance.hide();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static boolean justFoughtSpecialBoss() {
            return AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && AbstractDungeon.lastCombatMetricKey.equals(Encounters.WAR_AND_FEAR);
        }

        private static boolean shouldFightSpecialExtraBoss() {
            return AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && Cor.flags.warAndFear == CorruptionFlags.WarAndFear.EXTRA_BOSS && Cor.getRealActNum() == 2;
        }

        private static String getSpecialBoss() {
            return Encounters.WAR_AND_FEAR;
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.InstanceOfMatcher(MonsterRoomBoss.class);
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}