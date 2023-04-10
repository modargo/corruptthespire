package corruptthespire.cards.corrupted.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.powers.PowerUtil;

import java.text.MessageFormat;

public class ShadowAndFlame extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:ShadowAndFlame";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int DAMAGE = 11;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int DEBUFFS_AMOUNT = 2;
    private static final int UPGRADE_DEBUFFS_AMOUNT = 1;
    private static final int FRAIL = 2;

    public ShadowAndFlame() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, FRAIL), CardType.ATTACK, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = DEBUFFS_AMOUNT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_DEBUFFS_AMOUNT);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
        this.addToBot(new ApplyPowerAction(m, p, PowerUtil.abysstouched(m, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new FrailPower(p, FRAIL, false), FRAIL));
    }
}
