package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.monsters.PandemoniumArchfiend;
import javassist.CtBehavior;

@SpirePatch(cls = "sneckomod.cards.unknowns.AbstractUnknownCard", method = "replaceUnknown", optional = true)
public class PreventDownfallSneckoDevilsDueCrashPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = { "cUnknown" })
    public static SpireReturn<Void> preventCrash(AbstractCard __instance, AbstractCard cUnknown) {
        if (AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.id.equals(PandemoniumArchfiend.ID))) {
            int index = AbstractDungeon.player.discardPile.group.indexOf(__instance);
            if (index != -1) {
                AbstractDungeon.player.discardPile.removeCard(__instance);
                AbstractDungeon.player.discardPile.group.add(index, cUnknown);
                return SpireReturn.Return();
            }
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "isInnate");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
