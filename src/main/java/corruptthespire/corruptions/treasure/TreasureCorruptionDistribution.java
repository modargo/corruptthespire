package corruptthespire.corruptions.treasure;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;
import corruptthespire.relics.corrupted.OminousBracelet;

import java.util.ArrayList;
import java.util.List;

public class TreasureCorruptionDistribution extends AbstractCorruptionDistribution<TreasureCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<TreasureCorruptionType>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<TreasureCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Fragment, 22));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Money, 12));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Extra, 12));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Vault, 12));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.CorruptedRelic, 12));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Wardens, 18));
        d.add(new CorruptionDistributionInfo<>(TreasureCorruptionType.Sealed, 12));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<TreasureCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<TreasureCorruptionType>> distribution) {
        boolean isAct4 = Cor.getActNum() == 4;
        if (AbstractDungeon.player.hasRelic(OminousBracelet.ID)) {
            distribution.removeIf(di -> di.corruption == TreasureCorruptionType.Fragment);
        }
        if (Cor.flags.seenTreasureWardens || AbstractDungeon.floorNum <= 6 || isAct4) {
            distribution.removeIf(di -> di.corruption == TreasureCorruptionType.Wardens);
        }
        if (Cor.flags.seenSealedChest || isAct4) {
            distribution.removeIf(di -> di.corruption == TreasureCorruptionType.Sealed);
        }
        if (Cor.flags.seenVault) {
            distribution.removeIf(di -> di.corruption == TreasureCorruptionType.Vault);
        }
        return distribution;
    }
}
