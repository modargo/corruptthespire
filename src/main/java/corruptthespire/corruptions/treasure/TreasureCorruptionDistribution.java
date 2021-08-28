package corruptthespire.corruptions.treasure;

import corruptthespire.Cor;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class TreasureCorruptionDistribution extends AbstractCorruptionDistribution<TreasureCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<TreasureCorruptionType>> getDistribution() {
        //TODO Set real distribution when done implementing and testing
        //Fragment 40, Money 10, Extra 10, Vault 10, CorruptedRelic 10, Wardens 10, Sealed 10
        ArrayList<CorruptionDistributionInfo<TreasureCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Fragment, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Money, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Extra, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Vault, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.CorruptedRelic, 0));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Wardens, 100));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Sealed, 0));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<TreasureCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<TreasureCorruptionType>> distribution) {
        if (Cor.flags.seenTreasureWardens) {
            distribution.removeIf(di -> di.corruption == TreasureCorruptionType.Wardens);
        }
        return distribution;
    }
}
