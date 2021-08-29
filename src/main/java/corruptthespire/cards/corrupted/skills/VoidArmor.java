package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

public class VoidArmor extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:VoidArmor";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int BLOCK = 14;
    private static final int UPGRADE_BLOCK = 4;
    private static final int WEAK = 1;
    private static final int UPGRADE_WEAK = 1;

    public VoidArmor() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardTarget.ALL_ENEMY);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = WEAK;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_WEAK);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        this.addToBot(new GainBlockAction(p, this.block));
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
            }
        }
        this.addToBot(new MakeTempCardInDiscardAction(new VoidCard(), 1));
    }
}
