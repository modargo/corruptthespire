package corruptthespire.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class Fated extends CustomCard implements CustomSavable<Integer> {
    public static final String ID = "CorruptTheSpire:Fated";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = -2;
    public static final int COMBATS = 5;

    public Fated() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, COMBATS), CardType.CURSE, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.NONE);
        this.misc = COMBATS;
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void upgrade() {}

    @Override
    public AbstractCard makeCopy() {
        return new Fated();
    }

    @Override
    public Integer onSave() {
        return this.misc;
    }

    @Override
    public void onLoad(Integer misc) {
        this.misc = misc;
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.baseMagicNumber;
    }

    public void decrementMisc() {
        this.misc--;
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.baseMagicNumber;
        this.initializeDescription();
    }
}
