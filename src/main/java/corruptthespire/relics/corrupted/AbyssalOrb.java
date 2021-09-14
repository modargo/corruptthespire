package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.PowerUtil;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class AbyssalOrb extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:AbyssalOrb";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int ABYSSTOUCHED_AT_START_OF_TURN = 1;
    public static final int ABYSSTOUCHED_INCREASE = 1;

    public AbyssalOrb() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        this.flash();
        AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, PowerUtil.abysstouched(m, ABYSSTOUCHED_AT_START_OF_TURN)));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], ABYSSTOUCHED_AT_START_OF_TURN, ABYSSTOUCHED_INCREASE);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AbyssalOrb();
    }
}
