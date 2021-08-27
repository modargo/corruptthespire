package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Omamori;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class CorruptedOmamori extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:CorruptedOmamori";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int CORRUPTION = 4;

    public CorruptedOmamori() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    public boolean tryTrigger(AbstractCard c) {
        if (c.color == AbstractCard.CardColor.CURSE) {
            AbstractRelic omamori = AbstractDungeon.player.getRelic(Omamori.ID);
            if (omamori == null || omamori.counter <= 0) {
                this.flash();
                Cor.addCorruption(CORRUPTION);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], CORRUPTION);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CorruptedOmamori();
    }
}
