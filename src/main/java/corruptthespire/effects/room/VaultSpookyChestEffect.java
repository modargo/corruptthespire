package corruptthespire.effects.room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class VaultSpookyChestEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private final float vY;
    private final float aV;
    private final boolean flipX = MathUtils.randomBoolean();
    private final boolean flipY = MathUtils.randomBoolean();
    private final TextureAtlas.AtlasRegion img;

    public VaultSpookyChestEffect(float x, float y, boolean corrupted) {
        this.duration = MathUtils.random(4.0F, 7.0F);
        this.startingDuration = this.duration;
        switch(MathUtils.random(2)) {
            case 0:
                this.img = ImageMaster.SMOKE_1;
                break;
            case 1:
                this.img = ImageMaster.SMOKE_2;
                break;
            default:
                this.img = ImageMaster.SMOKE_3;
        }

        this.x = x - (float)this.img.packedWidth / 2.0F;
        this.y = y - (float)this.img.packedWidth / 2.0F - 150.0F * Settings.scale;
        this.vX = MathUtils.random(-100.0F, 100.0F) * Settings.scale;
        this.vY = MathUtils.random(-30.0F, 30.0F) * Settings.scale;
        this.aV = MathUtils.random(-100.0F, 100.0F);
        float tmp = MathUtils.random(0.4F, 0.9F);
        this.color = new Color();
        this.color.r = tmp * 0.75F;
        this.color.g = tmp;
        this.color.b = tmp;
        if (corrupted) {
            this.color = MathUtils.randomBoolean() ? Color.PURPLE.cpy() : Color.FIREBRICK.cpy();
        }
        this.renderBehind = true;
        this.scale = MathUtils.random(0.8F, 1.2F) * Settings.scale;
    }

    public void update() {
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.vX *= 0.99F;
        this.rotation += this.aV * Gdx.graphics.getDeltaTime();
        if (this.startingDuration - this.duration < 1.5F) {
            this.color.a = Interpolation.fade.apply(0.0F, 0.4F, (this.startingDuration - this.duration) / 1.5F);
        } else if (this.duration < 4.0F) {
            this.color.a = Interpolation.fade.apply(0.4F, 0.0F, 1.0F - this.duration / 4.0F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.scale += Gdx.graphics.getDeltaTime() / 3.0F;
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        if (this.flipX && !this.img.isFlipX()) {
            this.img.flip(true, false);
        } else if (!this.flipX && this.img.isFlipX()) {
            this.img.flip(true, false);
        }

        if (this.flipY && !this.img.isFlipY()) {
            this.img.flip(false, true);
        } else if (!this.flipY && this.img.isFlipY()) {
            this.img.flip(false, true);
        }

        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
    }

    public void dispose() {
    }
}