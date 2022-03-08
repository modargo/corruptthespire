package corruptthespire.cards.corrupted;

import com.megacrit.cardcrawl.cards.AbstractCard;
import corruptthespire.cards.AbstractModCard;
import corruptthespire.cards.CustomTags;

public abstract class AbstractCorruptedCard extends AbstractModCard {
    public AbstractCorruptedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED, AbstractCard.CardRarity.SPECIAL, target);

        this.tags.add(CustomTags.CORRUPTED);
        this.setBannerTexture("corruptthespire/images/corruptedcolor/corrupted_banner.png", "corruptthespire/images/corruptedcolor/corrupted_banner_p.png");
    }
}
