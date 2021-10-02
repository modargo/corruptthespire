package corruptthespire.cards.corrupted.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.effects.combat.BlazeFromBeyondEffect;

import java.text.MessageFormat;

public class BlazeFromBeyond extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:BlazeFromBeyond";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int AMOUNT = 1;
    private static final int CORRUPTION_THRESHOLD = 10;

    public BlazeFromBeyond() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, DAMAGE, CORRUPTION_THRESHOLD), CardType.ATTACK, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = AMOUNT;
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int blazeCount = Cor.getCorruptionDamageMultiplierPercent();
        if (Settings.FAST_MODE) {
            this.addToBot(new VFXAction(new BlazeFromBeyondEffect(blazeCount, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.25F));
        } else {
            this.addToBot(new VFXAction(new BlazeFromBeyondEffect(blazeCount, AbstractDungeon.getMonsters().shouldFlipVfx()), 1.0F));
        }
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void applyPowers() {
        int storedBaseDamage = this.baseDamage;
        this.baseDamage = this.baseDamage + (this.magicNumber * (Cor.corruption / CORRUPTION_THRESHOLD));

        super.applyPowers();

        this.isDamageModified = this.isDamageModified || this.baseDamage > storedBaseDamage;
        this.baseDamage = storedBaseDamage;

        this.rawDescription = MessageFormat.format(DESCRIPTION, this.baseDamage, CORRUPTION_THRESHOLD) + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        int storedBaseDamage = this.baseDamage;
        this.baseDamage = this.baseDamage + (this.magicNumber * (Cor.corruption / CORRUPTION_THRESHOLD));

        super.calculateCardDamage(m);

        this.isDamageModified = this.isDamageModified || this.baseDamage > storedBaseDamage;
        this.baseDamage = storedBaseDamage;
    }
}
