package corruptthespire;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

import java.util.ArrayList;

public class Cor {
    private static final Logger logger = LogManager.getLogger(Cor.class.getName());

    public static final int CORRUPTION_FOR_NORMAL_FIGHT = 6;
    public static final int CORRUPTION_FOR_ELITE_FIGHT = 12;
    public static final int CORRUPTION_FOR_BOSS_FIGHT = 0;
    public static final int CORRUPTION_FOR_CHEST = 13;
    public static final int CORRUPTION_FOR_CAMPFIRE = 8;
    public static final int CORRUPTION_FOR_EVENT = 8;
    public static final int CORRUPTION_FOR_SHOP = 10;
    public static final int CORRUPTION_FOR_OTHER = 4;

    public static final int CORRUPTION_PER_DAMAGE_INCREASE = 5;
    
    public static Integer corruption;
    public static Random rng;
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
        if (c == MonsterRoom.class) {
            corruption = CORRUPTION_FOR_NORMAL_FIGHT;
        }
        else if (c == MonsterRoomElite.class) {
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

    public static int getFragmentCount() {
        AbstractRelic r = AbstractDungeon.player.getRelic(FragmentOfCorruption.ID);
        return r != null ? r.counter : 0;
    }

    public static void reduceFragments(int amount) {
        FragmentOfCorruption r = (FragmentOfCorruption)AbstractDungeon.player.getRelic(FragmentOfCorruption.ID);
        r.reduce(amount);
    }

    public static void applyCorruptionHealthIncrease(AbstractMonster m) {
        int amount = (m.maxHealth * Cor.getCorruptionDamageMultiplierPercent()) / 100;
        if (amount > 0) {
            m.increaseMaxHp(amount, false);
        }
    }

    public static String returnRandomCorruptedRelicKey() {
        if (Cor.corruptedRelicPool.isEmpty()) {
            return Circlet.ID;
        }
        String relicKey = Cor.corruptedRelicPool.remove(0);
        if (!RelicLibrary.getRelic(relicKey).canSpawn()) {
            return returnRandomCorruptedRelicKey();
        }
        return relicKey;
    }

    public static String returnEndRandomCorruptedRelicKey() {
        if (Cor.corruptedRelicPool.isEmpty()) {
            return Circlet.ID;
        }
        String relicKey = Cor.corruptedRelicPool.remove(Cor.corruptedRelicPool.size() - 1);
        if (!RelicLibrary.getRelic(relicKey).canSpawn()) {
            return returnEndRandomCorruptedRelicKey();
        }
        return relicKey;
    }

    public static ArrayList<AbstractCorruptedRelic> getAllCorruptedRelics() {
        ArrayList<AbstractCorruptedRelic> corruptedRelics = new ArrayList<>();
        corruptedRelics.add(new BagOfTricks());
        corruptedRelics.add(new BlackCard());
        corruptedRelics.add(new BlightedSpirefruit());
        corruptedRelics.add(new BottledPrism());
        corruptedRelics.add(new CorruptedEgg());
        corruptedRelics.add(new CorruptedOmamori());
        corruptedRelics.add(new CorruptedToolbox());
        corruptedRelics.add(new Gohei());
        corruptedRelics.add(new MaskOfNightmares());
        corruptedRelics.add(new MystifyingTimepiece());
        corruptedRelics.add(new ObsidianShovel());
        corruptedRelics.add(new OminousBracelet());
        corruptedRelics.add(new OozingHeart());
        corruptedRelics.add(new RustedOrichalcum());
        corruptedRelics.add(new UnreliableCharm());
        return corruptedRelics;
    }
}
