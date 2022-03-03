package corruptthespire.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class CelestialMightPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:CelestialMight";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int TEMPORARY_HP = 5;

    public CelestialMightPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
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
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            this.addToBot(new AddTemporaryHPAction(this.owner, this.owner, TEMPORARY_HP));
            this.addToBot(new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(AbstractDungeon.floorNum / 2, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE, true));
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE));
        }
    }
}
