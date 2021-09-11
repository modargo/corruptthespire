package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AngerPower;
import corruptthespire.CorruptTheSpire;

public class SpacetimeMasteryPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:SpacetimeMastery";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final boolean twoActive;
    private int counter;

    public SpacetimeMasteryPower(AbstractCreature owner, boolean twoActive) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.twoActive = twoActive;
        this.counter = 0;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void onInitialApplication() {
        this.addPower(this.counter);
        if (this.twoActive) {
            this.addPower(this.counter + 1);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.removePower(this.counter);
        this.counter++;
        this.addPower(this.counter + (this.twoActive ? 1 : 0));
    }

    private void addPower(int index) {
        AbstractPower power;
        switch (index % 4) {
            case 0:
                power = new WaryPower(this.owner, 1);
                break;
            case 1:
                power = new ThoughtfulPower(this.owner, 1);
                break;
            case 2:
                power = new AngerPower(this.owner, 1);
                break;
            case 3:
                power = new RestlessPower(this.owner, 1);
                break;
            default:
                throw new RuntimeException("Impossible case");
        }
        this.addToTop(new ApplyPowerAction(this.owner, this.owner, power));
    }

    private void removePower(int index) {
        String powerId;
        switch (index % 4) {
            case 0:
                powerId = WaryPower.POWER_ID;
                break;
            case 1:
                powerId = ThoughtfulPower.POWER_ID;
                break;
            case 2:
                powerId = AngerPower.POWER_ID;
                break;
            case 3:
                powerId = RestlessPower.POWER_ID;
                break;
            default:
                throw new RuntimeException("Impossible case");
        }
        this.addToTop(new ReducePowerAction(this.owner, this.owner, powerId, 1));
    }

    @Override
    public void updateDescription() {
        this.description = this.twoActive ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }
}
