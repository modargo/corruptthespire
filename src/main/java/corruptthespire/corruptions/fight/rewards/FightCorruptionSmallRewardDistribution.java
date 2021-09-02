package corruptthespire.corruptions.fight.rewards;

import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionSmallRewardDistribution extends AbstractCorruptionDistribution<FightCorruptionRewardTypes.Small> {
    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Small>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<FightCorruptionRewardTypes.Small>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.CorruptedCard, 20));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.Fragment, 20));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.Potion, 20));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.Upgrade, 20));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.Gold, 20));
        return d;
    }
}
