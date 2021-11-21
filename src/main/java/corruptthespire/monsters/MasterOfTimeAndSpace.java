package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.combat.BlazeFromBeyondEffect;
import corruptthespire.powers.SpacetimeMasteryPower;

public class MasterOfTimeAndSpace extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:MasterOfTimeAndSpace";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MasterOfTimeAndSpace.monsterStrings.NAME;
    public static final String[] MOVES = MasterOfTimeAndSpace.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte CHAOS_ZONE_ATTACK = 1;
    private static final byte RAIN_DESTRUCTION_ATTACK = 2;
    private static final byte DARK_WILL_BUFF = 3;
    private static final byte DARK_FLAMES_EMBRACE = 4;
    private static final int CHAOS_ZONE_DAMAGE = 0;
    private static final int A4_CHAOS_ZONE_DAMAGE = 1;
    private static final int CHAOS_ZONE_WEAK = 1;
    private static final int CHAOS_ZONE_STATUSES = 1;
    private static final int RAIN_DESTRUCTION_DAMAGE = 4;
    private static final int A4_RAIN_DESTRUCTION_DAMAGE = 5;
    private static final int DARK_WILL_STRENGTH = 2;
    private static final int A19_DARK_WILL_STRENGTH = 3;
    private static final int DARK_FLAMES_EMBRACE_DAMAGE = 11;
    private static final int A4_DARK_FLAMES_EMBRACE_DAMAGE = 13;
    private static final int HP = 275;
    private static final int A9_HP = 300;
    private final int chaosZoneDamage;
    private final int rainDestructionDamage;
    private final int darkWillStrength;
    private final int darkFlamesEmbraceDamage;

    public MasterOfTimeAndSpace() {
        this(0.0f, 0.0f);
    }

    public MasterOfTimeAndSpace(final float x, final float y) {
        super(MasterOfTimeAndSpace.NAME, ID, HP, -5.0F, 0, 1000.0f, 560.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.chaosZoneDamage = A4_CHAOS_ZONE_DAMAGE;
            this.rainDestructionDamage = A4_RAIN_DESTRUCTION_DAMAGE;
            this.darkFlamesEmbraceDamage = A4_DARK_FLAMES_EMBRACE_DAMAGE;
        } else {
            this.chaosZoneDamage = CHAOS_ZONE_DAMAGE;
            this.rainDestructionDamage = RAIN_DESTRUCTION_DAMAGE;
            this.darkFlamesEmbraceDamage = DARK_FLAMES_EMBRACE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.chaosZoneDamage));
        this.damage.add(new DamageInfo(this, this.rainDestructionDamage));
        this.damage.add(new DamageInfo(this, this.darkFlamesEmbraceDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.darkWillStrength = A19_DARK_WILL_STRENGTH;
        }
        else {
            this.darkWillStrength = DARK_WILL_STRENGTH;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SpacetimeMasteryPower(this, AbstractDungeon.ascensionLevel >= 19)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CHAOS_ZONE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.75F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, CHAOS_ZONE_WEAK, true)));
                int statuses = CHAOS_ZONE_STATUSES + this.countLivingPhantasms() == 0 ? 1 : 0;
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), statuses));
                break;
            case RAIN_DESTRUCTION_ATTACK:
                if (Settings.FAST_MODE) {
                    this.addToBot(new VFXAction(new BlazeFromBeyondEffect(35,true), 0.25F));
                } else {
                    this.addToBot(new VFXAction(new BlazeFromBeyondEffect(35, true), 1.0F));
                }
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
            case DARK_WILL_BUFF:
                this.addToBot(new VFXAction(new BorderLongFlashEffect(Color.PURPLE, true)));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDead && !m.isDying) {
                        if (m == this) {
                            this.addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, this.darkWillStrength)));
                        }
                        else {
                            AbstractPower gainStrengthPower = new GainStrengthPower(m, this.darkWillStrength);
                            gainStrengthPower.type = AbstractPower.PowerType.BUFF;
                            this.addToBot(new ApplyPowerAction(m, this, gainStrengthPower));
                        }
                    }
                }
                break;
            case DARK_FLAMES_EMBRACE:
                this.addToBot(new VFXAction(new BorderLongFlashEffect(Color.RED, true)));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.darkWillStrength)));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(DARK_WILL_BUFF) || this.lastMove(DARK_FLAMES_EMBRACE)) {
            this.setMove(MOVES[0], CHAOS_ZONE_ATTACK, Intent.ATTACK_DEBUFF, this.chaosZoneDamage);
        }
        else if (this.lastMove(CHAOS_ZONE_ATTACK)) {
            this.setMove(MOVES[1], RAIN_DESTRUCTION_ATTACK, Intent.ATTACK, this.rainDestructionDamage);
        }
        else if (this.lastMove(RAIN_DESTRUCTION_ATTACK)) {
            if (this.countLivingPhantasms() > 0) {
                this.setMove(MOVES[2], DARK_WILL_BUFF, Intent.BUFF);
            }
            else {
                this.setMove(MOVES[3], DARK_FLAMES_EMBRACE, Intent.ATTACK_BUFF, this.darkFlamesEmbraceDamage);
            }
        }
    }

    private int countLivingPhantasms() {
        return (int)AbstractDungeon.getCurrRoom().monsters.monsters.stream().filter(m -> m.id.equals(SpatialPhantasm.ID) || m.id.equals(TemporalPhantasm.ID)).count();
    }

    @Override
    public void die() {
        super.die();
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.onBossVictoryLogic();
        }
    }
}