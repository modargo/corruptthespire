package corruptthespire.effects.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class BlazeFromBeyondEffect extends AbstractGameEffect {
    private final int blazeCount;
    private final boolean flipped;

    public BlazeFromBeyondEffect(int blazeCount, boolean flipped) {
        this.blazeCount = 5 + Math.min(blazeCount, 45);
        this.flipped = flipped;
    }

    public void update() {
        CardCrawlGame.sound.playA("ORB_DARK_EVOKE", -0.25F - (float)this.blazeCount / 200.0F);
        CardCrawlGame.screenShake.shake(ShakeIntensity.HIGH, ShakeDur.MED, true);
        AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.FIREBRICK));

        for(int i = 0; i < this.blazeCount; ++i) {
            AbstractDungeon.effectsQueue.add(new FallingBlazeEffect(this.blazeCount, this.flipped));
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
