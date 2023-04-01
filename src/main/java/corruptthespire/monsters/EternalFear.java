package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.SoulLinkPower;

public class EternalFear extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:EternalFear";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = EternalFear.monsterStrings.NAME;
    public static final String[] MOVES = EternalFear.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte DREAD_DEBUFF = 1;
    private static final byte DESPAIR_DEBUFF = 2;
    private static final byte HORROR_ATTACK = 3;
    private static final byte PANIC_ATTACK = 4;
    private static final int DREAD_FRAIL = 0;
    private static final int A19_DREAD_FRAIL = 1;
    private static final int DREAD_FRAIL_EXTRA = 1;
    private static final int DREAD_VULNERABLE = 1;
    private static final int DREAD_VULNERABLE_EXTRA = 1;
    private static final int DESPAIR_BLOCK = 5;
    private static final int A9_DESPAIR_BLOCK = 10;
    private static final int DESPAIR_WEAK = 2;
    private static final int DESPAIR_DRAW_DOWN = 1;
    private static final int A19_DESPAIR_DRAW_DOWN = 1;
    private static final int HORROR_DAMAGE = 8;
    private static final int A4_HORROR_DAMAGE = 10;
    private static final int PANIC_DAMAGE = 11;
    private static final int A4_PANIC_DAMAGE = 13;
    private static final int HP = 375;
    private static final int A9_HP = 400;
    private final int dreadFrail;
    private final int despairBlock;
    private final int despairDrawDown;
    private final int horrorDamage;
    private final int panicDamage;

    private AbstractMonster partner;

    public EternalFear() {
        this(0.0f, 0.0f);
    }

    public EternalFear(final float x, final float y) {
        super(EternalFear.NAME, ID, HP, -5.0F, 0, 255.0f, 260.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.despairBlock = A9_DESPAIR_BLOCK;
        } else {
            this.setHp(HP);
            this.despairBlock = DESPAIR_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.horrorDamage = A4_HORROR_DAMAGE;
            this.panicDamage = A4_PANIC_DAMAGE;
        } else {
            this.horrorDamage = HORROR_DAMAGE;
            this.panicDamage = PANIC_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.horrorDamage));
        this.damage.add(new DamageInfo(this, this.panicDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.dreadFrail = A19_DREAD_FRAIL;
            this.despairDrawDown = A19_DESPAIR_DRAW_DOWN;
        }
        else {
            this.dreadFrail = DREAD_FRAIL;
            this.despairDrawDown = DESPAIR_DRAW_DOWN;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.partner = AbstractDungeon.getMonsters().getMonster(RelentlessWar.ID);
        if (this.partner == null) {
            throw new RuntimeException("Relentless War and Eternal Fear must be encountered together.");
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SoulLinkPower(this, this.partner)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case DREAD_DEBUFF:
                this.addToBot(new FastShakeAction(this, 0.5F, 0.2F));
                int frail = this.dreadFrail + (this.empowered() ? DREAD_FRAIL_EXTRA : 0);
                int vulnerable = DREAD_VULNERABLE + (this.empowered() ? DREAD_VULNERABLE_EXTRA : 0);
                if (frail > 0) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, frail, true)));
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, vulnerable, true)));
                break;
            case DESPAIR_DEBUFF:
                this.addToBot(new FastShakeAction(this, 0.5F, 0.2F));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                        this.addToBot(new GainBlockAction(m, this.despairBlock));
                    }
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DESPAIR_WEAK, true)));
                if (this.empowered()) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, this.despairDrawDown)));
                }
                break;
            case HORROR_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                if (this.empowered()) {
                    this.addToBot(new MakeTempCardInDiscardAction(new VoidCard(), 1));
                }
                else {
                    this.addToBot(new MakeTempCardInDiscardAction(new Dazed(), 1));
                }
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.addToBot(new MakeTempCardInDiscardAction(new Dazed(), 1));
                }
                break;
            case PANIC_ATTACK:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        switch (this.moveHistory.size() % 4) {
            case 0:
                move = num < 50 ? DREAD_DEBUFF : DESPAIR_DEBUFF;
                break;
            case 1:
                move = this.lastMove(DREAD_DEBUFF) ? DESPAIR_DEBUFF : DREAD_DEBUFF;
                break;
            case 2:
                move = HORROR_ATTACK;
                break;
            case 3:
                move = PANIC_ATTACK;
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case DREAD_DEBUFF:
                this.setMove(MOVES[0], DREAD_DEBUFF, Intent.DEBUFF);
                break;
            case DESPAIR_DEBUFF:
                this.setMove(MOVES[1], DESPAIR_DEBUFF, Intent.DEFEND_DEBUFF);
                break;
            case HORROR_ATTACK:
                this.setMove(MOVES[2], HORROR_ATTACK, Intent.ATTACK_DEBUFF, this.horrorDamage);
                break;
            case PANIC_ATTACK:
                this.setMove(MOVES[3], PANIC_ATTACK, Intent.ATTACK, this.panicDamage);
                break;
        }
    }

    private boolean empowered() {
        return this.hasPower(BackAttackPower.POWER_ID);
    }

    @Override
    public void damage(final DamageInfo info) {
        super.damage(info);
        if (this.currentHealth < this.partner.currentHealth) {
            this.partner.currentHealth = this.currentHealth;
            this.partner.healthBarUpdatedEvent();
        }
    }

    @Override
    public void heal(int healAmount) {
        super.heal(healAmount);
        if (this.currentHealth > this.partner.currentHealth) {
            this.partner.currentHealth = this.currentHealth;
            this.partner.healthBarUpdatedEvent();
        }
    }

    @Override
    public void die() {
        super.die();
        if (!this.partner.isDeadOrEscaped() && !this.partner.isDying) {
            this.partner.die();
        }
    }
}