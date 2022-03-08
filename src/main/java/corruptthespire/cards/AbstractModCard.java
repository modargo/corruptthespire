package corruptthespire.cards;

import basemod.abstracts.CustomCard;

public abstract class AbstractModCard extends CustomCard {
    public int magicNumber2;
    public int baseMagicNumber2;
    public boolean upgradedMagicNumber2;
    public boolean isMagicNumber2Modified;
    public int magicNumber3;
    public int baseMagicNumber3;
    public boolean upgradedMagicNumber3;
    public boolean isMagicNumber3Modified;

    public AbstractModCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
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
