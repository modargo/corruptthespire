package corruptthespire.effects.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

public class FallingBlazeEffect extends AbstractGameEffect {
    private static final Texture BLAZE_LEFT = TextureLoader.getTexture(CorruptTheSpire.effectImage("blazeLeft"));
    private static final Texture BLAZE_MID = TextureLoader.getTexture(CorruptTheSpire.effectImage("blazeMid"));
    private static final Texture BLAZE_RIGHT = TextureLoader.getTexture(CorruptTheSpire.effectImage("blazeRight"));
    private float waitTimer;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private final float floorY;
    private final Texture img;
    private final int blazeCount;

    public FallingBlazeEffect(int blazeCount, boolean flipped) {
        this.blazeCount = blazeCount;
        switch(MathUtils.random(2)) {
            case 0:
                this.img = BLAZE_RIGHT;
                break;
            case 1:
                this.img = BLAZE_LEFT;
                break;
            default:
                this.img = BLAZE_MID;
        }

        this.waitTimer = MathUtils.random(0.0F, 0.5F);
        if (flipped) {
            this.x = MathUtils.random(420.0F, 1420.0F) * Settings.scale - 48.0F;
            this.vX = MathUtils.random(-600.0F, -900.0F);
            this.vX += (float)blazeCount * 5.0F;
        } else {
            this.x = MathUtils.random(500.0F, 1500.0F) * Settings.scale - 48.0F;
            this.vX = MathUtils.random(600.0F, 900.0F);
            this.vX -= (float)blazeCount * 5.0F;
        }

        this.y = (float)Settings.HEIGHT + MathUtils.random(100.0F, 300.0F) - 48.0F;
        this.vY = MathUtils.random(2500.0F, 4000.0F);
        this.vY -= (float)blazeCount * 10.0F;
        this.vY *= Settings.scale;
        this.vX *= Settings.scale;
        this.duration = 2.0F;
        this.scale = MathUtils.random(1.0F, 1.5F);
        this.scale += (float)blazeCount * 0.04F;
        this.vX *= this.scale;
        this.scale *= Settings.scale;
        this.color = new Color(1.0F, 0.9F, 0.9F, MathUtils.random(0.9F, 1.0F));
        Vector2 derp = new Vector2(this.vX, this.vY);
        if (flipped) {
            this.rotation = derp.angle() + 225.0F - (float)blazeCount / 3.0F;
        } else {
            this.rotation = derp.angle() - 45.0F + (float)blazeCount / 3.0F;
        }

        this.renderBehind = MathUtils.randomBoolean();
        this.floorY = AbstractDungeon.floorY + MathUtils.random(-200.0F, 50.0F) * Settings.scale;
    }

    public void update() {
        this.waitTimer -= Gdx.graphics.getDeltaTime();
        if (this.waitTimer <= 0.0F) {
            this.x += this.vX * Gdx.graphics.getDeltaTime();
            this.y -= this.vY * Gdx.graphics.getDeltaTime();
            if (this.y < this.floorY) {
                float pitch = 0.8F;
                pitch -= (float)this.blazeCount * 0.025F;
                pitch += MathUtils.random(-0.2F, 0.2F);
                CardCrawlGame.sound.playA("ATTACK_FIRE", pitch);

                for(int i = 0; i < 4; ++i) {
                    AbstractDungeon.effectsQueue.add(new BlazeShatterEffect(this.x, this.y));
                }

                this.isDone = true;
            }

        }
    }

    public void render(SpriteBatch sb) {
        if (this.waitTimer < 0.0F) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.rotation, 0, 0, 96, 96, false, false);
            sb.setBlendFunction(770, 771);
        }

    }

    public void dispose() {
    }
}
