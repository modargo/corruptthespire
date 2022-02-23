package corruptthespire.relics.elite;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

public class HarbingersClaw extends CustomRelic {
    public static final String ID = "CorruptTheSpire:HarbingersClaw";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));

    public HarbingersClaw() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public static float increaseDamage(float damage) {
        return damage * (1.0F + (Cor.getCorruptionDamageMultiplierPercent() / 100.0F));
    }

    @Override
    public String getUpdatedDescription() {
        return (DESCRIPTIONS[0]);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HarbingersClaw();
    }
}
