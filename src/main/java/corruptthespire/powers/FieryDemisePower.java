package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class FieryDemisePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:FieryDemise";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FieryDemisePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void onDeath() {
        this.addToBot(new MakeTempCardInDrawPileAction(new Burn(), this.amount, true, true));
    }
}
