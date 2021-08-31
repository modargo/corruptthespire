package corruptthespire.corruptions.fight;

public class FightCorruptionInfo {
    public final FightCorruptionType corruptionType;
    public final Integer amount;
    public final FightCorruptionSize size;

    public FightCorruptionInfo(FightCorruptionType corruptionType, Integer amount, FightCorruptionSize size) {
        this.corruptionType = corruptionType;
        this.amount = amount;
        this.size = size;
    }
}
