package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;

public class WickedWard extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:WickedWard";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 0;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 2;
    private static final int ADDITIONAL_BLOCK = 4;
    private static final int UPGRADE_ADDITIONAL_BLOCK = 1;
    private static final int CORRUPTION_THRESHOLD = 60;

    public WickedWard() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION_THRESHOLD), CardType.SKILL, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = ADDITIONAL_BLOCK;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_ADDITIONAL_BLOCK);
            this.upgradeName();
        }
    }

    @Override
    public void applyPowers() {
        int b = this.baseBlock;
        this.baseBlock = this.baseMagicNumber;
        super.applyPowers();
        this.magicNumber = this.block;
        this.isMagicNumberModified = this.isBlockModified;
        this.baseBlock = b;
        super.applyPowers();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyPowers();
        this.addToBot(new GainBlockAction(p, Cor.corruption >= CORRUPTION_THRESHOLD ? this.block + this.magicNumber : this.block));
    }
}
