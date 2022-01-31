package corruptthespire.corruptions.fight.rewards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.Cor;
import corruptthespire.corruptions.fight.FightCorruptionSize;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.rewards.CorruptedCardReward;
import corruptthespire.rewards.MaxHealthReward;
import corruptthespire.rewards.RandomUpgradeReward;

public class FightCorruptionReward {
    private static final float GOLD_FUZZ_FACTOR = 0.9F;
    private static final int GOLD_SMALL = 50;
    private static final int GOLD_MEDIUM = 100;
    private static final int MAX_HEALTH = 4;

    public static void addReward(FightCorruptionSize size) {
        switch(size) {
            case S:
                addSmallReward();
                break;
            case M:
                addMediumReward();
                break;
            case L:
                addLargeReward();
                break;
        }
    }

    private static void addSmallReward() {
        FightCorruptionRewardTypes.Small rewardType = new FightCorruptionSmallRewardDistribution().roll();
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        switch (rewardType) {
            case CorruptedCard:
                room.rewards.add(new CorruptedCardReward());
                break;
            case Fragment:
                room.addRelicToRewards(new FragmentOfCorruption());
                break;
            case Upgrade:
                room.rewards.add(new RandomUpgradeReward());
                break;
            case MaxHealth:
                room.rewards.add(new MaxHealthReward(MAX_HEALTH));
                break;
            case Gold:
                room.addGoldToRewards(fuzzGold(GOLD_SMALL));
                break;
        }
    }

    private static void addMediumReward() {
        FightCorruptionRewardTypes.Medium rewardType = new FightCorruptionMediumRewardDistribution().roll();
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        switch (rewardType) {
            case CorruptedCardAndFragment:
                room.addRelicToRewards(new FragmentOfCorruption());
                room.rewards.add(new CorruptedCardReward());
                break;
            case Fragments:
                room.addRelicToRewards(new FragmentOfCorruption());
                room.addRelicToRewards(new FragmentOfCorruption());
                break;
            case MaxHealthAndUpgrade:
                room.rewards.add(new MaxHealthReward(MAX_HEALTH));
                room.rewards.add(new RandomUpgradeReward());
                break;
            case Gold:
                room.addGoldToRewards(fuzzGold(GOLD_MEDIUM));
                break;
        }
    }

    private static void addLargeReward() {
        FightCorruptionRewardTypes.Large rewardType = new FightCorruptionLargeRewardDistribution().roll();
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        switch (rewardType) {
            case CorruptedCardAndRelic:
                room.rewards.add(new CorruptedCardReward());
                room.addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                break;
            case CorruptedRelic:
                room.addRelicToRewards(Cor.returnRandomCorruptedRelic());
                break;
        }
    }

    private static int fuzzGold(int gold) {
        return Cor.rng.random((int)(gold * GOLD_FUZZ_FACTOR), gold);
    }
}
