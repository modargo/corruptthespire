package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.combat.CorruptedFlashEffect;

import java.text.MessageFormat;

public class CorruptedFormPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:CorruptedForm";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CorruptedFormPower(AbstractCreature owner, int amount) {
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

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != null && !m.isDeadOrEscaped()) {
                    if (Settings.FAST_MODE) {
                        this.addToBot(new VFXAction(new CorruptedFlashEffect(m.hb.cX, m.hb.cY)));
                    }
                    else {
                        this.addToBot(new VFXAction(new CorruptedFlashEffect(m.hb.cX, m.hb.cY), 0.2F));
                    }
                }
            }
            int damage = (int)((this.amount / 100.0f) * Cor.corruption);
            this.addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(damage, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
        }
    }
}
