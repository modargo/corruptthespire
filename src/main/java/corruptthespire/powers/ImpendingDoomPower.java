package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class ImpendingDoomPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:ImpendingDoom";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int HP_THRESHOLD = 50;

    public ImpendingDoomPower(AbstractCreature owner, int amount) {
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
        this.description = this.amount == 1 ? DESCRIPTIONS[0] : MessageFormat.format(DESCRIPTIONS[1], this.amount);
    }

    @Override
    public void atEndOfRound() {
        if (this.owner.currentHealth <= this.owner.maxHealth * (HP_THRESHOLD / 100.0f)) {
            for (int i = 0; i < this.amount; i++) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this.owner, new VulnerablePower(AbstractDungeon.player, 1, true)));

            }
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
