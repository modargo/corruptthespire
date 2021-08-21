package corruptthespire.corruptions;

public class CorruptionDistributionInfo<T extends Enum<T>> {
    public T corruption;
    public int weight;

    public CorruptionDistributionInfo( T corruption, int weight) {
        this.corruption = corruption;
        this.weight = weight;
    }
}
