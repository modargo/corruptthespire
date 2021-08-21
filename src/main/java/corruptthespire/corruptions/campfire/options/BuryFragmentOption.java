package corruptthespire.corruptions.campfire.options;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.effects.campfire.CorruptedCampfireGainRelicEffect;
import corruptthespire.relics.FragmentOfCorruption;

import java.text.MessageFormat;

public class BuryFragmentOption extends AbstractCorruptedCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:BuryFragmentOption");
    public static final String[] TEXT = uiStrings.TEXT;

    private static final int CORRUPTION = 6;

    @Override
    public int getFragmentCost() {
        return 1;
    }

    @Override
    public String getLabel() {
        return TEXT[0];
    }

    @Override
    public String getDescription() {
        return MessageFormat.format(TEXT[1], this.getFragmentCost(), CORRUPTION);
    }

    @Override
    public Texture getImage() {
        //TODO Change to something else
        return ImageMaster.CAMPFIRE_DIG_BUTTON;
    }

    @Override
    public void useOption() {
        //TODO wrap this in an effect so it looks less jarring
        Cor.reduceFragments(this.getFragmentCost());
        Cor.addCorruption(-CORRUPTION);
    }
}
