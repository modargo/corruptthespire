package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;

public class MaskOfShards extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:MaskOfShards";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int HP_LOSS = 2;
    private static final int BLOCK = 15;
    private static final int UPGRADE_BLOCK = 5;
    private static final int AMOUNT = 1;
    private static final int CORRUPTION_THRESHOLD = 10;

    public MaskOfShards() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, HP_LOSS, BLOCK, CORRUPTION_THRESHOLD), CardType.SKILL, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = AMOUNT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeName();
            this.rawDescription = MessageFormat.format(DESCRIPTION, HP_LOSS, BLOCK + UPGRADE_BLOCK, CORRUPTION_THRESHOLD);
            this.initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        int block = BLOCK + (this.upgraded ? UPGRADE_BLOCK : 0);
        this.baseBlock = block + (this.magicNumber * (Cor.corruption / CORRUPTION_THRESHOLD));

        super.applyPowers();

        this.rawDescription = MessageFormat.format(DESCRIPTION, HP_LOSS, block, CORRUPTION_THRESHOLD) + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyPowers();
        this.addToBot(new LoseHPAction(p, p, HP_LOSS));
        this.addToBot(new GainBlockAction(p, this.block));
    }
}
