package corruptthespire.cards.corrupted;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.ForbiddenRitualPower;

import java.text.MessageFormat;

public class ForbiddenRitual extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:ForbiddenRitual";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int STRENGTH = 1;
    private static final int UPGRADE_STRENGTH = 1;
    public static final int CORRUPTION = 1;

    public ForbiddenRitual() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION), CardType.POWER, CardTarget.SELF);
        this.baseMagicNumber = STRENGTH;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_STRENGTH);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new ForbiddenRitualPower(p, this.magicNumber), this.magicNumber));
    }
}
