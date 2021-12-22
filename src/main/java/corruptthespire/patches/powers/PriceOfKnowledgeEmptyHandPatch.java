package corruptthespire.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.powers.PriceOfKnowledgePower;
import javassist.CtBehavior;

@SpirePatch(clz = CardGroup.class, method = "refreshHandLayout")
public class PriceOfKnowledgeEmptyHandPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void callEmptyHandTrigger(CardGroup __instance) {
        if (AbstractDungeon.actionManager.actions.isEmpty() && AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPower p = AbstractDungeon.player.getPower(PriceOfKnowledgePower.POWER_ID);
            if (p != null) {
                ((PriceOfKnowledgePower)p).onEmptyHand();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
