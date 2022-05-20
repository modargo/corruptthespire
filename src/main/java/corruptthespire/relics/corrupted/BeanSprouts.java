package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class BeanSprouts extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:BeanSprouts";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    public static final int STRENGTH = 1;
    public static final int BLOCK = 3;

    public BeanSprouts() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], STRENGTH, BLOCK);
    }

    public void trigger() {
        this.flash();
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NextTurnBlockPower(AbstractDungeon.player, BLOCK)));
        }
        else {
            this.addToTop(new GainBlockAction(AbstractDungeon.player, BLOCK));
        }
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1)));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BeanSprouts();
    }
}
