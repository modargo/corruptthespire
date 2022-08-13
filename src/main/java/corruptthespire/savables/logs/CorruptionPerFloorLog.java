package corruptthespire.savables.logs;

import basemod.abstracts.CustomSavable;

import java.util.ArrayList;

public class CorruptionPerFloorLog implements CustomSavable<ArrayList<Integer>> {
    public final static String SaveKey = "CorruptionPerFloor";

    public static ArrayList<Integer> corruptionPerFloorLog;

    @Override
    public ArrayList<Integer> onSave() {
        return CorruptionPerFloorLog.corruptionPerFloorLog;
    }

    @Override
    public void onLoad(ArrayList<Integer> corruptionPerFloorLog) {
        CorruptionPerFloorLog.corruptionPerFloorLog = corruptionPerFloorLog != null ? corruptionPerFloorLog : new ArrayList<>();
    }
}