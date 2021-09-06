package corruptthespire.corruptions.fight;

public class FightCorruptionDistributionInfo {
    public final FightCorruptionType corruptionType;
    public final FightCorruptionSize size;
    public final int weight;
    public final int amount;
    public float adjustedWeight;

    public FightCorruptionDistributionInfo(FightCorruptionType corruptionType, FightCorruptionSize size, int weight, int amount) {
        this.corruptionType = corruptionType;
        this.size = size;
        this.weight = weight;
        this.amount = amount;
    }
}
