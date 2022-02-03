package corruptthespire.corruptions;

import corruptthespire.Cor;

import java.util.List;

public abstract class AbstractCorruptionDistribution<T extends Enum<T>> {
    private static final int DEFAULT_TOTAL_WEIGHT = 100;

    public T roll() {
        List<CorruptionDistributionInfo<T>> distribution = this.getDistribution();
        int totalWeight = distribution.stream().map(cdi -> cdi.weight).reduce(0, Integer::sum);
        if (totalWeight != this.getTotalWeight()) {
            throw new RuntimeException("Excepted total weight to be 100, was " + totalWeight);
        }

        distribution = this.adjustDistribution(distribution);
        int adjustedTotalWeight = distribution.stream().map(cdi -> cdi.weight).reduce(0, Integer::sum);

        int roll = Cor.rng.random(adjustedTotalWeight - 1);
        CorruptionDistributionInfo<T> option = pick(distribution, roll);
        return option.corruption;
    }

    protected abstract List<CorruptionDistributionInfo<T>> getDistribution();

    protected int getTotalWeight() {
        return DEFAULT_TOTAL_WEIGHT;
    }

    protected List<CorruptionDistributionInfo<T>> adjustDistribution(List<CorruptionDistributionInfo<T>> distribution) {
        return distribution;
    }

    private CorruptionDistributionInfo<T> pick(List<CorruptionDistributionInfo<T>> list, int roll) {
        int currentWeight = 0;

        for (CorruptionDistributionInfo<T> info : list) {
            currentWeight += info.weight;
            if (roll < currentWeight) {
                return info;
            }
        }
        throw new RuntimeException("Could not pick a corruption option from the distribution.");
    }
}