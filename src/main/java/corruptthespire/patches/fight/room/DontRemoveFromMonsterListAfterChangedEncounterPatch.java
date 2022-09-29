package corruptthespire.patches.fight.room;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import corruptthespire.corruptions.fight.room.FightRoomCorruption;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
public class DontRemoveFromMonsterListAfterChangedEncounterPatch {
    public static class DontRemoveFromMonsterListAfterChangedEncounterPatchExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractDungeon.class.getName()) && methodCall.getMethodName().equals("getMonsterForRoomCreation")) {
                methodCall.replace(String.format("{ $_ = $0 == $1%s.monsterList && $args.length == 1 && $args[0] == 0 && %2$s.shouldChangeEncounter(%1$s.getCurrRoom()) ? ($r)null : $proceed($$); }", AbstractDungeon.class.getName(), FightRoomCorruption.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor dontRemoveFromMonsterListAfterChangedEncounterPatch() {
        return new DontRemoveFromMonsterListAfterChangedEncounterPatchExprEditor();
    }
}
