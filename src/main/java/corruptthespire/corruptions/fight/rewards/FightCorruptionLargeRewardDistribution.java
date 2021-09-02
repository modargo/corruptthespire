package corruptthespire.corruptions.fight.rewards;

import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionLargeRewardDistribution extends AbstractCorruptionDistribution<FightCorruptionRewardTypes.Large> {
    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Large>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<FightCorruptionRewardTypes.Large>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.CorruptedCardAndFragment, 25));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.Relic, 25));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.CorruptedRelic, 15));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.FragmentAndRelic, 15));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.MaxHealthAndUpgrade, 15));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.Gold, 15));
        return d;
    }
}
