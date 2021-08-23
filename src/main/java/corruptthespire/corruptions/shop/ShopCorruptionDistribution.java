package corruptthespire.corruptions.shop;

import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class ShopCorruptionDistribution extends AbstractCorruptionDistribution<ShopCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<ShopCorruptionType>> getDistribution() {
        //TODO Set real distribution when done implementing and testing
        //Fragment 50, Money 10, Extra 10, Vault 10, CorruptedRelic 10, something else 10?
        //Something else could be the fight idea
        ArrayList<CorruptionDistributionInfo<ShopCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.Prismatic, 0));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.Rare, 0));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedRelicsReplacePotions, 0));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.TransformReplacesRemove, 0));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedCards, 0));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedCardAndFragment, 0));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedRelics, 100));
        return d;
    }
}
