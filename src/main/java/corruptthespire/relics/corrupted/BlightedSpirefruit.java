package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class BlightedSpirefruit extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:BlightedSpirefruit";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int MAX_HP_LOSS = 4;
    private static final int STRENGTH = 2;

    public BlightedSpirefruit() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH), STRENGTH));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.decreaseMaxHealth(MAX_HP_LOSS);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.maxHealth > MAX_HP_LOSS;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], MAX_HP_LOSS, STRENGTH);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlightedSpirefruit();
    }
}
