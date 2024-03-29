package corruptthespire.effects.room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TextOverlayEffect extends AbstractGameEffect {
    private final String name;
    private final String levelNum;
    private static final float DUR = 5.0F;
    private final Color c1;
    private final Color c2;
    private boolean higher;

    public TextOverlayEffect(String name, String levelNum, boolean higher, Color c1, Color c2) {
        this.c1 = c1.cpy();
        this.c2 = c2.cpy();
        this.higher = false;
        this.name = name;
        this.levelNum = levelNum;
        this.duration = DUR;
        this.startingDuration = DUR;
        this.color = Settings.GOLD_COLOR.cpy();
        this.color.a = 0.0F;
        this.higher = higher;
    }

    public TextOverlayEffect(String name, String levelNum) {
        this(name, levelNum, false, Settings.CREAM_COLOR, Settings.GOLD_COLOR);
    }

    public void update() {
        float alpha;
        if (this.duration > 4.0F) {
            alpha = Interpolation.pow5Out.apply(1.0F, 0.0F, (this.duration - 4.0F) / 4.0F);
        } else {
            alpha = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 2.5F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.c1.a = alpha;
        this.c2.a = alpha;
    }

    public void render(SpriteBatch sb) {
        if (this.higher) {
            FontHelper.renderFontCentered(sb, FontHelper.SCP_cardDescFont, this.levelNum, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 290.0F * Settings.scale, this.c2, 1.0F);
            FontHelper.renderFontCentered(sb, FontHelper.SCP_cardDescFont, this.name, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 190.0F * Settings.scale, this.c1);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.SCP_cardDescFont, this.levelNum, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 90.0F * Settings.scale, this.c2, 1.0F);
            FontHelper.renderFontCentered(sb, FontHelper.SCP_cardDescFont, this.name, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F - 10.0F * Settings.scale, this.c1);
        }

    }

    public void dispose() {
    }
}
