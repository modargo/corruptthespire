package corruptthespire.patches.event;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.rooms.EventRoom;
import corruptthespire.Cor;
import corruptthespire.corruptions.event.EventCorruption;
import corruptthespire.corruptions.event.EventCorruptionDistribution;
import corruptthespire.corruptions.event.EventCorruptionType;
import corruptthespire.events.CorruptedEventType;
import corruptthespire.patches.core.CorruptedField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventPatch {
    private static final Logger logger = LogManager.getLogger(EventPatch.class.getName());

    @SpirePatch(clz = EventRoom.class, method = "onPlayerEntry")
    public static class EventRoomOnPlayerEntry {
        @SpirePrefixPatch
        public static SpireReturn<Void> EventRoomOnPlayerEntryPatch(EventRoom __instance) {
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

            if (corruptionType == EventCorruptionType.Corrupted) {
                event = EventCorruption.handleCorruptedEvent(CorruptedEventType.Corrupted);
                if (event == null) {
                    event = EventCorruption.handleFragment();
                }
            }

            if (corruptionType == EventCorruptionType.Chaotic) {
                event = EventCorruption.handleCorruptedEvent(CorruptedEventType.Chaotic);
                if (event == null) {
                    event = EventCorruption.handleFragment();
                }
            }

            if (corruptionType == EventCorruptionType.Harbinger) {
                event = EventCorruption.handleHarbinger();
            }

            if (corruptionType == EventCorruptionType.Devourer) {
                event = EventCorruption.handleDevourer();
            }

            if (corruptionType == EventCorruptionType.Doom) {
                event = EventCorruption.handleDoom();
            }

            if (event != null) {
                __instance.event = event;
                event.onEnterRoom();
                return SpireReturn.Return();
            }

            if (corruptionType != null) {
                logger.warn("Failed to apply event corruption, falling back to normal event. corruptionType: " + corruptionType);
            }

            return SpireReturn.Continue();
        }
    }
}
