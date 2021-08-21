package corruptthespire.patches.treasure;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;
import corruptthespire.corruptions.treasure.VaultChest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

public class CombatRewardScreenWhileInVaultPatch {
    public static final Logger logger = LogManager.getLogger(CombatRewardScreenWhileInVaultPatch.class.getName());

    @SpirePatch(clz = CombatRewardScreen.class, method = "setLabel")
    public static class SetLabelPatch {
        @SpirePrefixPatch
        public static SpireReturn FixButtons(CombatRewardScreen __instance) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if (room instanceof TreasureRoom
                && TreasureCorruptionTypeField.corruptionType.get(room) == TreasureCorruptionType.Vault) {
                VaultChest.FixButtons();
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "reopen")
    public static class ReopenPatch {
        @SpirePostfixPatch
        public static void FixButtons(CombatRewardScreen __instance) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if (room instanceof TreasureRoom
                && TreasureCorruptionTypeField.corruptionType.get(room) == TreasureCorruptionType.Vault) {
                VaultChest.FixButtons();
            }
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "rewardViewUpdate")
    public static class RewardViewUpdatePatch {
        @SpirePostfixPatch
        public static void UpdateCurrentRoomRewards(CombatRewardScreen __instance) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if (room instanceof TreasureRoom
                && TreasureCorruptionTypeField.corruptionType.get(room) == TreasureCorruptionType.Vault
                && AbstractDungeon.getCurrRoom().rewards != __instance.rewards) {
                logger.info("CombatRewardScreen RewardItems: " + __instance.rewards.stream().map(r -> r.type.name()).collect(Collectors.joining(",")));
                logger.info("AbstractDungeon.CurrentRoom RewardItems: " + AbstractDungeon.getCurrRoom().rewards.stream().map(r -> r.type.name()).collect(Collectors.joining(",")));
                //We synchronize these so that the combat reward screen can be reopened without entirely resetting it
                AbstractDungeon.getCurrRoom().rewards = __instance.rewards;
            }
        }
    }
}