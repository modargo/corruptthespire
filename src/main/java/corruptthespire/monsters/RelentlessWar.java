package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.PowerUtil;
import corruptthespire.powers.SoulLinkPower;
import corruptthespire.powers.ThreatenedPower;

public class RelentlessWar extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:RelentlessWar";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = RelentlessWar.monsterStrings.NAME;
    public static final String[] MOVES = RelentlessWar.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte BLOODSHED_ATTACK = 1;
    private static final byte SLAUGHTER_ATTACK = 2;
    private static final byte ENMITY_ATTACK = 3;
    private static final byte CHALLENGE_MOVE = 4;
    private static final int BLOODSHED_DAMAGE = 10;
    private static final int A4_BLOODSHED_DAMAGE = 12;
    private static final int SLAUGHTER_DAMAGE = 6;
    private static final int A4_SLAUGHTER_DAMAGE = 7;
    private static final int SLAUGHTER_HITS = 2;
    private static final int ENMITY_DAMAGE = 7;
    private static final int A4_ENMITY_DAMAGE = 8;
    private static final int ENMITY_STRENGTH = 1;
    private static final int A19_ENMITY_STRENGTH = 2;
    private static final int ENMITY_EXTRA_STRENGTH = 1;
    private static final int A19_ENMITY_EXTRA_STRENGTH = 2;
    private static final int CHALLENGE_BLOCK = 15;
    private static final int A9_CHALLENGE_BLOCK = 20;
    private static final int CHALLENGE_PLATED_ARMOR = 5;
    private static final int A19_CHALLENGE_PLATED_ARMOR = 5;
    private static final int CHALLENGE_ARTIFACT = 1;
    private static final int A19_CHALLENGE_ARTIFACT = 1;
    private static final int HP = 375;
    private static final int A9_HP = 400;
    private final int bloodshedDamage;
    private final int slaughterDamage;
    private final int enmityDamage;
    private final int enmityStrength;
    private final int enmityExtraStrength;
    private final int challengeBlock;
    private final int challengePlatedArmor;
    private final int challengeArtifact;

    private AbstractMonster partner;

    public RelentlessWar() {
        this(0.0f, 0.0f);
    }

    public RelentlessWar(final float x, final float y) {
        super(RelentlessWar.NAME, ID, HP, -5.0F, 0, 255.0f, 260.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.challengeBlock = A9_CHALLENGE_BLOCK;
        } else {
            this.setHp(HP);
            this.challengeBlock = CHALLENGE_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.bloodshedDamage = A4_BLOODSHED_DAMAGE;
            this.slaughterDamage = A4_SLAUGHTER_DAMAGE;
            this.enmityDamage = A4_ENMITY_DAMAGE;
        } else {
            this.bloodshedDamage = BLOODSHED_DAMAGE;
            this.slaughterDamage = SLAUGHTER_DAMAGE;
            this.enmityDamage = ENMITY_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.bloodshedDamage));
        this.damage.add(new DamageInfo(this, this.slaughterDamage));
        this.damage.add(new DamageInfo(this, this.enmityDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.enmityStrength = A19_ENMITY_STRENGTH;
            this.enmityExtraStrength = A19_ENMITY_EXTRA_STRENGTH;
            this.challengePlatedArmor = A19_CHALLENGE_PLATED_ARMOR;
            this.challengeArtifact = A19_CHALLENGE_ARTIFACT;
        }
        else {
            this.enmityStrength = ENMITY_STRENGTH;
            this.enmityExtraStrength = ENMITY_EXTRA_STRENGTH;
            this.challengePlatedArmor = CHALLENGE_PLATED_ARMOR;
            this.challengeArtifact = CHALLENGE_ARTIFACT;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");
        this.partner = AbstractDungeon.getMonsters().getMonster(EternalFear.ID);
        if (this.partner == null) {
            throw new RuntimeException("Relentless War and Eternal Fear must be encountered together.");
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SoulLinkPower(this, this.partner)));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SurroundedPower(AbstractDungeon.player)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ThreatenedPower(AbstractDungeon.player)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case BLOODSHED_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case SLAUGHTER_ATTACK:
                for (int i = 0; i < SLAUGHTER_HITS; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                break;
            case ENMITY_ATTACK:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                int strength = this.enmityStrength + (this.empowered() ? this.enmityExtraStrength : 0);
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                        if (m == this || m.drawX < this.drawX || m.getIntentDmg() == -1) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, strength)));
                        }
                        else {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, PowerUtil.gainStrengthBuff(m, strength)));
                        }
                    }
                }
                break;
            case CHALLENGE_MOVE:
                this.addToBot(new FastShakeAction(this, 0.5F, 0.2F));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                        this.addToBot(new GainBlockAction(m, this.challengeBlock));
                        this.addToBot(new ApplyPowerAction(m, this, new ArtifactPower(m, this.challengeArtifact)));
                        if (this.empowered()) {
                            this.addToBot(new ApplyPowerAction(m, this, new PlatedArmorPower(m, this.challengePlatedArmor)));
                        }
                    }
                }
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        switch (this.moveHistory.size() % 4) {
            case 0:
                move =num < 50 ? BLOODSHED_ATTACK : SLAUGHTER_ATTACK;
                break;
            case 1:
                move = this.lastMove(BLOODSHED_ATTACK) ? SLAUGHTER_ATTACK : BLOODSHED_ATTACK;
                break;
            case 2:
                move = ENMITY_ATTACK;
                break;
            case 3:
                move = CHALLENGE_MOVE;
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case BLOODSHED_ATTACK:
                this.setMove(MOVES[0], BLOODSHED_ATTACK, Intent.ATTACK, this.bloodshedDamage);
                break;
            case SLAUGHTER_ATTACK:
                this.setMove(MOVES[1], SLAUGHTER_ATTACK, Intent.ATTACK, this.slaughterDamage, SLAUGHTER_HITS, true);
                break;
            case ENMITY_ATTACK:
                this.setMove(MOVES[2], ENMITY_ATTACK, Intent.ATTACK_BUFF, this.enmityDamage);
                break;
            case CHALLENGE_MOVE:
                this.setMove(MOVES[3], CHALLENGE_MOVE, Intent.DEFEND_BUFF);
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
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.onBossVictoryLogic();
        }
    }
}