package corruptthespire.cards.corrupted;

import basemod.abstracts.CustomCard;
import corruptthespire.cards.CustomTags;

public abstract class AbstractCorruptedCard extends CustomCard {
    public int magicNumber2;
    public int baseMagicNumber2;
    public boolean upgradedMagicNumber2;
    public boolean isMagicNumber2Modified;
    public int magicNumber3;
    public int baseMagicNumber3;
    public boolean upgradedMagicNumber3;
    public boolean isMagicNumber3Modified;

    public AbstractCorruptedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED, CardRarity.SPECIAL, target);

        this.tags.add(CustomTags.CORRUPTED);
        this.setBannerTexture("corruptthespire/images/corruptedcolor/corrupted_banner.png", "corruptthespire/images/corruptedcolor/corrupted_banner_p.png");
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (this.upgradedMagicNumber2) {
            this.magicNumber2 = this.baseMagicNumber2;
            this.isMagicNumber2Modified = true;
        }
        if (this.upgradedMagicNumber3) {
            this.magicNumber3 = this.baseMagicNumber3;
            this.isMagicNumber3Modified = true;
        }
    }

    public void upgradeMagicNumber2(int amount) {
        this.magicNumber2 = this.baseMagicNumber2 = this.baseMagicNumber2 + amount;
        this.upgradedMagicNumber2 = true;
    }

    public void upgradeMagicNumber3(int amount) {
        this.magicNumber3 = this.baseMagicNumber3 = this.baseMagicNumber3 + amount;
        this.upgradedMagicNumber3 = true;
    }
}
