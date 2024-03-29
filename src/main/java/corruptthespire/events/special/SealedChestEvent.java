package corruptthespire.events.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.MaskedBandits;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.room.VaultChestShineEffect;
import corruptthespire.effects.room.VaultSpookyChestEffect;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.util.TextureLoader;

// We extend the MaskedBandits event because ProceedButton.java specifically checks if an event is an instance of this type
// (or a few other types) in the logic for what happens when you click proceed. This is easier than a patch.
public class SealedChestEvent extends MaskedBandits {
    public static final String ID = "CorruptTheSpire:SealedChest";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final int GOLD = 100;

    private int screen = 0;
    private Texture chestImg;
    private final Texture sealImg;
    private float shinyTimer = 0.0f;

    public SealedChestEvent() {
        this.noCardsInRewards = true;
        this.roomEventText.clear();
        this.body = DESCRIPTIONS[0];
        this.roomEventText.addDialogOption(OPTIONS[0]);
        this.roomEventText.addDialogOption(OPTIONS[2]);
        this.chestImg = ImageMaster.L_CHEST;
        this.sealImg = TextureLoader.getTexture(CorruptTheSpire.uiImage("Ofuda"));
        this.hasDialog = true;
        this.hasFocus = true;
        AbstractDungeon.getCurrRoom().monsters = null;
    }

    @Override
    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        //This is here so that it has to happen, but only happens after the room has fully loaded
        //This is because if we set this when entering the room, it will be immediately saved, and saving and reloading
        //won't work correctly, because it will reroll the corruption (and filter this out as an option)
        Cor.flags.seenSealedChest = true;
        switch(this.screen) {
            case 0:
                switch(buttonPressed) {
                    case 0:
                        logMetric(ID, "Open");
                        Cor.flags.openedSealedChest = true;
                        this.chestImg = ImageMaster.L_CHEST_OPEN;
                        CardCrawlGame.sound.playV("ATTACK_PIERCING_WAIL", 1.5F);
                        AbstractDungeon.topLevelEffects.add(new BorderLongFlashEffect(Color.PURPLE.cpy()));
                        AbstractDungeon.topLevelEffects.add(new ShockWaveEffect(AbstractChest.CHEST_LOC_X, AbstractChest.CHEST_LOC_Y, Color.NAVY, ShockWaveEffect.ShockWaveType.CHAOTIC));
                        AbstractDungeon.topLevelEffects.add(new ShockWaveEffect(AbstractChest.CHEST_LOC_X, AbstractChest.CHEST_LOC_Y, Color.BLACK, ShockWaveEffect.ShockWaveType.CHAOTIC));
                        AbstractDungeon.topLevelEffects.add(new ShockWaveEffect(AbstractChest.CHEST_LOC_X, AbstractChest.CHEST_LOC_Y, Color.FIREBRICK, ShockWaveEffect.ShockWaveType.CHAOTIC));

                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.roomEventText.updateDialogOption(0, OPTIONS[1]);
                        this.roomEventText.clearRemainingOptions();
                        this.screen = 1;
                        break;
                    case 1:
                        logMetricIgnored(ID);
                        this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.roomEventText.updateDialogOption(0, OPTIONS[2]);
                        this.roomEventText.clearRemainingOptions();
                        this.screen = 2;
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        break;
                }
                break;
            case 1:
                AbstractDungeon.getCurrRoom().rewards.clear();

                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onChestOpen(false);
                }

                AbstractDungeon.getCurrRoom().addGoldToRewards(GOLD);
                AbstractDungeon.getCurrRoom().addRelicToRewards(new FragmentOfCorruption());
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
                if (Settings.isFinalActAvailable && !Settings.hasSapphireKey) {
                    AbstractDungeon.getCurrRoom().addSapphireKey(AbstractDungeon.getCurrRoom().rewards.get(AbstractDungeon.getCurrRoom().rewards.size() - 1));
                }

                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onChestOpenAfter(false);
                }

                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                AbstractDungeon.combatRewardScreen.open();

                this.screen = 3;
                this.roomEventText.clear();
                break;
            default:
                this.openMap();
                break;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.setColor(Color.WHITE);
        sb.draw(this.chestImg, AbstractChest.CHEST_LOC_X - 256.0f, AbstractChest.CHEST_LOC_Y - 256.0f + AbstractDungeon.sceneOffsetY, 256.0f, 256.0f, 512.0f, 512.0f, Settings.scale, Settings.scale, 0.f, 0, 0, 512, 512, false, false);//

        int n = 5;
        for (int i = 0; i < n; i++) {
            //AbstractChest.CHEST_LOC_X: The center of the chest
            //32.0F: Half the width of the Ofuda image
            //66.0F: The spacing between each Ofuda image (both vertically and horizontally)
            float baseX = AbstractChest.CHEST_LOC_X - 32.0F;
            float xGap = ((n - (i + 1)) * 66.0F);
            float x1 = baseX - xGap;
            float x2 = baseX + xGap;
            float y1 = AbstractDungeon.floorY + 66.0F * i;
            float y2 = AbstractDungeon.floorY + 66.0F * (n - 1);
            sb.draw(this.sealImg, x1, y1);
            if (y1 != y2) {
                sb.draw(this.sealImg, x1, y2);
            }
            if (x1 != x2) {
                sb.draw(this.sealImg, x2, y1);
                if (y1 != y2) {
                    sb.draw(this.sealImg, x2, y2);
                }
            }
        }

        if (this.screen == 0 || this.screen == 2) {
            this.updateShiny();
        }
    }

    private void updateShiny() {
        this.shinyTimer -= Gdx.graphics.getDeltaTime();
        if (this.shinyTimer < 0.0f && !Settings.DISABLE_EFFECTS) {
            this.shinyTimer = 0.2f;
            AbstractDungeon.topLevelEffects.add(new VaultChestShineEffect(AbstractChest.CHEST_LOC_X, AbstractChest.CHEST_LOC_Y, true));
            AbstractDungeon.effectList.add(new VaultSpookyChestEffect(AbstractChest.CHEST_LOC_X, AbstractChest.CHEST_LOC_Y, true));
            AbstractDungeon.effectList.add(new VaultSpookyChestEffect(AbstractChest.CHEST_LOC_X, AbstractChest.CHEST_LOC_Y, true));
        }
    }
}