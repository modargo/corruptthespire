package corruptthespire.corruptions.campfire.options;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.RestRoom;
import corruptthespire.Cor;

import java.text.MessageFormat;
import java.util.ArrayList;

public class RareCardOption extends AbstractCorruptedCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:RareCardOption");
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
        //TODO Change to something else
        return ImageMaster.CAMPFIRE_DIG_BUTTON;
    }

    @Override
    public void useOption() {
        Cor.reduceFragments(this.getFragmentCost());
        //TODO Should I put this in an effect? If so can share with CorruptedCardOption
        AbstractDungeon.getCurrRoom().rewards.clear();
        AbstractDungeon.getCurrRoom().addCardReward(this.getReward());
        AbstractDungeon.combatRewardScreen.open();
        ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
    }

    private RewardItem getReward() {
        RewardItem rewardItem = new RewardItem();
        ArrayList<AbstractCard> cards = new ArrayList<>();

        while (cards.size() < rewardItem.cards.size()) {
            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
            if (cards.stream().noneMatch(c -> c.cardID.equals(card.cardID))) {
                cards.add(card);
            }
        }

        rewardItem.cards = cards;
        return rewardItem;
    }

}
