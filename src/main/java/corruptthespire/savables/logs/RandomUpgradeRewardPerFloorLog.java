package corruptthespire.savables.logs;

import basemod.abstracts.CustomSavable;

import java.util.List;

public class RandomUpgradeRewardPerFloorLog implements CustomSavable<List<List<String>>> {
    public static final String SaveKey = "RandomUpgradeRewardPerFloor";
    public static List<List<String>> randomUpgradeRewardPerFloorLog;

    @Override
    public List<List<String>> onSave() {
        return RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog;
    }

    @Override
    public void onLoad(List<List<String>> randomUpgradeRewardLog) {
        RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog = randomUpgradeRewardLog;
    }
}
