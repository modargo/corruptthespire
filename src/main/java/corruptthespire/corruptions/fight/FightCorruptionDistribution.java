package corruptthespire.corruptions.fight;

import corruptthespire.corruptions.AbstractCorruptionDistribution;

public class FightCorruptionDistribution {
    public FightCorruptionInfo roll(int actNum, FightType fightType) {
        if (actNum < 1 || actNum > 4) {
            throw new RuntimeException("actNum must be between 1 and 4. Received: " + actNum);
        }

        //TODO: Actually implement rolling from a distribution
        return new FightCorruptionInfo(FightCorruptionType.Metallicize, 2);
    }
}
