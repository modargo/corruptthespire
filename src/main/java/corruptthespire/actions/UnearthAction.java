package corruptthespire.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class UnearthAction extends AbstractGameAction {
    public static final String[] TEXT;
    private AbstractPlayer player;
    private boolean optional;

    public UnearthAction(boolean optional) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.optional = optional;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup eligibleCards = this.getEligibleCards();
            if (!eligibleCards.isEmpty()) {
                if (eligibleCards.size() <= 1 && !this.optional) {
                    ArrayList<AbstractCard> cardsToMove = new ArrayList<>(eligibleCards.group);
                    for (AbstractCard c : cardsToMove) {
                        if (this.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                            this.player.hand.addToHand(c);
                            this.player.discardPile.removeCard(c);
                        }

                        c.lighten(false);
                        c.applyPowers();
                    }

                    this.isDone = true;
                } else {
                    String text = this.getText();
                    if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(eligibleCards, 1, true, text);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(eligibleCards, 1, text, false);
                    }

                    this.tickDuration();
                }
            } else {
                this.addToTop(new DrawCardAction(1));
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (this.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        this.player.hand.addToHand(c);
                        this.player.discardPile.removeCard(c);
                    }

                    c.lighten(false);
                    c.unhover();
                    c.applyPowers();
                }

                for(AbstractCard c : this.player.discardPile.group) {
                    c.target_y = 0.0F;
                    c.unhover();
                    c.target_x = (float)CardGroup.DISCARD_PILE_X;
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
            if (this.isDone) {
                this.player.hand.applyPowers();
            }
        }
    }

    private CardGroup getEligibleCards() {
        CardGroup g = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : this.player.discardPile.group) {
            if (this.include(c)) {
                g.addToBottom(c);
            }
        }
        return g;
    }

    private boolean include(AbstractCard c) {
        return c.color != AbstractDungeon.player.getCardColor();
    }

    private String getText() {
        return TEXT[0];
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("DiscardPileToHandAction").TEXT;
    }
}