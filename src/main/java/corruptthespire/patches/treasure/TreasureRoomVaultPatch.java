package corruptthespire.patches.treasure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.vfx.ChestShineEffect;
import com.megacrit.cardcrawl.vfx.scene.SpookyChestEffect;
import corruptthespire.corruptions.treasure.TreasureCorruption;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;
import corruptthespire.corruptions.treasure.VaultChest;
import corruptthespire.effects.VaultChestShineEffect;
import corruptthespire.effects.VaultSpookyChestEffect;

import java.util.List;

public class TreasureRoomVaultPatch {
    @SpirePatch(clz = TreasureRoom.class, method = "onPlayerEntry")
    public static class VaultOnPlayerEntryPatch {
        @SpirePostfixPatch
        public static void setUpVault(TreasureRoom __instance) {
            if (TreasureCorruptionTypeField.corruptionType.get(__instance) == TreasureCorruptionType.Vault) {
                __instance.chest = null;
                TreasureCorruption.setUpVault(__instance);
            }
        }
    }

    @SpirePatch(clz = TreasureRoom.class, method = "update")
    public static class VaultUpdatePatch {
        @SpirePostfixPatch
        public static void updateVaultChests(TreasureRoom __instance) {
            List<VaultChest> vaultChests = VaultChestsField.vaultChests.get(__instance);
            if (vaultChests != null) {
                for (VaultChest vaultChest : vaultChests) {
                    vaultChest.update();
                }
            }
        }
    }

    @SpirePatch(clz = TreasureRoom.class, method = "updateShiny")
    public static class VaultUpdateShinyPatch {
        @SpirePrefixPatch
        public static SpireReturn updateVaultChests(TreasureRoom __instance) {
            List<VaultChest> vaultChests = VaultChestsField.vaultChests.get(__instance);
            if (vaultChests != null) {
                int numOpenedChests = (int)vaultChests.stream().filter(c -> c.isOpen).count();
                boolean corrupted = numOpenedChests >= TreasureCorruption.VAULT_CHESTS_BEFORE_CORRUPTION;
                for (VaultChest vaultChest : vaultChests) {
                    if (!vaultChest.isOpen) {
                        vaultChest.shinyTimer -= Gdx.graphics.getDeltaTime();
                        if (vaultChest.shinyTimer < 0.0F && !Settings.DISABLE_EFFECTS) {
                            vaultChest.shinyTimer = 0.2F;
                            AbstractDungeon.topLevelEffects.add(new VaultChestShineEffect(vaultChest.x, vaultChest.y, corrupted));
                            AbstractDungeon.effectList.add(new VaultSpookyChestEffect(vaultChest.x, vaultChest.y, corrupted));
                            AbstractDungeon.effectList.add(new VaultSpookyChestEffect(vaultChest.x, vaultChest.y, corrupted));
                        }
                    }
                }
            }

            if (__instance.chest == null) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = TreasureRoom.class, method = "render")
    public static class VaultRenderPatch {
        @SpirePrefixPatch
        public static void renderVaultChests(TreasureRoom __instance, SpriteBatch sb) {
            List<VaultChest> vaultChests = VaultChestsField.vaultChests.get(__instance);
            if (vaultChests != null) {
                for (VaultChest vaultChest : vaultChests) {
                    vaultChest.render(sb);
                }
            }
        }
    }
}
