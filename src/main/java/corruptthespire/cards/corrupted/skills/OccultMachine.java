package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.powers.PowerUtil;

public class OccultMachine extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:OccultMachine";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 1;
    private static final int ABYSSTOUCHED = 3;
    private static final int UPGRADE_ABYSSTOUCHED = 1;
    private static final int DRAW = 1;
    private static final int UPGRADE_DRAW = 1;

    public OccultMachine() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.ENEMY);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = ABYSSTOUCHED;
        this.magicNumber2 = this.baseMagicNumber2 = DRAW;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_ABYSSTOUCHED);
            this.upgradeMagicNumber2(UPGRADE_DRAW);
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        this.addToBot(new ApplyPowerAction(m, p, PowerUtil.abysstouched(m, this.magicNumber)));
        this.addToBot(new DrawCardAction(this.magicNumber2));
    }
}
