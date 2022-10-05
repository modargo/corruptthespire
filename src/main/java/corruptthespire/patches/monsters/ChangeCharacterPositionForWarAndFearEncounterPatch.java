package corruptthespire.patches.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import corruptthespire.monsters.Encounters;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
public class ChangeCharacterPositionForWarAndFearEncounterPatch {
    public static class ChangeCharacterPositionForWarAndFearEncounterPatchExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractPlayer.class.getName()) && methodCall.getMethodName().equals("movePosition")) {
                methodCall.replace(String.format("{ if (%1$s.lastCombatMetricKey != null && %1$s.lastCombatMetricKey.equals(%2$s.WAR_AND_FEAR)) { $1 = %3$s.WIDTH / 2.0F; }; $proceed($$); }", AbstractDungeon.class.getName(), Encounters.class.getName(), Settings.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor changeCharacterPositionForWarAndFearEncounterPatch() {
        return new ChangeCharacterPositionForWarAndFearEncounterPatchExprEditor();
    }
}
