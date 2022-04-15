package corruptthespire.relics.elite;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class DoomSight extends CustomRelic {
    public static final String ID = "CorruptTheSpire:DoomSight";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));

    private static final int WEAK = 2;
    private static final int DRAW = 1;

    public DoomSight() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter == 2) {
            this.flash();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, WEAK, false)));
            }
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
        return MessageFormat.format(DESCRIPTIONS[0], DRAW, WEAK);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DoomSight();
    }
}
