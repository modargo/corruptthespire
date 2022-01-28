package corruptthespire.corruptions.fight.rewards;

import corruptthespire.Cor;
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
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.Upgrade, 20));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.MaxHealth, 20));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Small.Gold, 20));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Small>> adjustDistribution(List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Small>> distribution) {
        if (!Cor.flags.hadFirstCorruptedNormalMonsterFight) {
            distribution.removeIf(d -> d.corruption != FightCorruptionRewardTypes.Small.CorruptedCard);
        }
        if (Cor.getActNum() == 4) {
            distribution.removeIf(d -> d.corruption == FightCorruptionRewardTypes.Small.Fragment || d.corruption == FightCorruptionRewardTypes.Small.Gold);
        }
        return distribution;
    }
}
