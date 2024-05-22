package corruptthespire.buttons;

import basemod.TopPanelItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.CorruptionFlags;
import corruptthespire.patches.core.SpecialActThreeBossFightPatch;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class CorruptionDisplay extends TopPanelItem {
    private static final float tipYpos = Settings.HEIGHT - (120.0f * Settings.scale);
    private static final float FLASH_ANIM_TIME = 2.0F;

    private static final Texture IMAGE = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:Corruption"));
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:Corruption").TEXT;
    public static final String ID = "CorruptTheSpire:CorruptionItem";

    public float flashTimer;

    public CorruptionDisplay() {
        super(IMAGE, ID);
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void update() {
        updateFlash();
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!Cor.active) {
            return;
        }
        super.render(sb);
        renderFlash(sb);
        renderCount(sb);
    }

    @Override
    protected void onClick() {
        CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
    }

    public void flash() {
        this.flashTimer = FLASH_ANIM_TIME;
    }

    private void updateFlash() {
        if(flashTimer != 0.0f) {
            flashTimer -= Gdx.graphics.getDeltaTime();
        }
    }

    public void renderCount(SpriteBatch sb) {
        FontHelper.cardTitleFont.getData().setScale(1.0f);
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, Integer.toString(Cor.corruption), this.x + (this.hb_w * 70) / 100, this.y + 16f * Settings.scale, Color.WHITE.cpy());

        if (this.getHitbox().hovered) {
            float xPos = this.x - this.hb_w;
            TipHelper.renderGenericTip(xPos, tipYpos, TEXT[0], this.getTooltip());
        }
    }

    public void renderFlash(SpriteBatch sb) {
        float tmp = Interpolation.exp10In.apply(0.0F, 4.0F, flashTimer / FLASH_ANIM_TIME);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, flashTimer * FLASH_ANIM_TIME));

        float halfWidth = (float)this.image.getWidth() / 2.0F;
        float halfHeight = (float)this.image.getHeight() / 2.0F;
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float)this.image.getWidth(), (float)this.image.getHeight(), Settings.scale+tmp, Settings.scale+tmp, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float)this.image.getWidth(), (float)this.image.getHeight(), Settings.scale+tmp* 0.66F, Settings.scale+tmp* 0.66F, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);
        sb.draw(this.image, this.x - halfWidth + halfHeight * Settings.scale, this.y - halfHeight + halfHeight * Settings.scale, halfWidth, halfHeight, (float)this.image.getWidth(), (float)this.image.getHeight(), Settings.scale+tmp/ 3.0F, Settings.scale+tmp/ 3.0F, this.angle, 0, 0, this.image.getWidth(), this.image.getHeight(), false, false);

        sb.setBlendFunction(770, 771);
    }

    private String getTooltip() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(TEXT[1], Cor.getCorruptionDamageMultiplierPercent()));
        boolean replaceAct2Boss = Cor.flags.warAndFear == CorruptionFlags.WarAndFear.REPLACE_BOSS;
        boolean extraAct2Boss = Cor.flags.warAndFear == CorruptionFlags.WarAndFear.EXTRA_BOSS;
        int extraAct3BossCount = SpecialActThreeBossFightPatch.getExtraBossLeftCount();
        if (replaceAct2Boss || extraAct2Boss || extraAct3BossCount > 0) {
            sb.append(" NL ");
        }
        if (replaceAct2Boss) {
            sb.append(" NL ");
            sb.append(TEXT[2]);
        }
        else if (extraAct2Boss) {
            sb.append(" NL ");
            sb.append(TEXT[3]);
        }
        if (extraAct3BossCount == 1) {
            sb.append(" NL ");
            sb.append(TEXT[4]);
        }
        else if (extraAct3BossCount > 1) {
            sb.append(" NL ");
            sb.append(TEXT[5].replace("{0}", extraAct3BossCount + ""));
        }

        return sb.toString();
    }
}