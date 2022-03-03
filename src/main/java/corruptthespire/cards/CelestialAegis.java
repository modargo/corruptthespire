package corruptthespire.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.GainCorruptionAction;

import java.text.MessageFormat;

public class CelestialAegis extends CustomCard {
    public static final String ID = "CorruptTheSpire:CelestialAegis";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int CORRUPTION = 1;
    private static final int UPGRADE_CORRUPTION = 1;
    private static final int CARDS_IN_DECK_AND_DISCARD = 2;
    public static final int BOONS_FOR_BUFF = CARDS_IN_DECK_AND_DISCARD * 2;

    public CelestialAegis() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CARDS_IN_DECK_AND_DISCARD), CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseMagicNumber = CORRUPTION;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Boon();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainCorruptionAction(-this.magicNumber));
        this.addToBot(new MakeTempCardInDrawPileAction(new Boon(), CARDS_IN_DECK_AND_DISCARD, true, true));
        this.addToBot(new MakeTempCardInDiscardAction(new Boon(), CARDS_IN_DECK_AND_DISCARD));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeMagicNumber(UPGRADE_CORRUPTION);
            this.upgradeName();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CelestialAegis();
    }
}
