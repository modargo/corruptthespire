package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.monsters.TranscendentDevourer;

import java.text.MessageFormat;

public class FetidBodyPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:FetidBody";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FetidBodyPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = TranscendentDevourer.FETID_BODY_DAMAGE_THRESHOLD;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(AbstractDungeon.ascensionLevel < 18 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        this.amount -= damageAmount;
        if (this.amount <= 0) {
            int triggers = Math.abs(this.amount / TranscendentDevourer.FETID_BODY_DAMAGE_THRESHOLD) + 1;
            for (int i = 0; i < triggers; i++) {
                this.trigger();
            }
            this.amount += TranscendentDevourer.FETID_BODY_DAMAGE_THRESHOLD * triggers;
        }
        this.updateDescription();
        return damageAmount;
    }

    private void trigger() {
        this.addToBot(new MakeTempCardInDrawPileAction(new Slimed(), 1, AbstractDungeon.ascensionLevel < 18, true));
    }
}
