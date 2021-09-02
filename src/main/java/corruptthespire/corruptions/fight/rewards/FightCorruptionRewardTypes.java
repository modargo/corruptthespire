package corruptthespire.corruptions.fight.rewards;

public class FightCorruptionRewardTypes {
    public enum Small {
        CorruptedCard,
        Fragment,
        Potion,
        Upgrade,
        Gold
    }

    public enum Medium {
        CorruptedCard,
        Fragments,
        CommonRelic,
        CorruptedRelic,
        Upgrade,
        MaxHealth,
        Gold
    }

    public enum Large {
        CorruptedCardAndFragment,
        Relic,
        CorruptedRelic,
        FragmentAndRelic,
        MaxHealthAndUpgrade,
        Gold
    }
}
