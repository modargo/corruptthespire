package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.monsters.MasterOfTimeAndSpace;

import java.text.MessageFormat;

public class SpatialFissurePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:SpatialFissure";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int healAmount;

    public SpatialFissurePower(AbstractCreature owner, int healAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.healAmount = healAmount;
        this.updateDescription();
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.healAmount);
    }

    @Override
    public void onDeath() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying && !m.isDead) {
                this.addToBot(new HealAction(m, this.owner, this.healAmount));
                if (m.id.equals(MasterOfTimeAndSpace.ID)) {
                    this.addToBot(new ApplyPowerAction(m, this.owner, new WorldAblazePower(m)));
                }
            }
        }
    }
}
