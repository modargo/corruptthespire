package corruptthespire.savables.logs;

import basemod.abstracts.CustomSavable;

import java.util.ArrayList;

public class CorruptionTypePerFloorLog implements CustomSavable<ArrayList<String>> {
    public final static String SaveKey = "CorruptionTypePerFloor";

    public static ArrayList<String> corruptionTypePerFloorLog;

    @Override
    public ArrayList<String> onSave() {
        return CorruptionTypePerFloorLog.corruptionTypePerFloorLog;
    }

    @Override
    public void onLoad(ArrayList<String> corruptionTypePerFloorLog) {
        CorruptionTypePerFloorLog.corruptionTypePerFloorLog = corruptionTypePerFloorLog != null ? corruptionTypePerFloorLog : new ArrayList<>();
    }
}