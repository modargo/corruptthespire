package corruptthespire.corruptions.campfire.options;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.RestRoom;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.rewards.CorruptedCardReward;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class CorruptedCardOption extends AbstractCorruptedCampfireOption {
    public static final String ID = "CorruptTheSpire:CorruptedCardOption";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.campfireImage(ID));
    public static final String[] TEXT = uiStrings.TEXT;

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
        return MessageFormat.format(TEXT[1], this.getFragmentCost());
    }

    @Override
    public Texture getImage() {
        return IMG;
    }

    @Override
    public void useOption() {
        Cor.reduceFragments(this.getFragmentCost());
        //TODO Should I put this in an effect? If so can share with RareCardOption
        AbstractDungeon.getCurrRoom().rewards.clear();
        AbstractDungeon.getCurrRoom().rewards.add(new CorruptedCardReward());
        AbstractDungeon.combatRewardScreen.open();
        ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
    }
}
