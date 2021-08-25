package corruptthespire.corruptions.event;

import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class EventCorruptionDistribution extends AbstractCorruptionDistribution<EventCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<EventCorruptionType>> getDistribution() {
        //TODO Set real distribution when done implementing and testing
        //50 Fragment, 25 corrupted, 25 chaotic
        //Adjust depending on how many events I end up with
        ArrayList<CorruptionDistributionInfo<EventCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.Fragment, 0));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.CorruptedEvent, 0));
        d.add(new CorruptionDistributionInfo<>(EventCorruptionType.ChaoticEvent, 100));
        return d;
    }
}
