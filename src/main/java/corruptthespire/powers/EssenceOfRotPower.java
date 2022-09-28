package corruptthespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class EssenceOfRotPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:EssenceOfRot";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int statuses;

    public EssenceOfRotPower(AbstractCreature owner, int statuses) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.statuses = statuses;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.statuses);
    }

    //The functionality for this is in RottingShambler, the only enemy that uses this power
}
