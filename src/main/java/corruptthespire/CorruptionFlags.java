package corruptthespire;

public class CorruptionFlags {
    public boolean seenTreasureWardens = false;
    public boolean seenSealedChest = false;
    public boolean openedSealedChest = false;
    public boolean seenVault = false;
    public boolean seenHarbinger = false;
    public boolean brokeDevice = false;
    public boolean seenServiceShop = false;
    public boolean foughtRottingShambler = false;
    public boolean foughtWisps = false;
    public boolean foughtHundredSouled = false;

    public int warAndFear = WarAndFear.NONE;
    public boolean foughtArchfiend = false;
    public boolean foughtMaster = false;

    public int normalMonsterCount = 0;
    public boolean hadFirstCorruptedNormalMonsterFight = false;

    public static class WarAndFear {
        public static final int NONE = 0;
        public static final int REPLACE_BOSS = 1;
        public static final int EXTRA_BOSS = 2;
        public static final int FOUGHT = 3;
    }
}
