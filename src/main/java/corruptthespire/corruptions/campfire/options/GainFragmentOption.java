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

public class GainFragmentOption extends AbstractCorruptedCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:GainFragmentOption");
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
        //TODO Change to something else
        return ImageMaster.CAMPFIRE_DIG_BUTTON;
    }

    @Override
    public void useOption() {
        AbstractDungeon.effectList.add(new CorruptedCampfireGainRelicEffect(new FragmentOfCorruption()));
    }
}
