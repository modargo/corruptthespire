package corruptthespire.corruptions.shop;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;
import corruptthespire.relics.corrupted.OminousBracelet;

import java.util.ArrayList;
import java.util.List;

public class ShopCorruptionDistribution extends AbstractCorruptionDistribution<ShopCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<ShopCorruptionType>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<ShopCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedCardAndFragment, 16));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.Prismatic, 14));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.Rare, 14));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedRelicsReplacePotions, 14));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.TransformReplacesRemove, 14));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedCards, 14));
        d.add(new CorruptionDistributionInfo<>(ShopCorruptionType.CorruptedRelics, 14));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<ShopCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<ShopCorruptionType>> distribution) {
        if (AbstractDungeon.player.hasRelic(OminousBracelet.ID)) {
            distribution.removeIf(di -> di.corruption == ShopCorruptionType.CorruptedCardAndFragment);
        }
        return distribution;
    }
}
