package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class TranscendentEye extends CustomRelic {
    public static final String ID = "CorruptTheSpire:TranscendentEye";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));

    private static final int SCRY = 2;
    private static final int DRAW = 1;

    public TranscendentEye() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter == 1) {
            this.flash();
            this.addToBot(new ScryAction(SCRY));
            this.addToBot(new DrawCardAction(DRAW));
            this.counter = -1;
            this.grayscale = true;
        }
        else if (!this.grayscale) {
            this.counter++;
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], SCRY, DRAW);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TranscendentEye();
    }
}
