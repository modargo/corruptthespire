package corruptthespire.relics.corrupted;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class CorruptedEgg extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:CorruptedEgg";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final float UPGRADE_CHANCE_INCREASE = 33;
    private static final int CORRUPTION = 1;

    public CorruptedEgg() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:corruption")), BaseMod.getKeywordDescription("corruptthespire:corruption")));
    }

    public static float modifyUpgradeChance(float chance) {
        return chance + (AbstractDungeon.player.hasRelic(ID) ? UPGRADE_CHANCE_INCREASE / 100.0F : 0.0F);
    }

    public static void afterObtainCard(AbstractCard c) {
        if (AbstractDungeon.player.hasRelic(ID) && c.upgraded) {
            Cor.addCorruption(CORRUPTION);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], UPGRADE_CHANCE_INCREASE, CORRUPTION);
    }
    
    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CorruptedEgg();
    }
}
