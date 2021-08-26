package corruptthespire.cards.corrupted;

import basemod.abstracts.CustomCard;
import corruptthespire.cards.CustomTags;
import corruptthespire.cards.corrupted.CorruptedCardColor;

public abstract class AbstractCorruptedCard extends CustomCard {
    public AbstractCorruptedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED, CardRarity.SPECIAL, target);

        this.tags.add(CustomTags.CORRUPTED);
        this.setBannerTexture("corruptthespire/images/corruptedcolor/corrupted_banner.png", "corruptthespire/images/corruptedcolor/corrupted_banner_p.png");
    }
}
