package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.monsters.MasterOfTimeAndSpace;

import java.text.MessageFormat;

public class TemporalFissurePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:TemporalFissure";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int FRAIL_AMOUNT = 2;

    private final int cleanseAmount;

    public TemporalFissurePower(AbstractCreature owner, int cleanseAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.cleanseAmount = cleanseAmount;
        this.updateDescription();
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.cleanseAmount, FRAIL_AMOUNT);
    }

    @Override
    public void onDeath() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying && !m.isDead && m.id.equals(MasterOfTimeAndSpace.ID)) {
                this.addToBot(new ApplyPowerAction(m, this.owner, new CleansePower(m, this.cleanseAmount, false)));
            }
        }
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this.owner, new FrailPower(AbstractDungeon.player, FRAIL_AMOUNT, true)));
    }
}
