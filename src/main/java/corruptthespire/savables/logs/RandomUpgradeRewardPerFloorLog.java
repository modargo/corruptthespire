package corruptthespire.savables.logs;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RandomUpgradeRewardPerFloorLog implements CustomSavable<List<List<String>>> {
    private static final Logger logger = LogManager.getLogger(RandomUpgradeRewardPerFloorLog.class.getName());
    public static final String SaveKey = "RandomUpgradeRewardPerFloor";
    public static List<List<String>> randomUpgradeRewardPerFloorLog;

    @Override
    public List<List<String>> onSave() {
        return RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog;
    }

    @Override
    public void onLoad(List<List<String>> randomUpgradeRewardLog) {
        RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog = randomUpgradeRewardLog;
        logger.info("AbstractDungeon.floorNum: " + AbstractDungeon.floorNum);
        if (randomUpgradeRewardLog == null) {
            logger.info("Specially initializing new log list, remove this before pushing");
            RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog.add(new ArrayList<>());
            }
        }
    }
}
