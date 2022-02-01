package corruptthespire.rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.util.TextureLoader;

import java.util.ArrayList;

public class CorruptedCardReward extends AbstractCorruptTheSpireReward {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:Rewards").TEXT;
    private static final Texture ICON = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptedCardReward"));

    public ArrayList<AbstractCard> cards;

    public CorruptedCardReward() {
        super(ICON, TEXT[2], CustomRewardTypes.CORRUPTTHESPIRE_CORRUPTEDCARD);
    }

    public void populateCards() {
        //We have this as a method that gets a delayed call (from a patch on CombatRewardScreen.open) because
        //immediately generating the cards in the constructor would make saving and loading after combat change
        //the cards you get (because we'd advance RNG the first time, save the advanced RNG, load, and generate
        //card rewards from the advanced RNG)
        if (this.cards == null) {
            this.cards = CorruptedCardUtil.getCorruptedCardReward().cards;
        }
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, TEXT[3]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
