package corruptthespire.patches.fight;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import corruptthespire.corruptions.fight.FightCorruptionInfo;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz = MonsterRoom.class,
        method = SpirePatch.CLASS
)
public class FightCorruptionInfosField {
    public static final SpireField<List<FightCorruptionInfo>> corruptionInfos = new SpireField<>(ArrayList::new);
}
