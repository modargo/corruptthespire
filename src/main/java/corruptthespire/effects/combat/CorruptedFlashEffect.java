
package corruptthespire.effects.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.CorruptedFormPower;
import corruptthespire.util.TextureLoader;

public class CorruptedFlashEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private Texture img;
    private boolean playedSound = false;
    private static Texture IMG = TextureLoader.getTexture(CorruptTheSpire.powerImage84(CorruptedFormPower.POWER_ID));

    public CorruptedFlashEffect(float x, float y) {
        this.img = IMG;
        this.x = x - (float)this.img.getWidth() / 2.0F;
        this.y = y - (float)this.img.getHeight() / 2.0F;
        this.startingDuration = 0.5F;
        this.duration = this.startingDuration;
        this.color = Color.WHITE.cpy();
    }

    public void update() {
        if (!this.playedSound) {
            CardCrawlGame.sound.playA("BLOCK_ATTACK", -0.5F);
            this.playedSound = true;
        }

        super.update();
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale * MathUtils.random(2.9F, 3.1F), this.scale * MathUtils.random(2.9F, 3.1F), this.rotation, this.img.getWidth() / 2, this.img.getHeight() / 2, this.img.getWidth(), this.img.getHeight(), false, false);
        sb.draw(this.img, this.x, this.y, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale * MathUtils.random(2.9F, 3.1F), this.scale * MathUtils.random(2.9F, 3.1F), this.rotation, this.img.getWidth() / 2, this.img.getHeight() / 2, this.img.getWidth(), this.img.getHeight(), false, false);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
