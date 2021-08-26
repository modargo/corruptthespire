package corruptthespire.patches.campfire;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import corruptthespire.corruptions.campfire.CampfireInfo;

@SpirePatch(
        clz = CampfireUI.class,
        method = SpirePatch.CLASS
)
public class CampfireInfoField {
    public static final SpireField<CampfireInfo> campfireInfo = new SpireField<>(CampfireInfo::new);
}
