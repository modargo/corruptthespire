package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.text.MessageFormat;
import java.util.ArrayList;

public class DevilsBargainAction extends AbstractGameAction {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:DevilsBargainAction").TEXT;
    private final AbstractPlayer player;
    private final int numberOfCards;
    private final boolean optional;

    public DevilsBargainAction(int numberOfCards, boolean optional) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (!this.player.discardPile.isEmpty() && this.numberOfCards > 0) {
                if (this.player.discardPile.size() <= this.numberOfCards && !this.optional) {
                    this.returnCards(this.player.discardPile.group);
                    this.isDone = true;
                } else {
                    String text = this.numberOfCards == 1 ? TEXT[0] : MessageFormat.format(TEXT[1], this.numberOfCards);
                    if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, true, text);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, text, false);
                    }

                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                this.returnCards(AbstractDungeon.gridSelectScreen.selectedCards);

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            this.tickDuration();
        }
    }

    private void returnCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard c : cards) {
            this.addToTop(new DiscardToHandAction(c));
        }
        this.addToTop(new DiscardAction(this.player, this.player, this.player.hand.size(), true));
    }
}