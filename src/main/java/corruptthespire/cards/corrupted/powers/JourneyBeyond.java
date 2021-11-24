package corruptthespire.cards.corrupted.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.FlexibleCostDiscoveryAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;

public class JourneyBeyond extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:JourneyBeyond";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int DEXTERITY = 2;
    private static final int COST_REDUCTION = 0;
    private static final int UPGRADE_COST_REDUCTION = 1;

    public JourneyBeyond() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DEXTERITY;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = MessageFormat.format(UPGRADE_DESCRIPTION, UPGRADE_COST_REDUCTION);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new FlexibleCostDiscoveryAction(this.upgraded ? UPGRADE_COST_REDUCTION : COST_REDUCTION));
    }
}
