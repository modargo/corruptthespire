package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;

public class LightningBlood extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:LightningBlood";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int HP_LOSS = 1;
    private static final int STRENGTH_AND_METALLICIZE = 3;

    public LightningBlood() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, HP_LOSS), CardType.SKILL, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STRENGTH_AND_METALLICIZE;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.exhaust = false;
            this.upgradeName();
            this.rawDescription = MessageFormat.format(cardStrings.UPGRADE_DESCRIPTION, HP_LOSS);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        this.addToBot(new LoseHPAction(p, p, HP_LOSS));
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, this.magicNumber)));
    }
}
