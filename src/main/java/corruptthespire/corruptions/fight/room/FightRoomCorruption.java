package corruptthespire.corruptions.fight.room;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.monsters.Encounters;
import corruptthespire.monsters.HundredSouled;
import corruptthespire.monsters.RottingShambler;
import corruptthespire.patches.fight.room.FightRoomCorruptionTypeField;
import corruptthespire.relics.FragmentOfCorruption;

public class FightRoomCorruption {
    public static boolean shouldChangeEncounter(AbstractRoom room) {
        FightRoomCorruptionType roomCorruptionType = FightRoomCorruptionTypeField.roomCorruptionType.get(room);
        String changedEncounterId = getChangedEncounterId(roomCorruptionType);
        return changedEncounterId != null;
    }

    public static MonsterGroup getChangedEncounter(AbstractRoom room) {
        FightRoomCorruptionType roomCorruptionType = FightRoomCorruptionTypeField.roomCorruptionType.get(room);
        String changedEncounterId = getChangedEncounterId(roomCorruptionType);
        if (changedEncounterId != null) {
            AbstractDungeon.lastCombatMetricKey = changedEncounterId;
            return MonsterHelper.getEncounter(changedEncounterId);
        }
        return null;
    }

    private static String getChangedEncounterId(FightRoomCorruptionType roomCorruptionType) {
        if (roomCorruptionType == null) {
            return null;
        }
        switch (roomCorruptionType) {
            case RottingShambler:
                return RottingShambler.ID;
            case Wisps:
                return Encounters.WISPS;
            case HundredSouled:
                return HundredSouled.ID;
            default:
                return null;
        }
    }

    public static void addRewards(FightRoomCorruptionType roomCorruptionType) {
        if (roomCorruptionType == null) {
            return;
        }
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        switch (roomCorruptionType) {
            case RottingShambler:
            case Wisps:
            case HundredSouled:
                room.addRelicToRewards(new FragmentOfCorruption());
                room.addRelicToRewards(new FragmentOfCorruption());
                break;
        }
    }
}
