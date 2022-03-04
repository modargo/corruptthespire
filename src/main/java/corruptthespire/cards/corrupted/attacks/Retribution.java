package corruptthespire.cards.corrupted.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

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
        int times = this.countCommonDebuffs() + 1;
        for (int i = 0; i < times; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    private int countCommonDebuffs() {
        List<String> positivePowerIds = Arrays.asList(WeakPower.POWER_ID, FrailPower.POWER_ID, VulnerablePower.POWER_ID);
        List<String> negativePowerIds = Arrays.asList(DexterityPower.POWER_ID, FocusPower.POWER_ID);
        List<String> numberlessPowers = Collections.singletonList(ConfusionPower.POWER_ID);
        return AbstractDungeon.player.powers.stream().map(p -> {
            if (positivePowerIds.contains(p.ID)) {
                return 1;
            }
            else if (negativePowerIds.contains(p.ID) && p.amount < 0) {
                return 1;
            }
            else if (numberlessPowers.contains(p.ID)) {
                return 1;
            }
            return 0;
        }).reduce(0, Integer::sum);
    }
}
