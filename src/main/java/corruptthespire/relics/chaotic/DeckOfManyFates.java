package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.FatedCardPower;
import corruptthespire.util.TextureLoader;

public class DeckOfManyFates extends CustomRelic {
    public static final String ID = "CorruptTheSpire:DeckOfManyFates";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int MAX_TURN = 3;

    public DeckOfManyFates() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        int turn = AbstractDungeon.miscRng.random(1, MAX_TURN);
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FatedCardPower(AbstractDungeon.player, turn), turn));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DeckOfManyFates();
    }
}
