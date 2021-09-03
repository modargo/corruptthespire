package corruptthespire.corruptions.event;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;
import corruptthespire.relics.corrupted.OminousBracelet;

import java.util.ArrayList;
import java.util.List;

public class EventCorruptionDistribution extends AbstractCorruptionDistribution<EventCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<EventCorruptionType>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<EventCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Fragment, 15));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Corrupted, 15));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Chaotic, 15));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Harbinger, 55));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<EventCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<EventCorruptionType>> distribution) {
        if (AbstractDungeon.player.hasRelic(OminousBracelet.ID)) {
            distribution.removeIf(di -> di.corruption == EventCorruptionType.Fragment);
        }
        if (Cor.flags.seenHarbinger || AbstractDungeon.actNum != 2) {
            distribution.removeIf(di -> di.corruption == EventCorruptionType.Harbinger);
        }
        return distribution;
    }
}
