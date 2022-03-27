package corruptthespire.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import corruptthespire.CorruptTheSpire;

public class Nudge extends AbstractModCard {
    public static final String ID = "CorruptTheSpire:Nudge";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int TEMP_HP = 1;
    private static final int UPGRADE_TEMP_HP = 1;
    private static final int SCRY = 2;
    private static final int UPGRADE_SCRY = 1;
    private static final int DRAW_NEXT_TURN = 1;
    private static final int UPGRADE_DRAW_NEXT_TURN = 1;
    private static final int EXHAUSTIVE = 2;

    public Nudge() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = TEMP_HP;
        this.magicNumber2 = this.baseMagicNumber2 = SCRY;
        this.magicNumber3 = this.baseMagicNumber3 = DRAW_NEXT_TURN;
        ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
        this.addToBot(new AddTemporaryHPAction(p, p, this.magicNumber));
        this.addToBot(new ScryAction(this.magicNumber2));
        this.addToBot(new DrawCardAction(1));
        this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.magicNumber3)));
        this.addToBot(new UpgradeRandomCardAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeMagicNumber(UPGRADE_TEMP_HP);
            this.upgradeMagicNumber2(UPGRADE_SCRY);
            this.upgradeMagicNumber3(UPGRADE_DRAW_NEXT_TURN);
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Nudge();
    }
}
