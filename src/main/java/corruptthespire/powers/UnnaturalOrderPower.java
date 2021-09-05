package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class UnnaturalOrderPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:UnnaturalOrder";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public UnnaturalOrderPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void onInitialApplication() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractPower power = m.getPower(AbysstouchedPower.POWER_ID);
            if (power == null) {
                power = m.getPower(PowerUtil.AbysstouchedPowerId);
            }
            if (power != null) {
                this.addToTop(new ApplyPowerAction(m, this.owner, new PoisonPower(m, this.owner, power.amount)));
                this.addToTop(new RemoveSpecificPowerAction(m, this.owner, power));
            }
        }
    }
}
