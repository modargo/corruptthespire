package corruptthespire.corruptions.fight;

public class FightCorruptionInfo {
    public FightCorruptionType corruptionType;
    public Integer amount;

    public FightCorruptionInfo(FightCorruptionType corruptionType, Integer amount) {
        this.corruptionType = corruptionType;
        this.amount = amount;
    }
}
