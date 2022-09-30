package corruptthespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ThreatenedPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:Threatened";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ThreatenedPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.loadRegion("master_smite");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    //The functionality for this is in RelentlessWar and EternalFear, the enemies that benefit from this power
}
