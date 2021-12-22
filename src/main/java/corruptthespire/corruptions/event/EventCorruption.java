package corruptthespire.corruptions.event;

import com.megacrit.cardcrawl.daily.mods.Diverse;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import corruptthespire.Cor;
import corruptthespire.cards.CustomTags;
import corruptthespire.events.*;
import corruptthespire.events.chaotic.Divergence;
import corruptthespire.events.chaotic.MindsEye;
import corruptthespire.events.chaotic.TreeOfSwords;
import corruptthespire.events.chaotic.WorldsUponWorlds;
import corruptthespire.events.corrupted.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class EventCorruption {
    private static final Logger logger = LogManager.getLogger(EventCorruption.class.getName());

    public static AbstractEvent handleFragment() {
        return new FragmentOfCorruptionEvent();
    }

    public static AbstractEvent handleHarbinger() {
        return new HarbingerEvent();
    }

    public static AbstractEvent handleCorruptedEvent(CorruptedEventType corruptedEventType) {
        ArrayList<String> possibleEvents = filterEvents(Cor.corruptedEventList, corruptedEventType);

        if (possibleEvents.isEmpty()) {
            return null;
        }

        String eventId = possibleEvents.remove(Cor.rng.random(possibleEvents.size() - 1));
        Cor.corruptedEventList.remove(eventId);
        Class<? extends AbstractEvent> eventClass = CorruptedEventUtil.getAllCorruptedEvents().get(eventId).cls;
        try {
            return eventClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.info("Failed to instantiate event: " + eventClass.getName());
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<String> filterEvents(ArrayList<String> corruptedEventList, CorruptedEventType corruptedEventType) {
        Map<String, CorruptedEventInfo> allEvents = CorruptedEventUtil.getAllCorruptedEvents();
        return corruptedEventList.stream()
            .filter(EventCorruption::keepEvent)
            .filter(e -> allEvents.get(e).corruptedEventType == corruptedEventType)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean keepEvent(String e) {
        int actNum = Cor.getActNum();
        if (e.equals(MindsEye.ID)) {
            return !ModHelper.isModEnabled(Diverse.ID) && !AbstractDungeon.player.hasRelic(PrismaticShard.ID);
        }

        if (e.equals(TreeOfSwords.ID)) {
            return AbstractDungeon.player.masterDeck.getPurgeableCards().size() >= 2 && AbstractDungeon.player.relics.stream().anyMatch(r -> r.tier == AbstractRelic.RelicTier.STARTER) && AbstractDungeon.player.currentHealth > (AbstractDungeon.ascensionLevel >= 15 ? TreeOfSwords.A15_DAMAGE : TreeOfSwords.DAMAGE);
        }

        if (e.equals(NightmareBloom.ID)) {
            return Cor.getRealActNum() == 3 && AbstractDungeon.eventList.contains(MindBloom.ID);
        }

        if (e.equals(SinisterTemple.ID)) {
            return AbstractDungeon.player.relics.stream().filter(r -> r.tier == AbstractRelic.RelicTier.COMMON).count() >= 2;
        }

        if (e.equals(StrangeMarketplace.ID)) {
            return AbstractDungeon.player.gold >= (AbstractDungeon.ascensionLevel >= 15 ? StrangeMarketplace.A15_GOLD : StrangeMarketplace.GOLD);
        }

        if (e.equals(TheChoice.ID)) {
            return Cor.corruption >= TheChoice.CORRUPTION_REDUCTION && AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c.tags != null && c.hasTag(CustomTags.CORRUPTED));
        }

        if (e.equals(WorldsUponWorlds.ID)) {
            return Cor.corruption >= (AbstractDungeon.ascensionLevel >= 15 ? WorldsUponWorlds.A15_CORRUPTION_REDUCTION : WorldsUponWorlds.CORRUPTION_REDUCTION);
        }

        if (e.equals(IncantationOfCorruption.ID)) {
            return actNum > 1;
        }

        if (e.equals(CorruptedShrine.ID)) {
            return actNum > 1;
        }

        if (e.equals(TheDevice.ID)) {
            return AbstractDungeon.floorNum > 6 && actNum != 4;
        }

        if (e.equals(Divergence.ID)) {
            return AbstractDungeon.floorNum > 6;
        }

        if (e.equals(ForbiddenLibrary.ID)) {
            return AbstractDungeon.floorNum > 6;
        }

        return true;
    }
}
