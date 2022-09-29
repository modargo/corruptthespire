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
        d.add(new CorruptionDistributionInfo<>(FightRoomCorruptionType.Normal, 90));
        d.add(new CorruptionDistributionInfo<>(FightRoomCorruptionType.RottingShambler, 10));
        return d;
    }

    @Override
    protected List<CorruptionDistributionInfo<FightRoomCorruptionType>> adjustDistribution(List<CorruptionDistributionInfo<FightRoomCorruptionType>> distribution) {
        if (Cor.getRealActNum() != 1 || Cor.flags.foughtRottingShambler || Cor.flags.normalMonsterCount < 3) {
            distribution.removeIf(di -> di.corruption == FightRoomCorruptionType.RottingShambler);
        }
        return distribution;
    }
}
