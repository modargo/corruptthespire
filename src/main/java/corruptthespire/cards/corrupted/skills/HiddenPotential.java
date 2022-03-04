package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int BLOCK = 12;
    private static final int UPGRADE_BLOCK = 2;
    private static final int DRAW = 1;
    private static final int UPGRADE_DRAW = 1;
    private static final int ARTIFACT = 1;

    public HiddenPotential() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.ALL_ENEMY);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.magicNumber2 = this.baseMagicNumber2 = ARTIFACT;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_DRAW);
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        this.addToBot(new GainBlockAction(p, this.block));
        this.addToBot(new HiddenPotentialAction(this.magicNumber, this.magicNumber2));
    }
}
