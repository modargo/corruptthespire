package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.DelayedVulnerablePower;
import corruptthespire.powers.DelayedWeakPower;
import corruptthespire.powers.PowerUtil;

public class StrifeManifest extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:StrifeManifest";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private final Version version;
    private static final byte ANIMOSITY_ATTACK = 1;
    private static final byte STRUGGLE_ATTACK = 2;
    private static final byte STRIFE_DEBUFF = 3;
    private static final int[] ANIMOSITY_DAMAGE = { 2, 3, 4 };
    private static final int[] A2_ANIMOSITY_DAMAGE = { 3, 4, 5 };
    private static final int[] ANIMOSITY_STRENGTH = { 1, 1, 2 };
    private static final int[] A17_ANIMOSITY_STRENGTH = { 1, 1, 2 };
    private static final int[] STRUGGLE_DAMAGE = { 3, 6, 9 };
    private static final int[] A2_STRUGGLE_DAMAGE = { 4, 7, 10 };
    private static final int[] STRIFE_DEBUFFS = { 1, 1, 2 };
    private static final int[] A17_STRIFE_DEBUFFS = { 2, 2, 3 };
    private static final int[] THORNS = { 1, 2, 3 };
    private static final int[] A17_THORNS = { 2, 3, 4 };
    private static final int[] HP_MIN = { 10, 14, 19 };
    private static final int[] HP_MAX = { 10, 14, 19 };
    private static final int[] A7_HP_MIN = { 11, 15, 21 };
    private static final int[] A7_HP_MAX = { 11, 15, 21 };
    private final int animosityDamage;
    private final int animosityStrength;
    private final int struggleDamage;
    private final int strifeDebuffs;
    private final int thorns;

    private boolean usedStrife = false;

    public StrifeManifest() {
        this(0.0f, 0.0f, Version.Act1);
    }

    public StrifeManifest(final float x, final float y, Version version) {
        super(StrifeManifest.NAME, ID, HP_MAX[0], -5.0F, 0, 175.0f, 175.0f, IMG, x, y);
        this.version = version;
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(this.v(A7_HP_MIN), this.v(A7_HP_MAX));
        } else {
            this.setHp(this.v(HP_MIN), this.v(HP_MAX));
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.animosityDamage = this.v(A2_ANIMOSITY_DAMAGE);
            this.struggleDamage = this.v(A2_STRUGGLE_DAMAGE);
        } else {
            this.animosityDamage = this.v(ANIMOSITY_DAMAGE);
            this.struggleDamage = this.v(STRUGGLE_DAMAGE);
        }
        this.damage.add(new DamageInfo(this, this.animosityDamage));
        this.damage.add(new DamageInfo(this, this.struggleDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.animosityStrength = this.v(A17_ANIMOSITY_STRENGTH);
            this.strifeDebuffs = this.v(A17_STRIFE_DEBUFFS);
            this.thorns = this.v(A17_THORNS);
        }
        else {
            this.animosityStrength = this.v(ANIMOSITY_STRENGTH);
            this.strifeDebuffs = this.v(STRIFE_DEBUFFS);
            this.thorns = this.v(THORNS);
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new ThornsPower(this, this.thorns)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case ANIMOSITY_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDead && !m.isDying) {
                        if (m == this) {
                            this.addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, this.animosityStrength)));
                        }
                        else {
                            this.addToBot(new ApplyPowerAction(m, this, PowerUtil.gainStrengthBuff(m, this.animosityStrength)));
                        }
                    }
                }
                break;
            case STRUGGLE_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case STRIFE_DEBUFF:
                this.usedStrife = true;
                this.addToBot(new AnimateShakeAction(this, 0.3F, 0.1F));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DelayedVulnerablePower(AbstractDungeon.player, this.strifeDebuffs)));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDead && !m.isDying) {
                        this.addToBot(new ApplyPowerAction(m, this, new DelayedWeakPower(m, this.strifeDebuffs)));
                    }
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || (this.usedStrife && !this.lastMove(ANIMOSITY_ATTACK))) {
            this.setMove(MOVES[0], ANIMOSITY_ATTACK, Intent.ATTACK_BUFF, this.animosityDamage);
        }
        else if (this.lastMove(STRUGGLE_ATTACK) && !this.usedStrife) {
            this.setMove(MOVES[2], STRIFE_DEBUFF, Intent.STRONG_DEBUFF);
        }
        else {
            this.setMove(MOVES[1], STRUGGLE_ATTACK, Intent.ATTACK, this.struggleDamage);
        }
    }

    private int v(int[] a) {
        switch (this.version) {
            case Act1: return a[0];
            case Act2: return a[1];
            default: return a[2];
        }
    }

    public enum Version {
        Act1,
        Act2,
        Act3
    }
}