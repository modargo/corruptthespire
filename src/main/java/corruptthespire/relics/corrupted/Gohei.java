package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class Gohei extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:Gohei";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int DAMAGE_THRESHOLD = 10;
    private static final int DAMAGE_REDUCTION = 5;

    public Gohei() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], DAMAGE_THRESHOLD, DAMAGE_REDUCTION);
    }

    public int onAttackedAfterToriiBeforeTungstenRod(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > DAMAGE_THRESHOLD) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new MakeTempCardInDiscardAction(new Wound(), 1));
            return damageAmount - DAMAGE_REDUCTION;
        } else {
            return damageAmount;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Gohei();
    }
}
