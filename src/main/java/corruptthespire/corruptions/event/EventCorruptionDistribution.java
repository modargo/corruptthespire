package corruptthespire.corruptions.event;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;
import corruptthespire.relics.corrupted.OminousBracelet;

import java.util.ArrayList;
import java.util.List;

public class EventCorruptionDistribution extends AbstractCorruptionDistribution<EventCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<EventCorruptionType>> getDistribution() {
        //TODO Set real distribution when done implementing and testing
        //30 Fragment, 15 corrupted, 15 chaotic, 40 harbinger
        //Adjust depending on how many events I end up with, and how common I want harbinger to be
        //Might want to think of the target Harbinger encounter rate, the number of corrupted event nodes that act 2
        //will have on average, and how many of those a player is likely to go through
        ArrayList<CorruptionDistributionInfo<EventCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Fragment, 0));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Corrupted, 0));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Chaotic, 1));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Harbinger, 99));
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
