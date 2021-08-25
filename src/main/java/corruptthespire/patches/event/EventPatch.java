package corruptthespire.patches.event;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.rooms.EventRoom;
import corruptthespire.corruptions.event.EventCorruption;
import corruptthespire.corruptions.event.EventCorruptionDistribution;
import corruptthespire.corruptions.event.EventCorruptionType;
import corruptthespire.patches.CorruptedField;

public class EventPatch {
    @SpirePatch(clz = EventRoom.class, method = "onPlayerEntry")
    public static class EventRoomOnPlayerEntry {
        @SpirePrefixPatch
        public static SpireReturn EventRoomOnPlayerEntryPatch(EventRoom __instance) {
            EventCorruptionType corruptionType = null;
            if (CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
                corruptionType = new EventCorruptionDistribution().roll();
                EventCorruptionTypeField.corruptionType.set(__instance, corruptionType);

                AbstractDungeon.overlayMenu.proceedButton.hide();
            }

            AbstractEvent event = null;
            if (corruptionType == EventCorruptionType.Fragment) {
                event = EventCorruption.handleFragment();
            }

            if (corruptionType == EventCorruptionType.CorruptedEvent) {
                event = EventCorruption.handleCorruptedEvent();
            }

            if (event != null) {
                __instance.event = event;
                event.onEnterRoom();
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}
