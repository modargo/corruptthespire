package corruptthespire.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;

public class WheelOfFortune extends CustomCard {
    public static final String ID = "CorruptTheSpire:WheelOfFortune";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int CARDS = 2;
    private static final int UPGRADE_CARDS = 1;

    public WheelOfFortune() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.baseMagicNumber = CARDS;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
            this.addToBot(new MakeTempCardInHandAction(c, true));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeMagicNumber(UPGRADE_CARDS);
            this.upgradeName();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WheelOfFortune();
    }
}
