package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class RustedOrichalcum extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:RustedOrichalcum";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int TURNS = 3;
    private static final int BLOCK = 12;
    private static final int CONSTRICTED = 4;

    public RustedOrichalcum() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConstrictedPower(AbstractDungeon.player, AbstractDungeon.player, CONSTRICTED), CONSTRICTED));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.counter = 0;
        this.grayscale = false;
    }

    @Override
    public void atTurnStart() {
        if (!this.grayscale) {
            this.counter++;
        }

        if (!this.grayscale && this.counter <= TURNS) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK));
        }

        if (this.counter == TURNS) {
            this.counter = -1;
            this.grayscale = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], TURNS, BLOCK, CONSTRICTED);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RustedOrichalcum();
    }
}
