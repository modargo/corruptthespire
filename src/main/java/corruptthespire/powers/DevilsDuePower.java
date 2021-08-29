package corruptthespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class DevilsDuePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:DevilsDue";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int statuses;
    private boolean discardToo;

    public DevilsDuePower(AbstractCreature owner, int statuses, boolean discardToo) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.statuses = statuses;
        this.discardToo = discardToo;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(this.discardToo ? DESCRIPTIONS[1] : DESCRIPTIONS[0], this.statuses);
    }

    //The functionality for this is in PandemoniumArchfiend, the only enemy that uses this power
}
