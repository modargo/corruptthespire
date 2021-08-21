package corruptthespire.corruptions.treasure;

import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class TreasureCorruptionDistribution extends AbstractCorruptionDistribution<TreasureCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<TreasureCorruptionType>> getDistribution() {
        //TODO Set real distribution when done implementing and testing
        //Fragment 50, Money 10, Extra 10, Vault 10, CorruptedRelic 10, something else 10?
        //Something else could be the fight idea
        ArrayList<CorruptionDistributionInfo<TreasureCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Fragment, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Money, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Extra, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Vault, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.CorruptedRelic, 100));
        return d;
    }
}
