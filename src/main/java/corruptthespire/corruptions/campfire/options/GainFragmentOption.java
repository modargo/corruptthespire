package corruptthespire.corruptions.campfire.options;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.campfire.CorruptedCampfireGainRelicEffect;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.util.TextureLoader;

public class GainFragmentOption extends AbstractCorruptedCampfireOption {
    public static final String ID = "CorruptTheSpire:GainFragmentOption";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.campfireImage(ID));
    public static final String[] TEXT = uiStrings.TEXT;

    @Override
    public int getFragmentCost() {
        return 0;
    }

    @Override
    public String getLabel() {
        return TEXT[0];
    }

    @Override
    public String getDescription() {
        return TEXT[1];
    }

    @Override
    public Texture getImage() {
        return IMG;
    }

    @Override
    public void useOption() {
        AbstractDungeon.effectList.add(new CorruptedCampfireGainRelicEffect(new FragmentOfCorruption()));
    }
}
