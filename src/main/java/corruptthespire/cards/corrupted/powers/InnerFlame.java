package corruptthespire.cards.corrupted.powers;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.CorruptingAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class InnerFlame extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:InnerFlame";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int STATS = 2;
    private static final int UPGRADE_STATS = 1;
    private static final int CORRUPTION = 1;

    public InnerFlame() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION), CardType.POWER, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STATS;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_STATS);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new CorruptingAction(CORRUPTION));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> list = new ArrayList<>();
        list.add(new TooltipInfo(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:corruption")), BaseMod.getKeywordDescription("corruptthespire:corruption")));
        return  list;
    }
}
