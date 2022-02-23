package corruptthespire.relics.elite;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ShimmeringFan extends CustomRelic {
    public static final String ID = "CorruptTheSpire:ShimmeringFan";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int COLORS = 3;
    private static final int BLOCK = 7;

    private final ArrayList<AbstractCard.CardColor> colorsPlayed = new ArrayList<>();

    public ShimmeringFan() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        this.colorsPlayed.clear();
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction useCardAction) {
        if (this.counter >= COLORS) {
            return;
        }
        if (!colorsPlayed.contains(c.color)) {
            colorsPlayed.add(c.color);
            this.counter++;
        }
        if (this.counter == COLORS) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GainBlockAction(AbstractDungeon.player, BLOCK));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], COLORS, BLOCK);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShimmeringFan();
    }
}
