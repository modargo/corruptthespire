package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class EldritchFireAction extends AbstractGameAction {
    private final int[] multiDamage;
    private final boolean freeToPlayOnce;
    private final DamageType damageType;
    private final AbstractPlayer p;
    private final int energyOnUse;
    private final int strength;

    public EldritchFireAction(AbstractPlayer p, int[] multiDamage, DamageType damageType, boolean freeToPlayOnce, int energyOnUse, int strength) {
        this.multiDamage = multiDamage;
        this.damageType = damageType;
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.strength = strength;
    }

    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            this.p.getRelic(ChemicalX.ID).flash();
        }

        if (effect > 0) {
            this.addToBot(new LoseHPAction(this.p, this.p, effect));

            for(int i = 0; i < effect; ++i) {
                if (i == 0) {
                    if (Settings.FAST_MODE) {
                        this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 0.3F));
                    } else {
                        this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 1.5F));
                    }
                }

                this.addToBot(new SFXAction("ATTACK_FIRE"));
                this.addToBot(new VFXAction(this.p, new CleaveEffect(), 0.0F));
                this.addToBot(new DamageAllEnemiesAction(this.p, this.multiDamage, this.damageType, AttackEffect.FIRE, true));
            }

            int strength = this.strength * effect;
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -strength), -strength, true, AttackEffect.NONE));
                    if (!m.hasPower(ArtifactPower.POWER_ID)) {
                        this.addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, strength), strength, true, AttackEffect.NONE));
                    }
                }
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
