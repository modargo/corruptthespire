package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;

@SpirePatch(
        clz = ProceedButton.class,
        method = "update"
)
public class ExtraActThreeBossFightPatch {
    @SpireInsertPatch(locator = ExtraActThreeBossFightPatch.Locator.class)
    public static SpireReturn<Void> ExtraActThreeBossFight(ProceedButton __instance) {
        //TODO: Figure out what conditions I want for this
        if (true) {
            // This is the same thing that BaseMod does; we reproduce it here because this patch
            // could be located before the BaseMod patch
            while (AbstractDungeon.bossList.size() > 2) {
                AbstractDungeon.bossList.remove(AbstractDungeon.bossList.size() - 1);
            }

            if ((AbstractDungeon.ascensionLevel >= 20 && AbstractDungeon.bossList.size() == 1)
                || (AbstractDungeon.ascensionLevel < 20 && AbstractDungeon.bossList.size() == 2)) {
                AbstractDungeon.bossKey = AbstractDungeon.bossList.get(0);
                CardCrawlGame.music.fadeOutBGM();
                CardCrawlGame.music.fadeOutTempBGM();
                MapRoomNode node = new MapRoomNode(-1, 15);
                node.room = new MonsterRoomBoss();
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.nextRoomTransitionStart();
                __instance.hide();
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "ascensionLevel");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
