package corruptthespire.corruptions.fight.rewards;

import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionMediumRewardDistribution extends AbstractCorruptionDistribution<FightCorruptionRewardTypes.Medium> {
    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Medium>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<FightCorruptionRewardTypes.Medium>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.CorruptedCard, 25));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.Fragments, 20));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.CommonRelic, 10));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.CorruptedRelic, 10));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.Upgrade, 10));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.MaxHealth, 10));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.Gold, 15));
        return d;
    }
}
