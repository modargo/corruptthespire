package corruptthespire.cards.corrupted.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Retribution extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:Retribution";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 2;

    public Retribution() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
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
        for (int i = 0; i < this.calculateTimes(m); i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        int times = this.calculateTimes(m);
        this.rawDescription = DESCRIPTION + (times == 1 ? cardStrings.EXTENDED_DESCRIPTION[0] : MessageFormat.format(cardStrings.EXTENDED_DESCRIPTION[1], times));
        this.initializeDescription();
    }

    private int calculateTimes(AbstractMonster m) {
        return this.countDebuffs(AbstractDungeon.player) + this.countDebuffs(m) + 1;
    }

    private int countDebuffs(AbstractCreature creature) {
        return (int)creature.powers.stream().filter(c -> c.type == AbstractPower.PowerType.DEBUFF).count();
    }
}
