package corruptthespire.cards.corrupted.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.powers.DiabolicForcePower;

public class DiabolicForce extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:DiabolicForce";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int FORCE_TO_ABYSSTOUCHED_PERCENT = 20;
    private static final int UPGRADE_FORCE_TO_ABYSSTOUCHED_PERCENT = 10;

    public DiabolicForce() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FORCE_TO_ABYSSTOUCHED_PERCENT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_FORCE_TO_ABYSSTOUCHED_PERCENT);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new DiabolicForcePower(p, this.magicNumber)));
    }
}
