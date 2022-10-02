package corruptthespire.corruptions.fight.room;

import corruptthespire.Cor;
import corruptthespire.corruptions.AbstractCorruptionDistribution;
import corruptthespire.corruptions.CorruptionDistributionInfo;

import java.util.ArrayList;
import java.util.List;

public class FightRoomCorruptionDistribution extends AbstractCorruptionDistribution<FightRoomCorruptionType> {
    @Override
    protected List<CorruptionDistributionInfo<FightRoomCorruptionType>> getDistribution() {
        ArrayList<CorruptionDistributionInfo<FightRoomCorruptionType>> d = new ArrayList<>();
        d.add(new CorruptionDistributionInfo<>(FightRoomCorruptionType.Normal, 84));
        d.add(new CorruptionDistributionInfo<>(FightRoomCorruptionType.RottingShambler, 8));
        d.add(new CorruptionDistributionInfo<>(FightRoomCorruptionType.Wisps, 8));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<FightRoomCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<FightRoomCorruptionType>> distribution) {
        if (Cor.getRealActNum() != 1 || Cor.flags.foughtRottingShambler || Cor.flags.normalMonsterCount < 3) {
            distribution.removeIf(di -> di.corruption == FightRoomCorruptionType.RottingShambler);
        }
        if (Cor.getRealActNum() != 2 || Cor.flags.foughtWisps || Cor.flags.normalMonsterCount < 2) {
            distribution.removeIf(di -> di.corruption == FightRoomCorruptionType.Wisps);
        }
        return distribution;
    }
}
