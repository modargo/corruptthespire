package corruptthespire.corruptions.fight.rewards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;
import corruptthespire.corruptions.event.EventCorruptionType;
import corruptthespire.relics.corrupted.OminousBracelet;

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

    @Override
    protected List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Small>> adjustDistribution(List<CorruptionDistributionInfo<FightCorruptionRewardTypes.Small>> distribution) {
        if (AbstractDungeon.actNum == 4) {
            distribution.removeIf(d -> d.corruption == FightCorruptionRewardTypes.Small.Fragment || d.corruption == FightCorruptionRewardTypes.Small.Gold);
        }
        return distribution;
    }
}
