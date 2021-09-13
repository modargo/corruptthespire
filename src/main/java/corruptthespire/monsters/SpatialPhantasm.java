package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.SpatialFissurePower;
import corruptthespire.powers.WarpAuraPower;

public class SpatialPhantasm extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:SpatialPhantasm";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = SpatialPhantasm.monsterStrings.NAME;
    public static final String[] MOVES = SpatialPhantasm.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte SPATIAL_REND_ATTACK = 1;
    private static final byte MEND_BUFF = 2;
    private static final byte DIMENSIONAL_RIP_ATTACK = 3;
    private static final int SPATIAL_REND_DAMAGE = 2;
    private static final int A4_SPATIAL_REND_DAMAGE = 3;
    private static final int MEND_HEAL = 20;
    private static final int A19_MEND_HEAL = 30;
    private static final int DIMENSIONAL_RIP_DAMAGE = 14;
    private static final int A4_DIMENSIONAL_RIP_DAMAGE = 16;
    private static final int HP = 90;
    private static final int A9_HP = 100;
    private final int spatialRendDamage;
    private final int mendHeal;
    private final int dimensionalRipDamage;

    public SpatialPhantasm() {
        this(0.0f, 0.0f);
    }

    public SpatialPhantasm(final float x, final float y) {
        //super(MasterOfTimeAndSpace.NAME, ID, HP, -5.0F, 0, 1370.0f, 775.0f, IMG, x, y);
        super(SpatialPhantasm.NAME, ID, HP, -5.0F, 0, 95.0f, 115.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.spatialRendDamage = A4_SPATIAL_REND_DAMAGE;
            this.dimensionalRipDamage = A4_DIMENSIONAL_RIP_DAMAGE;
        } else {
            this.spatialRendDamage = SPATIAL_REND_DAMAGE;
            this.dimensionalRipDamage = DIMENSIONAL_RIP_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.spatialRendDamage));
        this.damage.add(new DamageInfo(this, this.dimensionalRipDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.mendHeal = A19_MEND_HEAL;
        }
        else {
            this.mendHeal = MEND_HEAL;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WarpAuraPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SpatialFissurePower(this, this.mendHeal)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case SPATIAL_REND_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case MEND_HEAL:
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDead && !m.isDying) {
                        this.addToBot(new HealAction(m, this, this.mendHeal));
                    }
                }
                break;
            case DIMENSIONAL_RIP_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(DIMENSIONAL_RIP_ATTACK)) {
            this.setMove(MOVES[0], SPATIAL_REND_ATTACK, Intent.ATTACK_DEBUFF, this.spatialRendDamage, 2, true);
        }
        else if (this.lastMove(SPATIAL_REND_ATTACK)) {
            this.setMove(MOVES[1], MEND_BUFF, Intent.BUFF);
        }
        else {
            this.setMove(MOVES[2], DIMENSIONAL_RIP_ATTACK, Intent.ATTACK, this.dimensionalRipDamage);
        }
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