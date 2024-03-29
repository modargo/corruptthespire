package corruptthespire.corruptions.fight;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import corruptthespire.Cor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FightCorruptionDistribution {
    private static final Logger logger = LogManager.getLogger(FightCorruptionDistribution.class.getName());
    public FightCorruptionInfo roll(int actNum, FightType fightType) {
        logger.info("Rolling corruption for act and fight type: " + actNum + ", " + fightType.name());

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
        if (AbstractDungeon.getCurrRoom().monsters.monsters.size() >= 3 || hasMonster("Elementarium:ElementalPortal") || hasMonster("Abyss:PrimevalQueen")) {
            distribution.removeIf(d -> isMinionCorruption(d.corruptionType));
        }
        if (hasMonster(BookOfStabbing.ID)) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Ritual);
        }
        if (hasMonster(Nemesis.ID)) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.FlameManifestMinion);
        }
        if (AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(m -> m.id.equals(Cultist.ID))) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Ritual);
        }
        if (hasMonster("Menagerie:MaskedSummoner")) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Metallicize);
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Malleable);
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Buffer);
        }
        if (hasMonster(AwakenedOne.ID)) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Curiosity);
        }
        if (hasMonster(Donu.ID)) {
            distribution.removeIf(d -> d.corruptionType == FightCorruptionType.Artifact);
        }
        if (AbstractDungeon.getCurrRoom().monsters.monsters.stream().filter(c -> c.id.equals(Byrd.ID)).count() >= 3) {
            this.adjustStrengthCorruption(distribution, 0.5f);
        }

        if (actNum == 4 && fightType == FightType.Boss && !hasMonster(CorruptHeart.ID)) {
            this.adjustStrengthCorruption(distribution, 3.0f);
        }
    }

    private void adjustStrengthCorruption(ArrayList<FightCorruptionDistributionInfo> distribution, float strengthMultiplier) {
        FightCorruptionDistributionInfo strengthCorruption = null;
        for (FightCorruptionDistributionInfo d : distribution) {
            if (d.corruptionType == FightCorruptionType.Strength) {
                strengthCorruption = d;
                break;
            }
        }
        if (strengthCorruption != null) {
            int newStrength = (int)(strengthCorruption.amount * strengthMultiplier);
            FightCorruptionDistributionInfo di = new FightCorruptionDistributionInfo(strengthCorruption.corruptionType, strengthCorruption.size, strengthCorruption.weight, newStrength);
            distribution.set(distribution.indexOf(strengthCorruption), di);
        }
    }

    private static boolean hasMonster(String id) {
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(m -> m.id.equals(id));
    }

    private static boolean isMinionCorruption(FightCorruptionType f) {
        return f == FightCorruptionType.CorruptionManifestMinion
            || f == FightCorruptionType.FlameManifestMinion
            || f == FightCorruptionType.StrifeManifestMinion
            || f == FightCorruptionType.DeliriumManifestMinion
            || f == FightCorruptionType.DevourerMinion
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
