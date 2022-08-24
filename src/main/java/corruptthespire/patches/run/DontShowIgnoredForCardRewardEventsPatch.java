package corruptthespire.patches.run;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.beyond.SecretPortal;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.CardChoiceStats;
import com.megacrit.cardcrawl.screens.stats.EventStats;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.Arrays;
import java.util.List;

@SpirePatch(clz = RunPathElement.class, method = "getTipDescriptionText")
public class DontShowIgnoredForCardRewardEventsPatch {
    //In the long run we should add this patch to each of these mods. For now, just handle them all here.
    private static List<String> modIds = Arrays.asList("CorruptTheSpire", "Menagerie", "Elementarium", "Abyss");

    public static String handle(RunPathElement e, String player_choice) {
        if (e != null) {
            EventStats eventStats = ReflectionHacks.getPrivate(e, RunPathElement.class, "eventStats");
            if (modIds.stream().anyMatch(modId -> eventStats.event_name.startsWith(modId + ":"))) {
                CardChoiceStats cardChoiceStats = ReflectionHacks.getPrivate(e, RunPathElement.class, "cardChoiceStats");
                if (cardChoiceStats != null && !cardChoiceStats.picked.equals("SKIP")) {
                    return SecretPortal.EVENT_CHOICE_TOOK_PORTAL;
                }
            }
        }
        return player_choice;
    }

    public static class DontShowIgnoredForCardRewardEventsPatchExprEditor extends ExprEditor {
        @Override
        public void edit(FieldAccess fieldAccess) throws CannotCompileException {
            if (fieldAccess.getClassName().equals(EventStats.class.getName()) && fieldAccess.getFieldName().equals("player_choice")) {
                fieldAccess.replace(String.format("{ $_ = %1$s.handle(this, $proceed($$)); }", DontShowIgnoredForCardRewardEventsPatch.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor dontShowIgnoredForCardRewardEventsPatch() {
        return new DontShowIgnoredForCardRewardEventsPatchExprEditor();
    }
}
