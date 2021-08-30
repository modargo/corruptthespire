package corruptthespire.corruptions.fight;

import corruptthespire.Cor;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionDistribution {
    public FightCorruptionInfo roll(int actNum, FightType fightType) {
        if (actNum < 1 || actNum > 4) {
            throw new RuntimeException("actNum must be between 1 and 4. Received: " + actNum);
        }

        ArrayList<FightCorruptionDistributionInfo> distribution = FightCorruptionDistributionReader.getFightCorruptionDistribution(actNum, fightType);
        int totalWeight = distribution.stream().map(cdi -> cdi.weight).reduce(0, Integer::sum);

        int roll = Cor.rng.random(totalWeight - 1);
        FightCorruptionDistributionInfo option = pick(distribution, roll);

        return new FightCorruptionInfo(option.corruptionType, option.amount, option.size);
    }

    private FightCorruptionDistributionInfo pick(List<FightCorruptionDistributionInfo> list, int roll) {
        int currentWeight = 0;

        for (FightCorruptionDistributionInfo info : list) {
            currentWeight += info.weight;
            if (roll < currentWeight) {
                return info;
            }
        }
        throw new RuntimeException("Could not pick a corruption option from the distribution.");
    }
}
