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
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.CorruptedCardAndRelic, 50));
        d.add(new CorruptionDistributionInfo<>(FightCorruptionRewardTypes.Large.CorruptedRelic, 50));
        return d;
    }
}
