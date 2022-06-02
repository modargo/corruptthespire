package corruptthespire.patches.event;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(clz = AbstractDungeon.class, method = "getEvent")
@SpirePatch(clz = AbstractDungeon.class, method = "getShrine")
public class PathsInTheSmokePatch {
    private static final Logger logger = LogManager.getLogger(PathsInTheSmokePatch.class.getName());
    public static boolean isActive = false;

    @SpireInsertPatch(locator = Locator.class, localvars = {"tmpKey"})
    public static SpireReturn<AbstractEvent> pathsInTheSmokePatch(String tmpKey) {
        if (isActive && tmpKey != null) {
            logger.info("Returning dummy event for Paths in the Smoke, eventKey: " + tmpKey);
            return SpireReturn.Return(new DummyEvent(tmpKey));
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(EventHelper.class, "getEvent");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    @AutoAdd.Ignore //Just in case something else is picking up on this dummy event
    public static class DummyEvent extends AbstractEvent {
        public final String eventId;

        public DummyEvent(String eventId) {
            this.eventId = eventId;
            type = EventType.IMAGE;
            this.body = "This is a dummy event that should not be encountered.";
            this.hasDialog = true;
        }

        @Override
        protected void buttonEffect(int i) {}
    }
}
