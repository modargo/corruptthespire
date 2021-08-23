package corruptthespire.cards;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.FragmentizeAction;
import corruptthespire.relics.FragmentOfCorruption;

import java.text.MessageFormat;

public class Fragmentize extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:Fragmentize";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 13;

    public Fragmentize() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, new FragmentOfCorruption().name), CardType.ATTACK, CardTarget.ENEMY);
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
        this.addToBot(new FragmentizeAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
    }
}
