package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.UnearthAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

public class Unearth extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:Unearth";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int SCRY = 2;
    private static final int UPGRADE_SCRY = 2;

    public Unearth() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.ALL_ENEMY);
        this.magicNumber = this.baseMagicNumber = SCRY;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_SCRY);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        this.addToBot(new ScryAction(this.magicNumber));
        this.addToBot(new UnearthAction(false));
    }
}
