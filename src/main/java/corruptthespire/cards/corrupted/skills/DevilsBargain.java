package corruptthespire.cards.corrupted.skills;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.CorruptingAction;
import corruptthespire.actions.DevilsBargainAction;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DevilsBargain extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:DevilsBargain";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int CARDS = 3;
    private static final int UPGRADE_CARDS = 2;
    private static final int CORRUPTION = 2;

    public DevilsBargain() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION), CardType.SKILL, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = CARDS;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_CARDS);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DevilsBargainAction(this.magicNumber, false));
        this.addToBot(new CorruptingAction(CORRUPTION));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> list = new ArrayList<>();
        list.add(new TooltipInfo(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:corruption")), BaseMod.getKeywordDescription("corruptthespire:corruption")));
        return  list;
    }
}
