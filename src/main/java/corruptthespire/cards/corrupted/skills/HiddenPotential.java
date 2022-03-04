package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.HiddenPotentialAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

public class HiddenPotential extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:HiddenPotential";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int BLOCK = 9;
    private static final int UPGRADE_BLOCK = 3;
    private static final int DRAW = 2;
    private static final int UPGRADE_DRAW = 1;
    private static final int ARTIFACT = 1;
    private static final int UPGRADE_ARTIFACT = 1;
    private static final int ABYSSTOUCHED = 4;
    private static final int UPGRADE_ABYSSTOUCHED = 2;

    public HiddenPotential() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.ALL_ENEMY);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.magicNumber2 = this.baseMagicNumber2 = ARTIFACT;
        this.magicNumber3 = this.baseMagicNumber3 = ABYSSTOUCHED;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_DRAW);
            this.upgradeMagicNumber2(UPGRADE_ARTIFACT);
            this.upgradeMagicNumber3(UPGRADE_ABYSSTOUCHED);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        this.addToBot(new HiddenPotentialAction(this.block, this.magicNumber, this.magicNumber2, this.magicNumber3));
    }
}
