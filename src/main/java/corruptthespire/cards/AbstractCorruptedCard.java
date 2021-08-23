package corruptthespire.cards;

import basemod.abstracts.CustomCard;

public abstract class AbstractCorruptedCard extends CustomCard {
    public AbstractCorruptedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED, CardRarity.SPECIAL, target);

        this.setBannerTexture("corruptthespire/images/corruptedcolor/corrupted_banner.png", "corruptthespire/images/corruptedcolor/corrupted_banner_p.png");
    }
}
