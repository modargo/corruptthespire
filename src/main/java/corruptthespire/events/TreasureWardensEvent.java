package corruptthespire.events;

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
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.vfx.ChestShineEffect;
import com.megacrit.cardcrawl.vfx.scene.SpookyChestEffect;
import corruptthespire.monsters.Encounters;

// We extend the MaskedBandits event because ProceedButton.java specifically checks if an event is an instance of this type
// (or a few other types) in the logic for what happens when you click proceed. This is easier than a patch.
public class TreasureWardensEvent extends MaskedBandits {
    public static final String ID = "CorruptTheSpire:TreasureWardens";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screen = 0;
    private final Texture chestImg;
    private float shinyTimer = 0.0f;

    public TreasureWardensEvent() {
        this.roomEventText.clear();
        this.body = DESCRIPTIONS[0];
        this.roomEventText.addDialogOption(OPTIONS[0]);
        this.roomEventText.addDialogOption(OPTIONS[1]);
        this.chestImg = ImageMaster.S_CHEST;
        this.hasDialog = true;
        this.hasFocus = true;
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(Encounters.TREASURE_WARDENS);
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
        switch(this.screen) {
            case 0:
                switch(buttonPressed) {
                    case 0:
                        logMetric(ID, "Fight");
                        if (Settings.isDailyRun) {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(30));
                        } else {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(25, 35));
                        }

                        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());

                        this.screen = 2;
                        this.enterCombat();
                        AbstractDungeon.lastCombatMetricKey = Encounters.TREASURE_WARDENS;
                        break;
                    case 1:
                        logMetricIgnored(ID);
                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.roomEventText.updateDialogOption(0, OPTIONS[1]);
                        this.roomEventText.clearRemainingOptions();
                        this.screen = 1;
                        break;
                }
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
        sb.draw(this.chestImg, AbstractChest.CHEST_LOC_X - 256.0f, AbstractChest.CHEST_LOC_Y - 256.0f + AbstractDungeon.sceneOffsetY, 256.0f, 256.0f, 512.0f, 512.0f, Settings.scale, Settings.scale, 0.f, 0, 0, 512, 512, false, false);

        if (this.screen == 0) {
            this.updateShiny();
        }
    }

    private void updateShiny() {
        this.shinyTimer -= Gdx.graphics.getDeltaTime();
        if (this.shinyTimer < 0.0f && !Settings.DISABLE_EFFECTS) {
            this.shinyTimer = 0.2f;
            AbstractDungeon.topLevelEffects.add(new ChestShineEffect());
            AbstractDungeon.effectList.add(new SpookyChestEffect());
            AbstractDungeon.effectList.add(new SpookyChestEffect());
        }
    }
}