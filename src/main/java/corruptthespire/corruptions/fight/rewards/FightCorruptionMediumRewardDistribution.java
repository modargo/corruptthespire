package corruptthespire.corruptions.fight.rewards;

import com.megacrit.cardcrawl.random.Random;
import corruptthespire.Cor;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionMediumRewardDistribution extends AbstractCorruptionDistribution<FightCorruptionRewardTypes.Medium> {
    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Medium>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<FightCorruptionRewardTypes.Medium>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.CorruptedCardAndFragment, 25));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.Fragments, 25));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.MaxHealthAndUpgrade, 25));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Medium.Gold, 25));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Medium>> adjustDistribution(List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Medium>> distribution) {
        if (Cor.getActNum() == 4) {
            distribution.removeIf(d -> d.corruption == FightCorruptionRewardTypes.Medium.Fragments || d.corruption == FightCorruptionRewardTypes.Medium.Gold);
        }
        return distribution;
    }

    @Override
    protected Random getRng() {
        return Cor.rewardRng;
    }
}
