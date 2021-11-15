package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

public class ProfaneShield extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:ProfaneShield";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int PERCENT = 15;
    private static final int UPGRADE_PERCENT = 5;

    public ProfaneShield() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.SELF);
        this.baseBlock = 0;
        this.magicNumber = this.baseMagicNumber = PERCENT;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PERCENT);
            this.initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        this.baseBlock = this.getBlock();

        super.applyPowers();

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyPowers();
        this.addToBot(new GainBlockAction(p, this.block));
    }

    private int getBlock() {
        return (int)(Cor.corruption * this.magicNumber / 100.0F);
    }
}
