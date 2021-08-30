package corruptthespire.corruptions.fight;

public class FightCorruptionDistributionInfo {
    public FightCorruptionType corruptionType;
    public FightCorruptionSize size;
    public int weight;
    public int amount;

    public FightCorruptionDistributionInfo(FightCorruptionType corruptionType, FightCorruptionSize size, int weight, int amount) {
        this.corruptionType = corruptionType;
        this.size = size;
        this.weight = weight;
        this.amount = amount;
    }
}
