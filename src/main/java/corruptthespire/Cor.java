package corruptthespire;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rooms.*;
import corruptthespire.buttons.CorruptionDisplay;
import corruptthespire.cards.*;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.relics.corrupted.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Cor {
    public static final Logger logger = LogManager.getLogger(Cor.class.getName());

    //TODO Adjust all of these to set the right risk/reward balance
    //E.g. if fights turn out to be the riskiest, they should generate less corruption
    public static final int CORRUPTION_FOR_NORMAL_FIGHT = 5;
    public static final int CORRUPTION_FOR_ELITE_FIGHT = 13;
    public static final int CORRUPTION_FOR_BOSS_FIGHT = 0; //TODO Decide if this should stay at 0 or not
    public static final int CORRUPTION_FOR_CHEST = 15;
    public static final int CORRUPTION_FOR_CAMPFIRE = 10;
    public static final int CORRUPTION_FOR_EVENT = 7;
    public static final int CORRUPTION_FOR_SHOP = 10;
    public static final int CORRUPTION_FOR_OTHER = 10;
    
    public static Integer corruption;
    public static Random rng;
    public static ArrayList<String> corruptedRelicPool;
    public static CorruptionDisplay display;

    public static Integer getCorruptionDamageMultiplierPercent() {
        return corruption / 2;
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

    public static String returnRandomCorruptedRelicKey() {
        if (Cor.corruptedRelicPool.isEmpty()) {
            return Circlet.ID;
        }
        return Cor.corruptedRelicPool.remove(0);
    }

    public static ArrayList<AbstractCard> getAllCorruptedCards() {
        ArrayList<AbstractCard> corruptedCards = new ArrayList<>();
        corruptedCards.add(new Shadowblast());
        corruptedCards.add(new EssenceRip());
        corruptedCards.add(new Condemn());
        corruptedCards.add(new Recoil());
        corruptedCards.add(new DrainLife());
        corruptedCards.add(new Fragmentize());
        return corruptedCards;
    }

    public static ArrayList<AbstractRelic> getAllCorruptedRelics() {
        ArrayList<AbstractRelic> corruptedRelics = new ArrayList<>();
        corruptedRelics.add(new BagOfTricks());
        corruptedRelics.add(new BlightedSpirefruit());
        corruptedRelics.add(new CorruptedEgg());
        corruptedRelics.add(new ObsidianShovel());
        corruptedRelics.add(new RustedOrichalcum());
        corruptedRelics.add(new UnreliableCharm());
        return corruptedRelics;
    }
}
