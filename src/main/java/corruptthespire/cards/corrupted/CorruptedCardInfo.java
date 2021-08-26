package corruptthespire.cards.corrupted;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class CorruptedCardInfo {
    public final AbstractCorruptedCard card;
    public final AbstractCard.CardRarity rarity;

    public CorruptedCardInfo(AbstractCorruptedCard card, AbstractCard.CardRarity rarity) {
        this.card = card;
        this.rarity = rarity;
    }
}
