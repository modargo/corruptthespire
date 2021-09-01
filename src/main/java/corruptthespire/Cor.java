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

    //TODO Adjust all of these to set the right risk/reward balance
    //E.g. if fights turn out to be the riskiest, they should generate less corruption
    //TODO Consider doubling everything here so that cards can have more granular corruption gain
    //I think I need to, since the values I'm trending towards are getting quite small
    //I want 1 corruption on a card to be "This doesn't seem too bad, but I know it builds up"
    //I don't want 1 corruption on a card to be "Dear god, this is nearly as much as the fight I'm in"
    //(Having 5 corruption be that is fine)
    //So yeah, double everything here, then take a pass through the corrupted cards
    //Corruption gains on cards are probably fine, but corruption thresholds may need to be bumped up
    public static final int CORRUPTION_FOR_NORMAL_FIGHT = 3;
    public static final int CORRUPTION_FOR_ELITE_FIGHT = 8;
    public static final int CORRUPTION_FOR_BOSS_FIGHT = 0;
    public static final int CORRUPTION_FOR_CHEST = 8;
    public static final int CORRUPTION_FOR_CAMPFIRE = 4;
    public static final int CORRUPTION_FOR_EVENT = 4;
    public static final int CORRUPTION_FOR_SHOP = 5;
    public static final int CORRUPTION_FOR_OTHER = 5;

    public static final int CORRUPTION_PER_DAMAGE_INCREASE = 2;
    
    public static Integer corruption;
    public static Random rng;
    public static CorruptionFlags flags;
    public static ArrayList<String> corruptedRelicPool;
    public static ArrayList<String> corruptedEventList;
    public static CorruptionDisplay display;

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
        if (Cor.corruption > 0) {
            m.increaseMaxHp((m.maxHealth * Cor.getCorruptionDamageMultiplierPercent()) / 100, false);
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
