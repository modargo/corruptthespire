package corruptthespire.cards.corrupted.attacks;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

public class AstralGust extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:AstralGust";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int DAMAGE = 10;

    public AstralGust() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.exhaust = true;
        this.isMultiDamage = true;
        MultiCardPreview.add(this, new Apparition(), new Miracle(), new Slimed());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            for (AbstractCard c : MultiCardPreview.multiCardPreview.get(this)) {
                if (c.type != CardType.STATUS) {
                    c.upgrade();
                }
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        AbstractCard card1 = new Apparition();
        AbstractCard card2 = new Miracle();
        AbstractCard card3 = new Slimed();
        if (this.upgraded) {
            card1.upgrade();
            card2.upgrade();
        }
        this.addToBot(new MakeTempCardInDrawPileAction(card1, 1, true, true));
        this.addToBot(new MakeTempCardInDrawPileAction(card2, 1, true, true));
        this.addToBot(new MakeTempCardInDrawPileAction(card3, 1, true, true));
    }
}
