package corruptthespire.map;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.patches.CorruptedField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

public class CorruptMap {
    public static final Logger logger = LogManager.getLogger(CorruptMap.class.getName());
    private static final int RANDOM_EXTRA_CORRUPT_NODES = 3;
    //TODO: Test that resetting this when starting a new act works
    private static boolean isBossCorrupted = false;

    public static boolean isBossCorrupted() {
        return isBossCorrupted;
    }

    public static void setIsBossCorrupted(boolean value) {
        isBossCorrupted = value;
    }

    public static void markCorruptedNodes() {
        int actNum = AbstractDungeon.actNum;
        isBossCorrupted = actNum >= 3;
        ArrayList<MapRoomNode> potentialCorruptNodes = new ArrayList<>();
        for (int i = 0; i < AbstractDungeon.map.size(); i++) {
            for (int j = 0; j < AbstractDungeon.map.get(i).size(); j++) {
                MapRoomNode node = AbstractDungeon.map.get(i).get(j);
                //logger.info("Map node (" + i + ", " + j + "): " + (node.getRoom() != null ? node.getRoom().getClass().getTypeName() : "empty"));

                if (node.getRoom() instanceof MonsterRoomBoss) {
                    //Normally, boss rooms are created dynamically and are marked as corrupted through a patch
                    //But just in case there's one on the map, mark it properly
                    if (isBossCorrupted) {
                        MarkCorrupted(node);
                    }
                }
                if ((actNum == 3 && node.y == AbstractDungeon.map.size() - 1)
                    || node.hasEmeraldKey) {
                    MarkCorrupted(node);
                }
                else if (node.hasEdges() && !(actNum == 1 && node.y == 0)) {
                    potentialCorruptNodes.add(node);
                }
            }
        }

        //Just like mapRng in the base game, we don't track this because it's only used in one burst at the
        //start of each act -- and when reloading the game, which recreates the map for the act from scratch,
        //we need the result to be the same
        //Like the base game, we adjust this for each act. Without doing that, if two act maps had the same
        //number of nodes and a similar layout, the pattern of corrupted nodes would also be similar
        Random rng = new Random(Settings.seed + actNum);

        //TODO change back when done with initial testing
        //Maybe 30-40-50-100 for a more even increase in the first three acts
        //double percentCorrupt = actNum < 1 || actNum > 4 ? 0 : 1.0 / (5 - actNum); // 25%, 33%, 50%, 100%
        double percentCorrupt = 1.0;
        int baseCorrupt = (int)Math.ceil(potentialCorruptNodes.size() * percentCorrupt);
        int numCorrupt = Math.min(potentialCorruptNodes.size(), baseCorrupt + rng.random(RANDOM_EXTRA_CORRUPT_NODES));
        logger.info("Corrupting act " + actNum + ": " + baseCorrupt + " nodes base, " + numCorrupt + " after adjustment");

        Collections.shuffle(potentialCorruptNodes, rng.random);

        for (int i = 0; i < numCorrupt; i++) {
            MapRoomNode node = potentialCorruptNodes.get(i);
            if (node != null) {
                MarkCorrupted(node);
            }
            if (node == null || node.getRoom() instanceof MonsterRoomBoss) {
                isBossCorrupted = true;
            }
        }
    }

    private static void MarkCorrupted(MapRoomNode node) {
        CorruptedField.corrupted.set(node, true);
    }
}
