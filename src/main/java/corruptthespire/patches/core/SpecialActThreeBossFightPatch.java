package corruptthespire.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import corruptthespire.Cor;
import corruptthespire.monsters.Encounters;
import corruptthespire.monsters.PandemoniumArchfiend;
import corruptthespire.util.Downfall;
import javassist.CtBehavior;

@SpirePatch(
        clz = ProceedButton.class,
        method = "update"
)
@SpirePatch(
        cls = "actlikeit.patches.ContinueOntoHeartPatch",
        method = "Insert",
        optional = true
)
public class SpecialActThreeBossFightPatch {
    @SpireInsertPatch(locator = SpecialActThreeBossFightPatch.Locator.class)
    public static SpireReturn<Void> SpecialActThreeBossFight(ProceedButton __instance) {
        if (shouldFightSpecialBoss()) {
            // This is the same thing that BaseMod does; we reproduce it here because this patch
            // could be located before the BaseMod patch
            while (AbstractDungeon.bossList.size() > 2) {
                AbstractDungeon.bossList.remove(AbstractDungeon.bossList.size() - 1);
            }

            if ((AbstractDungeon.ascensionLevel >= 20 && AbstractDungeon.bossList.size() == 1)
                || (AbstractDungeon.ascensionLevel < 20 && AbstractDungeon.bossList.size() == 2)
                || Downfall.isDownfallMode()) {
                AbstractDungeon.bossKey = getSpecialBoss();
                if (AbstractDungeon.bossKey.equals(PandemoniumArchfiend.ID)) {
                    Cor.flags.foughtArchfiend = true;
                }
                if (AbstractDungeon.bossKey.equals(Encounters.TIME_AND_SPACE)) {
                    Cor.flags.foughtMaster = true;
                }
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
        }
        return SpireReturn.Continue();
    }

    private static boolean shouldFightSpecialBoss() {
        return getExtraBossLeftCount() > 0;
    }

    public static int getExtraBossLeftCount() {
        int extraBossLeftCount = (Cor.flags.openedSealedChest ? 1 : 0) + (Cor.flags.brokeDevice ? 1 : 0);
        extraBossLeftCount -= (Cor.flags.foughtArchfiend ? 1 : 0);
        extraBossLeftCount -= (Cor.flags.foughtMaster ? 1 : 0);
        return extraBossLeftCount;
    }

    private static String getSpecialBoss() {
        return Cor.flags.foughtArchfiend ? Encounters.TIME_AND_SPACE
            : Cor.flags.foughtMaster || Cor.rng.randomBoolean() ? PandemoniumArchfiend.ID
            : Encounters.TIME_AND_SPACE;
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "ascensionLevel");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
