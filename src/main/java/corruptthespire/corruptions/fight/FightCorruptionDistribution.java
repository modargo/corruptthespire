package corruptthespire.corruptions.fight;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import corruptthespire.Cor;
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
        this.adjustDistribution(distribution, actNum, fightType);
        if (distribution.isEmpty()) {
            throw new RuntimeException("No fight corruptions to choose from.");
        }
        int totalWeight = distribution.stream().map(cdi -> cdi.weight).reduce(0, Integer::sum);

        logger.info("Rolling fight corruption. Cor.rng.counter: " + Cor.rng.counter);
        //logger.info("Distribution: " + distribution.stream().map(e -> "(" + e.corruptionType + ", " + e.weight + ")").reduce("", (s1, s2) -> s1 + " " + s2));
        //logger.info("Total weight: " + totalWeight);
        //int roll = Cor.rng.random(totalWeight - 1);

        for (FightCorruptionDistributionInfo fightCorruptionDistributionInfo : distribution) {
            fightCorruptionDistributionInfo.adjustedWeight = fightCorruptionDistributionInfo.weight / (float) totalWeight;
        }
        logger.info("Distribution: " + distribution.stream().map(e -> "(" + e.corruptionType + ", " + e.adjustedWeight + ")").reduce("", (s1, s2) -> s1 + " " + s2));

        float roll = Cor.rng.random();
        logger.info("Roll: " + roll);
        FightCorruptionDistributionInfo option = pick(distribution, roll);
        logger.info("Picked: " + option.corruptionType);

        return new FightCorruptionInfo(option.corruptionType, option.amount, option.size);
    }

    private void adjustDistribution(ArrayList<FightCorruptionDistributionInfo> distribution, int actNum, FightType fightType) {
        if ((fightType == FightType.Easy || fightType == FightType.Hard) && !Cor.flags.hadFirstCorruptedNormalMonsterFight) {
            distribution.removeIf(d -> d.size != FightCorruptionSize.S);
        }
        if (AbstractDungeon.getCurrRoom().monsters.monsters.size() >= 3) {
            distribution.removeIf(d -> isMinionCorruption(d.corruptionType));
        }
        if (AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(m -> m.hasPower(CuriosityPower.POWER_ID))) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Curiosity);
        }
        if (AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(m -> m.hasPower("Abyss:ThoughtStealer"))) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.ThoughtStealer);
        }
        if (actNum == 4 && fightType == FightType.Boss && AbstractDungeon.getCurrRoom().monsters.monsters.stream().noneMatch(m -> m.id.equals(CorruptHeart.ID))) {
            FightCorruptionDistributionInfo strengthCorruption = null;
            for (FightCorruptionDistributionInfo d : distribution) {
                if (d.corruptionType == FightCorruptionType.Strength) {
                    strengthCorruption = d;
                    break;
                }
            }
            if (strengthCorruption != null) {
                int strengthMultiplierForNonHeartBosses = 3;
                distribution.remove(strengthCorruption);
                distribution.add(new FightCorruptionDistributionInfo(strengthCorruption.corruptionType, strengthCorruption.size, strengthCorruption.weight, strengthCorruption.amount * strengthMultiplierForNonHeartBosses));
            }
        }

    }

    private static boolean isMinionCorruption(FightCorruptionType f) {
        return f == FightCorruptionType.CorruptionManifestMinion
            || f == FightCorruptionType.LouseMinion
            || f == FightCorruptionType.SlimeMinion
            || f == FightCorruptionType.GremlinMinion
            || f == FightCorruptionType.RepulsorMinion
            || f == FightCorruptionType.ByrdMinion
            || f == FightCorruptionType.SnakeDaggerMinion
            || f == FightCorruptionType.CultistMinion;

    }

    private FightCorruptionDistributionInfo pick(List<FightCorruptionDistributionInfo> list, float roll) {
        float currentWeight = 0;

        for (FightCorruptionDistributionInfo info : list) {
            currentWeight += info.adjustedWeight;
            if (roll < currentWeight) {
                return info;
            }
        }
        throw new RuntimeException("Could not pick a corruption option from the distribution.");
    }
}
