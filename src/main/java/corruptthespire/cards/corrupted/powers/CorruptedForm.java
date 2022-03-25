package corruptthespire.cards.corrupted.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.GainCorruptionAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.powers.CorruptedFormPower;

import java.text.MessageFormat;

public class CorruptedForm extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:CorruptedForm";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 3;
    private static final int CORRUPTION = 5;
    private static final int PERCENT = 30;

    public CorruptedForm() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION), CardType.POWER, CardTarget.SELF);
        this.isEthereal = true;
        this.magicNumber = this.baseMagicNumber = PERCENT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.isEthereal = false;
            this.upgradeName();
            this.rawDescription = MessageFormat.format(cardStrings.UPGRADE_DESCRIPTION, CORRUPTION);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainCorruptionAction(CORRUPTION));
        this.addToBot(new ApplyPowerAction(p, p, new CorruptedFormPower(p, this.magicNumber), this.magicNumber));
    }
}
