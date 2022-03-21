package corruptthespire;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rooms.*;
import corruptthespire.buttons.CorruptionDisplay;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.relics.corrupted.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Cor {
    private static final Logger logger = LogManager.getLogger(Cor.class.getName());
    private static final HashSet<AbstractMonster> handledMonsters = new HashSet<>();

    public static final int CORRUPTION_FOR_NORMAL_FIGHT = 6;
    public static final int CORRUPTION_FOR_ELITE_FIGHT = 12;
    public static final int CORRUPTION_FOR_BOSS_FIGHT = 0;
    public static final int CORRUPTION_FOR_CHEST = 12;
    public static final int CORRUPTION_FOR_CAMPFIRE = 7;
    public static final int CORRUPTION_FOR_EVENT = 7;
    public static final int CORRUPTION_FOR_SHOP = 9;
    public static final int CORRUPTION_FOR_OTHER = 4;

    public static final int CORRUPTION_PER_DAMAGE_INCREASE = 5;

    public static Boolean active;
    public static Integer corruption;
    public static Random rng;
    public static Random rewardRng;
    public static CorruptionFlags flags;
    public static ArrayList<String> corruptedRelicPool;
    public static ArrayList<String> corruptedEventList;
    public static CorruptionDisplay display;
    public static ArrayList<Integer> corruptionPerFloor;

    public static Integer getCorruptionDamageMultiplierPercent() {
        return corruption / CORRUPTION_PER_DAMAGE_INCREASE;
    }

    public static void addCorruption(AbstractRoom room) {
        int corruption;
        Class<? extends AbstractRoom> c = room.getClass();
        if (c == MonsterRoom.class || c.getName().equals("ruina.rooms.RuinaMonsterRoom") || c.getName().equals("paleoftheancients.rooms.FixedMonsterRoom")) {
            corruption = CORRUPTION_FOR_NORMAL_FIGHT;
        }
        else if (c == MonsterRoomElite.class || c.getName().equals("infinitespire.rooms.NightmareEliteRoom")) {
            corruption = CORRUPTION_FOR_ELITE_FIGHT;
        }
        else if (c == MonsterRoomBoss.class) {
            corruption = CORRUPTION_FOR_BOSS_FIGHT;
        }
        else if (c == TreasureRoom.class) {
            corruption = CORRUPTION_FOR_CHEST;
        }
        else if (c == RestRoom.class) {
            corruption = CORRUPTION_FOR_CAMPFIRE;
        }
        else if (c == EventRoom.class) {
            corruption = CORRUPTION_FOR_EVENT;
        }
        else if (c == ShopRoom.class) {
            corruption = CORRUPTION_FOR_SHOP;
        }
        else {
            corruption = CORRUPTION_FOR_OTHER;
        }

        if (AbstractDungeon.player.hasRelic(OminousBracelet.ID)) {
            corruption = Math.max(corruption - OminousBracelet.CORRUPTION_REDUCTION_FOR_ROOMS, 0);
        }

        addCorruption(corruption);
    }

    public static void addCorruption(int corruption) {
        if (corruption == 0) {
            return;
        }
        logger.info("Adding corruption: " + corruption);
        Cor.corruption += corruption;
        Cor.display.update();
        Cor.display.flash();
    }

    public static void resetRng(Long seed, Integer counter) {
        int c = counter != null ? counter : 0;
        Cor.rng = new Random(seed, c);
        Cor.rewardRng = new Random(seed + 43, c);
    }

    //Gets the act number that we want to use for most purposes
    //This is always in the range 1-4, using the following logic:
    //* Acts with number >4 return 4
    //* Acts 1-3 in the second cycle and beyond of endless mode return 3
    //* Everything else returns the normal act number
    public static int getActNum() {
        return getActNumInfo().corruptionActNum;
    }

    public static int getRealActNum() {
        return getActNumInfo().realActNum;
    }

    private static ActNumInfo getActNumInfo() {
        ActNumInfo actNumInfo = new ActNumInfo();
        if (Loader.isModLoaded("actlikeit")) {
            // If ActLikeIt is loaded, it has the real act number for us, and
            // we can check that against AbstractDungeon.actNum
            int realActNum;
            try {
                Class<?> clz = Class.forName("actlikeit.savefields.BehindTheScenesActNum");
                Method m = clz.getMethod("getActNum");
                realActNum = (int)m.invoke(null);
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed when trying to call BehindTheScenesActNum.getActNum()", e);
            }
            if (realActNum != AbstractDungeon.actNum) {
                actNumInfo.corruptionActNum = realActNum < 4 ? 3 : 4;
            }
            else {
                actNumInfo.corruptionActNum = Math.min(AbstractDungeon.actNum, 4);
            }
            actNumInfo.realActNum = realActNum;
        }
        else {
            // If ActLikeIt isn't loaded, we can safely enumerate the possible acts
            // and use that to determine the corruption and real act numbers
            actNumInfo.corruptionActNum = AbstractDungeon.id.equals(TheEnding.ID) ? 4
                : AbstractDungeon.actNum < 4 ? AbstractDungeon.actNum
                : 3;
            switch (AbstractDungeon.id) {
                case Exordium.ID:
                    actNumInfo.realActNum = 1;
                    break;
                case TheCity.ID:
                case "theJungle:Jungle":
                    actNumInfo.realActNum = 2;
                    break;
                case TheBeyond.ID:
                    actNumInfo.realActNum = 3;
                    break;
                case TheEnding.ID:
                    actNumInfo.realActNum = 4;
                    break;
                default:
                    throw new RuntimeException("Unrecognized act ID: " + AbstractDungeon.id + ". When ActLikeIt isn't loaded, the only possible acts should be the vanilla four and The Jungle.");
            }
        }

        return actNumInfo;
    }

    public static int getFragmentCount() {
        AbstractRelic r = AbstractDungeon.player.getRelic(FragmentOfCorruption.ID);
        return r != null ? r.counter : 0;
    }

    public static void reduceFragments(int amount) {
        FragmentOfCorruption r = (FragmentOfCorruption)AbstractDungeon.player.getRelic(FragmentOfCorruption.ID);
        r.reduce(amount);
    }

    public static void applyCorruptionHealthIncrease(AbstractMonster m) {
        if (!handledMonsters.contains(m)) {
            int amount = (m.maxHealth * Cor.getCorruptionDamageMultiplierPercent()) / 100;
            if (amount > 0) {
                m.increaseMaxHp(amount, false);
            }
            handledMonsters.add(m);
        }
    }

    public static AbstractRelic returnRandomCorruptedRelic() {
        return RelicLibrary.getRelic(returnRandomCorruptedRelicKey()).makeCopy();
    }

    public static AbstractRelic returnEndRandomCorruptedRelic() {
        return RelicLibrary.getRelic(returnEndRandomCorruptedRelicKey()).makeCopy();
    }

    private static String returnRandomCorruptedRelicKey() {
        if (Cor.corruptedRelicPool.isEmpty()) {
            fillCorruptedRelicPool(true);
        }
        if (Cor.corruptedRelicPool.isEmpty()) {
            return Circlet.ID;
        }
        String relicKey = Cor.corruptedRelicPool.remove(0);
        if (!RelicLibrary.getRelic(relicKey).canSpawn()) {
            return returnRandomCorruptedRelicKey();
        }
        return relicKey;
    }

    private static String returnEndRandomCorruptedRelicKey() {
        if (Cor.corruptedRelicPool.isEmpty()) {
            fillCorruptedRelicPool(true);
        }
        if (Cor.corruptedRelicPool.isEmpty()) {
            return Circlet.ID;
        }
        String relicKey = Cor.corruptedRelicPool.remove(Cor.corruptedRelicPool.size() - 1);
        if (!RelicLibrary.getRelic(relicKey).canSpawn()) {
            return returnEndRandomCorruptedRelicKey();
        }
        return relicKey;
    }

    public static void fillCorruptedRelicPool(boolean checkPlayerRelics) {
        if (checkPlayerRelics) {
            logger.info("Refilling corrupted relic pool with relics the player doesn't already have");
        }
        else {
            logger.info("Initializing corrupted relic pool");
        }
        ArrayList<String> corruptedRelics = Cor.getAllCorruptedRelics().stream()
                .filter(r -> !checkPlayerRelics || !AbstractDungeon.player.hasRelic(r.relicId))
                .map(r -> r.relicId)
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(corruptedRelics, new Random(Settings.seed).random);
        Cor.corruptedRelicPool = corruptedRelics;
    }

    public static ArrayList<AbstractCorruptedRelic> getAllCorruptedRelics() {
        ArrayList<AbstractCorruptedRelic> corruptedRelics = new ArrayList<>();
        corruptedRelics.add(new AbyssalOrb());
        corruptedRelics.add(new BagOfTricks());
        corruptedRelics.add(new BlackCard());
        corruptedRelics.add(new BlightedSpirefruit());
        corruptedRelics.add(new BottledPrism());
        corruptedRelics.add(new CorruptedEgg());
        corruptedRelics.add(new CorruptedToolbox());
        corruptedRelics.add(new Gohei());
        corruptedRelics.add(new HeavySword());
        corruptedRelics.add(new HideousStatue());
        corruptedRelics.add(new MaskOfNightmares());
        corruptedRelics.add(new MystifyingTimepiece());
        corruptedRelics.add(new ObsidianShovel());
        corruptedRelics.add(new OminousBracelet());
        corruptedRelics.add(new OozingHeart());
        corruptedRelics.add(new RustedOrichalcum());
        corruptedRelics.add(new SlimeShield());
        corruptedRelics.add(new UnreliableCharm());
        return corruptedRelics;
    }

    private static class ActNumInfo {
        public int corruptionActNum;
        public int realActNum;
    }
}
