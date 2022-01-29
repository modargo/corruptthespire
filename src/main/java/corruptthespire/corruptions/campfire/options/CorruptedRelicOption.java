package corruptthespire.corruptions.campfire.options;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.campfire.CorruptedCampfireGainRelicEffect;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class CorruptedRelicOption extends AbstractCorruptedCampfireOption {
    public static final String ID = "CorruptTheSpire:CorruptedRelicOption";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.campfireImage(ID));
    public static final String[] TEXT = uiStrings.TEXT;

    private static final int CORRUPTION = 3;

    @Override
    public int getFragmentCost() {
        return 2;
    }

    @Override
    public String getLabel() {
        return TEXT[0];
    }

    @Override
    public String getDescription() {
        return MessageFormat.format(TEXT[1], CORRUPTION, this.getFragmentCost());
    }

    @Override
    public Texture getImage() {
        return IMG;
    }

    @Override
    public void useOption() {
        //TODO wrap this in an effect so it looks less jarring
        Cor.reduceFragments(this.getFragmentCost());
        Cor.addCorruption(CORRUPTION);
        AbstractDungeon.effectList.add(new CorruptedCampfireGainRelicEffect(Cor.returnRandomCorruptedRelic()));
    }
}
