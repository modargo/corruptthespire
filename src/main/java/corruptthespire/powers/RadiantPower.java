package corruptthespire.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class RadiantPower extends TwoAmountPower {
    public static final String POWER_ID = "CorruptTheSpire:Radiant";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RadiantPower(AbstractCreature owner, int amount, int amount2) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount2;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount, this.amount2);
    }

    @Override
    public void onDeath() {
        this.flash();
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != this.owner && !m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(m, this.owner, PowerUtil.gainStrengthBuff(m, this.amount)));
                this.addToBot(new HealAction(m, this.owner, this.amount2));
            }
        }
    }
}
