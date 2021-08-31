package corruptthespire.patches.event;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractDungeon.class, method = "getEvent")
@SpirePatch(clz = AbstractDungeon.class, method = "getShrine")
public class PathsInTheSmokePatch {
    public static boolean isActive = false;

    @SpireInsertPatch(locator = Locator.class, localvars = {"tmpKey"})
    public static SpireReturn<AbstractEvent> pathsInTheSmokePatch(String tmpKey) {
        if (isActive && tmpKey != null) {
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

    public static class DummyEvent extends AbstractEvent {
        public final String eventId;

        public DummyEvent(String eventId) {
            this.eventId = eventId;
            type = EventType.IMAGE;
        }

        @Override
        protected void buttonEffect(int i) {}
    }
}
