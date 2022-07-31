package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class DragonsHeartPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:DragonsHeart";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DragonsHeartPower(AbstractCreature owner, int amount) {
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
    public void onDeath() {
        this.flash();
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != this.owner && !m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                if (AbstractDungeon.actionManager.turnHasEnded) {
                    AbstractPower gainStrengthPower = new GainStrengthPower(m, this.amount);
                    gainStrengthPower.type = AbstractPower.PowerType.BUFF;
                    this.addToBot(new ApplyPowerAction(m, m, gainStrengthPower));
                }
                else {
                    this.addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, this.amount), this.amount));
                }
                this.addToBot(new ApplyPowerAction(m, m, new MetallicizePower(m, this.amount), this.amount));
            }
        }
    }
}
