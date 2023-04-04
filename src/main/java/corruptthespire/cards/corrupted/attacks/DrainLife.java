package corruptthespire.cards.corrupted.attacks;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.CorruptingAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DrainLife extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:DrainLife";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int HEAL = 5;
    private static final int UPGRADE_DAMAGE = 1;
    private static final int UPGRADE_HEAL = 1;
    private static final int CORRUPTION = 1;

    public DrainLife() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION), CardType.ATTACK, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = HEAL;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_HEAL);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.addToBot(new HealAction(p, p, this.magicNumber));
        this.addToBot(new CorruptingAction(CORRUPTION));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> list = new ArrayList<>();
        list.add(new TooltipInfo(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:corruption")), BaseMod.getKeywordDescription("corruptthespire:corruption")));
        return  list;
    }
}
