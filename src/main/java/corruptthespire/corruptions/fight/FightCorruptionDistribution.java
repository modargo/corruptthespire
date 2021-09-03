package corruptthespire.corruptions.fight;

import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionDistribution {
    private static final Logger logger = LogManager.getLogger(FightCorruptionDistribution.class.getName());
    public FightCorruptionInfo roll(int actNum, FightType fightType) {
        if (actNum < 1 || actNum > 4) {
            throw new RuntimeException("actNum must be between 1 and 4. Received: " + actNum);
        }

        ArrayList<FightCorruptionDistributionInfo> distribution = FightCorruptionDistributionReader.getFightCorruptionDistribution(actNum, fightType);
        if ((fightType == FightType.Easy || fightType == FightType.Hard) && !Cor.flags.hadFirstCorruptedNormalMonsterFight) {
            distribution.removeIf(d -> d.size != FightCorruptionSize.S);
        }
        int totalWeight = distribution.stream().map(cdi -> cdi.weight).reduce(0, Integer::sum);

        logger.info("Rolling fight corruption. Cor.rng.counter: " + Cor.rng.counter);
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
