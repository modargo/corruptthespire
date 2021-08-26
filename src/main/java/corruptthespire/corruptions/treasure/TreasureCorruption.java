package corruptthespire.corruptions.treasure;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import corruptthespire.Cor;
import corruptthespire.patches.CorruptedField;
import corruptthespire.patches.treasure.TreasureCorruptionTypeField;
import corruptthespire.patches.treasure.VaultChestsField;
import corruptthespire.relics.FragmentOfCorruption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TreasureCorruption {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:TreasureCorruption").TEXT;
    private static final int BASE_VAULT_CHESTS = 6;
    private static final int POTENTIAL_EXTRA_VAULT_CHESTS = 2;
    public static final int VAULT_CHESTS_BEFORE_CORRUPTION = 3;
    public static final List<Integer> VaultChestCorruption = Arrays.asList(1, 2, 3, 4, 5);

    public static boolean handleRelic(AbstractRelic.RelicTier tier) {
        TreasureCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
            ? TreasureCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
            : null;

        if (corruptionType == TreasureCorruptionType.Money) {
            int baseGold = tier == AbstractRelic.RelicTier.COMMON ? 200
                    : tier == AbstractRelic.RelicTier.UNCOMMON ? 275
                    : 325;
            int gold = Math.round(AbstractDungeon.treasureRng.random(baseGold * 0.9F, baseGold * 1.1F));
            RewardItem reward = new RewardItem(gold);
            reward.text = reward.goldAmt + " " + TEXT[0];
            AbstractDungeon.getCurrRoom().rewards.add(reward);
            return true;
        }

        return false;
    }

    public static void handleExtraRewards(boolean bossChest) {
        TreasureCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
                ? TreasureCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType == TreasureCorruptionType.Fragment) {
            AbstractDungeon.getCurrRoom().addRelicToRewards(new FragmentOfCorruption());
        }

        if (corruptionType == TreasureCorruptionType.Extra) {
            //Same distribution as Matryoshka
            if (AbstractDungeon.relicRng.randomBoolean(0.75F)) {
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
            } else {
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
            }
        }

        if (corruptionType == TreasureCorruptionType.CorruptedRelic) {
            AbstractDungeon.getCurrRoom().addRelicToRewards(RelicLibrary.getRelic(Cor.returnRandomCorruptedRelicKey()).makeCopy());
        }
    }

    public static void setUpVault(TreasureRoom room) {
        int numChests = BASE_VAULT_CHESTS + Cor.rng.random(POTENTIAL_EXTRA_VAULT_CHESTS);
        List<VaultChestType> vaultChestTypes = new ArrayList<>();
        vaultChestTypes.add(VaultChestType.RelicAndSapphireKey);
        vaultChestTypes.add(VaultChestType.Empty);

        List<VaultChestType> potentialVaultChestTypes = new ArrayList<>();
        potentialVaultChestTypes.add(VaultChestType.Gold);
        potentialVaultChestTypes.add(VaultChestType.Fragment);
        potentialVaultChestTypes.add(VaultChestType.Card);
        potentialVaultChestTypes.add(VaultChestType.MaxHealth);
        potentialVaultChestTypes.add(VaultChestType.ShopRelic);
        potentialVaultChestTypes.add(VaultChestType.ColorlessCard);
        potentialVaultChestTypes.add(VaultChestType.Upgrade);
        potentialVaultChestTypes.add(VaultChestType.CorruptedCard);
        Collections.shuffle(potentialVaultChestTypes, Cor.rng.random);

        int chestTypesToAdd = numChests - vaultChestTypes.size();
        for (int i = 0; i < chestTypesToAdd; i++) {
            vaultChestTypes.add(potentialVaultChestTypes.get(i));
        }

        Random random = new Random(Settings.seed + AbstractDungeon.floorNum);
        List<Coordinate> potentialCoordinates = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                float x = Settings.WIDTH - (350.0F + 250.0F * i + random.random(200)) * Settings.scale;
                float y = AbstractDungeon.floorY + (100.0F + 250.0F * j + random.random(200)) * Settings.scale;
                potentialCoordinates.add(new Coordinate(x, y));
            }
        }
        Collections.shuffle(potentialCoordinates, random.random);

        List<VaultChest> vaultChests = new ArrayList<>();
        VaultChestsField.vaultChests.set(room, vaultChests);
        int i = 0;
        for (VaultChestType vaultChestType : vaultChestTypes) {
            Coordinate c = potentialCoordinates.get(i);
            vaultChests.add(new VaultChest(vaultChestType, c.x, c.y));
            i++;
        }

        AbstractDungeon.overlayMenu.proceedButton.setLabel(AbstractChest.TEXT[0]);
    }

    private static class Coordinate {
        public final float x;
        public final float y;
        public Coordinate(float x, float y) { this.x = x; this.y = y; }
    }
}
