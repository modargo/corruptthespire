package corruptthespire.patches.fight.room;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import corruptthespire.corruptions.fight.room.FightRoomCorruption;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry")
public class ChangeEncounterCorruptionPatch {
    public static class ChangeEncounterCorruptionPatchExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractDungeon.class.getName()) && methodCall.getMethodName().equals("getMonsterForRoomCreation")) {
                methodCall.replace(String.format("{ $_ = %1$s.shouldChangeEncounter(this) ? %1$s.getChangedEncounter(this) : $proceed($$); }", FightRoomCorruption.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor changeEncounterCorruptionPatch() {
        return new ChangeEncounterCorruptionPatchExprEditor();
    }
}
