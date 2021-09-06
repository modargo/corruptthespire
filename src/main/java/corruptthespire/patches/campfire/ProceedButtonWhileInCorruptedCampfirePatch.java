package corruptthespire.patches.campfire;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import corruptthespire.corruptions.campfire.CampfireInfo;
import corruptthespire.patches.CorruptedField;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = ProceedButton.class, method = "update")
public class ProceedButtonWhileInCorruptedCampfirePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<Void> ReturnToTreasureRoom(ProceedButton __instance) {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room instanceof RestRoom
            && CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
            && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            CampfireUI campfireUI = ((RestRoom) room).campfireUI;
            CampfireInfo campfireInfo = CampfireInfoField.campfireInfo.get(campfireUI);

            if (campfireInfo.isDone) {
                return SpireReturn.Continue();
            }

            campfireInfo.isDone = true;
            Hitbox hb = ReflectionHacks.getPrivate(__instance, ProceedButton.class, "hb");
            hb.clicked = false;

            ArrayList<AbstractCampfireOption> buttons = ReflectionHacks.getPrivate(campfireUI, CampfireUI.class, "buttons");
            buttons.clear();
            ReflectionHacks.privateMethod(CampfireUI.class, "initializeButtons").invoke(campfireUI);
            if (buttons.size() > 2) {
                ReflectionHacks.setPrivate(campfireUI, CampfireUI.class, "bubbleAmt", 60);
            } else {
                ReflectionHacks.setPrivate(campfireUI, CampfireUI.class, "bubbleAmt", 40);
            }

            String campMessage = ReflectionHacks.privateMethod(CampfireUI.class, "getCampMessage").invoke(campfireUI);
            ReflectionHacks.setPrivate(campfireUI, CampfireUI.class, "bubbleMsg", campMessage);

            AbstractDungeon.overlayMenu.proceedButton.hide();
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.previousScreen = null;
            campfireUI.reopen();

            return SpireReturn.Return();
        }

        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(Hitbox.class, "clicked");
            int[] lines = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            // We need to insert on the line after the call, so add 1
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i] + 1;
            }
            return lines;
        }
    }
}
