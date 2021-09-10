package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class DevilsDueAction extends AbstractGameAction {
    public DevilsDueAction() {
        this.startDuration = this.duration = 0.0F;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            ArrayList<AbstractCard> cardsToMove = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c.rarity != AbstractCard.CardRarity.BASIC && c.rarity != AbstractCard.CardRarity.COMMON && c.rarity != AbstractCard.CardRarity.CURSE) {
                    cardsToMove.add(c);
                }
            }
            for (AbstractCard c : cardsToMove) {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
            }
        }

        this.tickDuration();
    }
}
