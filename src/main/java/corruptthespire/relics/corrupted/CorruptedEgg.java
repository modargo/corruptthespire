package corruptthespire.relics.corrupted;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class CorruptedEgg extends CustomRelic {
    public static final String ID = "CorruptTheSpire:CorruptedEgg";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final float UPGRADE_CHANCE_INCREASE = 25;
    private static final int CORRUPTION = 1;

    public CorruptedEgg() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public static float modifyUpgradeChance(float chance) {
        return chance + (AbstractDungeon.player.hasRelic(ID) ? UPGRADE_CHANCE_INCREASE / 100.0F : 0.0F);
    }

    public static void afterObtainCard(AbstractCard c) {
        if (c.upgraded) {
            Cor.addCorruption(CORRUPTION);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], UPGRADE_CHANCE_INCREASE, CORRUPTION);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CorruptedEgg();
    }
}
