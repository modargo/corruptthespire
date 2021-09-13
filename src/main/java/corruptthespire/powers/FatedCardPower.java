package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.CardUtil;

import java.text.MessageFormat;

public class FatedCardPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:FatedCard";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractCard.CardRarity rarity;

    public FatedCardPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.rarity = this.amount == 1 ? AbstractCard.CardRarity.COMMON : this.amount == 2 ? AbstractCard.CardRarity.UNCOMMON : AbstractCard.CardRarity.RARE;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void onInitialApplication() {
        this.atStartOfTurn();
    }

    @Override
    public void updateDescription() {
        int offset = this.amount == 1 ? 0 : 3;
        String description = this.rarity == AbstractCard.CardRarity.COMMON ? DESCRIPTIONS[offset] : this.rarity == AbstractCard.CardRarity.UNCOMMON ? DESCRIPTIONS[1 + offset] : DESCRIPTIONS[2 + offset];
        this.description = MessageFormat.format(description, this.amount);
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount <= 1) {
            this.flash();
            this.addToBot(new MakeTempCardInHandAction(CardUtil.returnTrulyRandomCardInCombat(this.rarity).makeCopy(), false));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, ID, 1));
        }
    }
}
