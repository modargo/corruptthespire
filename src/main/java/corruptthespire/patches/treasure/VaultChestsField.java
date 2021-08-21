package corruptthespire.patches.treasure;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import corruptthespire.corruptions.treasure.VaultChest;

import java.util.List;

@SpirePatch(
        clz = TreasureRoom.class,
        method = SpirePatch.CLASS
)
public class VaultChestsField {
    public static SpireField<List<VaultChest>> vaultChests = new SpireField<>(() -> null);
}
