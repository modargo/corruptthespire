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
        //TODO Set real distribution when done implementing and testing
        //CorruptedCardAndFragment 30
        //Prismatic 10
        //Rare 10
        //TransformReplacesRemove 10
        //CorruptedRelics 10
        //CorruptedCards 10
        //CorruptedRelicsReplacePotions 10
        //Something else 10
        //TBD what something else is
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

    @Override
    protected List<CorruptionDistributionInfo<ShopCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<ShopCorruptionType>> distribution) {
        if (AbstractDungeon.player.hasRelic(OminousBracelet.ID)) {
            distribution.removeIf(di -> di.corruption == ShopCorruptionType.CorruptedCardAndFragment);
        }
        return distribution;
    }
}
