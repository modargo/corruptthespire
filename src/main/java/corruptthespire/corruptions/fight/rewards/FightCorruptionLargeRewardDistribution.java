package corruptthespire.corruptions.fight.rewards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionLargeRewardDistribution extends AbstractCorruptionDistribution<FightCorruptionRewardTypes.Large> {
    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Large>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<FightCorruptionRewardTypes.Large>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.CorruptedCardAndFragment, 25));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.CorruptedRelic, 50));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.Gold, 25));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Large>> adjustDistribution(List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Large>> distribution) {
        if (AbstractDungeon.actNum == 4) {
            distribution.removeIf(d -> d.corruption == FightCorruptionRewardTypes.Large.Gold);
        }
        return distribution;
    }
}
