package corruptthespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.monsters.Harbinger;

import java.text.MessageFormat;

public class AstralCoreCounterPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:AstralCoreCounter";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AstralCorePower astralCorePower;

    //This is separated out as its own power purely for UI reasons. We want to both have Astral Core showing the amount
    //of strength that will be gained, and to have this showing the counter of damaged left until Astral Core triggers.
    public AstralCoreCounterPower(AbstractCreature owner, AstralCorePower astralCorePower) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.astralCorePower = astralCorePower;
        this.amount = Harbinger.ASTRAL_CORE_DAMAGE_THRESHOLD;
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
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        this.amount -= damageAmount;
        if (this.amount <= 0) {
            int triggers = Math.abs(this.amount / Harbinger.ASTRAL_CORE_DAMAGE_THRESHOLD) + 1;
            for (int i = 0; i < triggers; i++) {
                this.astralCorePower.trigger();
            }
            this.amount += Harbinger.ASTRAL_CORE_DAMAGE_THRESHOLD * triggers;
            this.updateDescription();
        }
        return damageAmount;
    }
}
