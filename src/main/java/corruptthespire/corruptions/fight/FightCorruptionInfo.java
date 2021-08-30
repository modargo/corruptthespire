package corruptthespire.corruptions.fight;

public class FightCorruptionInfo {
    public FightCorruptionType corruptionType;
    public Integer amount;
    public FightCorruptionSize size;

    public FightCorruptionInfo(FightCorruptionType corruptionType, Integer amount, FightCorruptionSize size) {
        this.corruptionType = corruptionType;
        this.amount = amount;
        this.size = size;
    }
}
