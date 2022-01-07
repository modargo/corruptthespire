package corruptthespire.corruptions.treasure;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Cauldron;
import com.megacrit.cardcrawl.relics.Orrery;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.Cor;
import corruptthespire.cards.CardUtil;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.effects.room.TextOverlayEffect;
import corruptthespire.patches.treasure.VaultChestsField;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.rewards.MaxHealthReward;
import corruptthespire.rewards.RandomUpgradeReward;

import java.util.List;

public class VaultChest extends AbstractChest {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:VaultChest").TEXT;
    private static final int MAX_HEALTH = 4;
    private final VaultChestType vaultChestType;
    public final float x;
    public final float y;
    //This might be better as a field on TreasureRoom, but easier to just put here
    public float shinyTimer;

    public VaultChest(VaultChestType vaultChestType, float x, float y) {
        this.vaultChestType = vaultChestType;
        this.x = x;
        this.y = y;
        this.shinyTimer = 0.0f;
        this.img = ImageMaster.S_CHEST;
        this.openedImg = ImageMaster.S_CHEST_OPEN;
        this.hb = new Hitbox(256.0F * Settings.scale, 200.0F * Settings.scale);
        this.hb.move(x, y - 150.0F * Settings.scale);
    }

    @Override
    public void open(boolean bossChest) {
        AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[0]);

        if (this.vaultChestType == VaultChestType.RelicAndSapphireKey) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onChestOpen(bossChest);
            }
        }

        CardCrawlGame.sound.play("CHEST_OPEN");

        if (this.vaultChestType == VaultChestType.RelicAndSapphireKey && this.cursed) {
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), this.hb.cX, this.hb.cY));
        }

        String text = this.addRewards();

        if (this.vaultChestType == VaultChestType.RelicAndSapphireKey) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onChestOpenAfter(bossChest);
            }
        }

        //Maybe make a version of LevelTransitionTextOverlayEffect to use here?
        //Could also have that display something at the start of the room... though that would be unusual
        String overlayText = null;
        String overlayTextTitle = null;
        List<VaultChest> vaultChests = VaultChestsField.vaultChests.get(AbstractDungeon.getCurrRoom());
        int numOpenedChests = (int)vaultChests.stream().filter(c -> c.isOpen).count();
        if (numOpenedChests == TreasureCorruption.VAULT_CHESTS_BEFORE_CORRUPTION) {
            overlayText = TreasureCorruption.TEXT[3];
            overlayTextTitle = TreasureCorruption.TEXT[5];
        }
        else if (numOpenedChests > TreasureCorruption.VAULT_CHESTS_BEFORE_CORRUPTION) {
            overlayText = TreasureCorruption.TEXT[4];
            overlayTextTitle = TreasureCorruption.TEXT[6];
            Cor.addCorruption(TreasureCorruption.VaultChestCorruption.get(numOpenedChests - TreasureCorruption.VAULT_CHESTS_BEFORE_CORRUPTION - 1));
        }
        AbstractDungeon.combatRewardScreen.open(text);
        FixButtons();
        if (overlayText != null) {
            AbstractDungeon.topLevelEffects.add(new TextOverlayEffect(overlayText, overlayTextTitle, false, Settings.CREAM_COLOR, Settings.PURPLE_COLOR));
        }
    }

    public static void FixButtons() {
        List<VaultChest> vaultChests = VaultChestsField.vaultChests.get(AbstractDungeon.getCurrRoom());
        int numUnopenedChests = (int)vaultChests.stream().filter(c -> !c.isOpen).count();
        if (numUnopenedChests > 0) {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(TreasureCorruption.TEXT[2]);
        }
        else {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(AbstractChest.TEXT[0]);
        }
        AbstractDungeon.overlayMenu.cancelButton.hide();
        AbstractDungeon.overlayMenu.proceedButton.show();
    }

    private String addRewards() {
        String text;
        switch(this.vaultChestType) {
            case RelicAndSapphireKey:
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
                if (Settings.isFinalActAvailable && !Settings.hasSapphireKey) {
                    AbstractDungeon.getCurrRoom().addSapphireKey(AbstractDungeon.getCurrRoom().rewards.get(AbstractDungeon.getCurrRoom().rewards.size() - 1));
                }
                text = TEXT[0];
                break;
            case Empty:
                text = TEXT[1];
                break;
            case Gold:
                int baseGold = 100;
                int gold = Math.round(AbstractDungeon.treasureRng.random(baseGold * 0.7F, baseGold * 1.3F));
                AbstractDungeon.getCurrRoom().addGoldToRewards(gold);
                text = TEXT[2];
                break;
            case Fragment:
                AbstractDungeon.getCurrRoom().addRelicToRewards(new FragmentOfCorruption());
                text = TEXT[3];
                break;
            case Card:
                AbstractDungeon.getCurrRoom().addCardToRewards();
                text = TEXT[4];
                break;
            case MaxHealth:
                AbstractDungeon.getCurrRoom().rewards.add(new MaxHealthReward(MAX_HEALTH));
                text = TEXT[5];
                break;
            case ShopRelic:
                String shopRelicKey = AbstractDungeon.returnRandomRelicKey(AbstractRelic.RelicTier.SHOP);
                //Orrery and Cauldron cause crashes when picked in the treasure vault rewards screen
                while (shopRelicKey.equals(Orrery.ID) || shopRelicKey.equals(Cauldron.ID)) {
                    shopRelicKey = AbstractDungeon.returnRandomRelicKey(AbstractRelic.RelicTier.SHOP);
                }
                AbstractDungeon.getCurrRoom().addRelicToRewards(RelicLibrary.getRelic(shopRelicKey));
                text = TEXT[6];
                break;
            case ColorlessCard:
                RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
                AbstractDungeon.getCurrRoom().addCardReward(reward);
                text = TEXT[7];
                break;
            case Upgrade:
                AbstractDungeon.getCurrRoom().rewards.add(new RandomUpgradeReward());
                text = TEXT[8];
                break;
            case CorruptedCard:
                RewardItem corruptedCardReward = new RewardItem(null, RewardItem.RewardType.CARD);
                corruptedCardReward.cards = CorruptedCardUtil.getRandomCorruptedCards(CardUtil.getNumCardsForRewards(), null);
                AbstractDungeon.getCurrRoom().rewards.add(corruptedCardReward);
                text = TEXT[9];
                break;
            default:
                throw new RuntimeException("Unknown vault chest type: " + this.vaultChestType);
        }

        return text;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        float angle = 0.0F;
        if (this.isOpen && this.openedImg == null) {
            angle = 180.0F;
        }

        if (this.isOpen && angle != 180.0F) {
            sb.draw(this.openedImg, this.x - 256.0F, this.y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, angle, 0, 0, 512, 512, false, false);
        } else {
            sb.draw(this.img, this.x - 256.0F, this.y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, angle, 0, 0, 512, 512, false, false);
            if (this.hb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
                sb.draw(this.img, this.x - 256.0F, this.y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, angle, 0, 0, 512, 512, false, false);
                sb.setBlendFunction(770, 771);
            }
        }

        if (Settings.isControllerMode && !this.isOpen) {
            sb.setColor(Color.WHITE);
            sb.draw(CInputActionSet.select.getKeyImg(), this.x - 32.0F - 150.0F * Settings.scale, this.y - 32.0F - 210.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        this.hb.render(sb);
    }
}
