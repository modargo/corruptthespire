package corruptthespire.events;

import com.megacrit.cardcrawl.events.AbstractEvent;

public class CorruptedEventInfo {
    public final Class<? extends AbstractEvent> cls;
    public final CorruptedEventType corruptedEventType;

    public CorruptedEventInfo(Class<? extends AbstractEvent> cls, CorruptedEventType corruptedEventType) {
        this.cls = cls;
        this.corruptedEventType = corruptedEventType;
    }
}
