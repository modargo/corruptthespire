package corruptthespire.patches.treasure;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;
import corruptthespire.corruptions.treasure.VaultChest;
import javassist.CtBehavior;

import java.util.List;

@SpirePatch(clz = ProceedButton.class, method = "update")
public class ProceedButtonWhileInVaultPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn ReturnToTreasureRoom(ProceedButton __instance) {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room instanceof TreasureRoom
            && TreasureCorruptionTypeField.corruptionType.get(room) == TreasureCorruptionType.Vault
            && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            List<VaultChest> vaultChests = VaultChestsField.vaultChests.get(AbstractDungeon.getCurrRoom());
            int numUnopenedChests = (int)vaultChests.stream().filter(c -> !c.isOpen).count();

            if (numUnopenedChests == 0) {
                return SpireReturn.Continue();
            }

            Hitbox hb = ReflectionHacks.getPrivate(__instance, ProceedButton.class, "hb");
            hb.clicked = false;

            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.overlayMenu.proceedButton.setLabel(AbstractChest.TEXT[0]);
            AbstractDungeon.overlayMenu.proceedButton.show();

            return SpireReturn.Return(null);
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
