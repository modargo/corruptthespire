package corruptthespire.corruptions.fight.rewards;

public class FightCorruptionRewardTypes {
    public enum Small {
        CorruptedCard,
        Fragment,
        Upgrade,
        MaxHealth,
        Gold
    }

    public enum Medium {
        CorruptedCardAndFragment,
        Fragments,
        MaxHealthAndUpgrade,
        Gold
    }

    public enum Large {
        CorruptedCardAndRelic,
        CorruptedRelic
    }
}
