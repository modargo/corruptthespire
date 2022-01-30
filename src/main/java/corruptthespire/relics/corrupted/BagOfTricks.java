package corruptthespire.relics.corrupted;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.AfterImagePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class BagOfTricks extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:BagOfTricks";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int AFTERIMAGE = 1;
    private static final int DRAW_REDUCTION = 1;

    public BagOfTricks() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:after\u00a0image")), BaseMod.getKeywordDescription("corruptthespire:after\u00a0image")));
    }

    @Override
    public void atBattleStartPreDraw() {
        AbstractDungeon.player.gameHandSize -= DRAW_REDUCTION;
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AfterImagePower(AbstractDungeon.player, AFTERIMAGE), AFTERIMAGE));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.player.gameHandSize += DRAW_REDUCTION;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], AFTERIMAGE, DRAW_REDUCTION);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BagOfTricks();
    }
}
