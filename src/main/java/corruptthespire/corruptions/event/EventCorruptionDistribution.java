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
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Fragment, 10));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Corrupted, 20));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Chaotic, 20));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Harbinger, 25));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Devourer, 25));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<EventCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<EventCorruptionType>> distribution) {
        if (AbstractDungeon.player.hasRelic(OminousBracelet.ID)) {
            distribution.removeIf(di -> di.corruption == EventCorruptionType.Fragment);
        }
        if (Cor.flags.seenHarbinger || Cor.getRealActNum() != 2) {
            distribution.removeIf(di -> di.corruption == EventCorruptionType.Harbinger);
            distribution.removeIf(di -> di.corruption == EventCorruptionType.Devourer);
        }
        return distribution;
    }
}
