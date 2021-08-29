package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.DarkLoreAction;
import corruptthespire.actions.GainCorruptionAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;

public class EldritchInsight extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:EldritchInsight";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int TIMES = 2;
    private static final int UPGRADE_TIMES = 1;
    private static final int CORRUPTION = 1;

    public EldritchInsight() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION), CardType.SKILL, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = TIMES;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_TIMES);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            this.addToBot(new DarkLoreAction());
        }
        this.addToBot(new GainCorruptionAction(CORRUPTION));
    }
}
