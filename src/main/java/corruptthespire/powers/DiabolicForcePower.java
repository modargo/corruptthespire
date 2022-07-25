package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class DiabolicForcePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:DiabolicForce";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DiabolicForcePower(AbstractCreature owner, int amount) {
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

    public void onForceTrigger(int forceAmount) {
        int abysstouched = forceAmount * this.amount / 100;
        if (abysstouched > 0) {
            this.flash();
            List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters.stream().filter(m -> !m.isDeadOrEscaped()).collect(Collectors.toList());
            for (AbstractMonster m : monsters) {
                this.addToBot(new ApplyPowerAction(m, this.owner, PowerUtil.abysstouched(m, abysstouched)));
            }
        }
    }
}
