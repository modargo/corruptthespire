package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.CleansePower;
import corruptthespire.powers.TemporalFissurePower;

public class TemporalPhantasm extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:TemporalPhantasm";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = TemporalPhantasm.monsterStrings.NAME;
    public static final String[] MOVES = TemporalPhantasm.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte IMPERMANENCE_ATTACK = 1;
    private static final byte GRADUAL_DECAY_ATTACK = 2;
    private static final byte TEMPORAL_DISTORTION_ATTACK = 3;
    private static final int IMPERMANENCE_DAMAGE = 4;
    private static final int A4_IMPERMANENCE_DAMAGE = 5;
    private static final int IMPERMANENCE_VULNERABLE = 1;
    private static final int GRADUAL_DECAY_DAMAGE = 3;
    private static final int A4_GRADUAL_DECAY_DAMAGE = 4;
    private static final int GRADUAL_DECAY_HITS = 2;
    private static final int GRADUAL_DECAY_WOUNDS = 1;
    private static final int A19_GRADUAL_DECAY_WOUNDS = 2;
    private static final int TEMPORAL_DISTORTION_DAMAGE = 10;
    private static final int A4_TEMPORAL_DISTORTION_DAMAGE = 12;
    private static final int TEMPORAL_DISTORTION_BLOCK = 15;
    private static final int A9_TEMPORAL_DISTORTION_BLOCK = 20;
    private static final int CLEANSE = 3;
    private static final int A19_CLEANSE = 4;
    private static final int METALLICIZE = 10;
    private static final int A9_METALLICIZE = 15;
    private static final int HP = 60;
    private static final int A9_HP = 70;
    private final int impermanenceDamage;
    private final int gradualDecayDamage;
    private final int gradualDecayWounds;
    private final int temporalDistortionDamage;
    private final int temporalDistortionBlock;
    private final int cleanse;
    private final int metallicize;

    public TemporalPhantasm() {
        this(0.0f, 0.0f);
    }

    public TemporalPhantasm(final float x, final float y) {
        //super(MasterOfTimeAndSpace.NAME, ID, HP, -5.0F, 0, 1370.0f, 775.0f, IMG, x, y);
        super(TemporalPhantasm.NAME, ID, HP, -5.0F, 0, 105.0f, 205.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.temporalDistortionBlock = A9_TEMPORAL_DISTORTION_BLOCK;
            this.metallicize = A9_METALLICIZE;
        } else {
            this.setHp(HP);
            this.temporalDistortionBlock = TEMPORAL_DISTORTION_BLOCK;
            this.metallicize = METALLICIZE;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.impermanenceDamage = A4_IMPERMANENCE_DAMAGE;
            this.gradualDecayDamage = A4_GRADUAL_DECAY_DAMAGE;
            this.temporalDistortionDamage = A4_TEMPORAL_DISTORTION_DAMAGE;
        } else {
            this.impermanenceDamage = IMPERMANENCE_DAMAGE;
            this.gradualDecayDamage = GRADUAL_DECAY_DAMAGE;
            this.temporalDistortionDamage = TEMPORAL_DISTORTION_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.impermanenceDamage));
        this.damage.add(new DamageInfo(this, this.gradualDecayDamage));
        this.damage.add(new DamageInfo(this, this.temporalDistortionDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.gradualDecayWounds = A19_GRADUAL_DECAY_WOUNDS;
            this.cleanse = A19_CLEANSE;
        }
        else {
            this.gradualDecayWounds = GRADUAL_DECAY_WOUNDS;
            this.cleanse = CLEANSE;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, this.metallicize)));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.metallicize));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CleansePower(this, this.cleanse, false)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TemporalFissurePower(this, this.cleanse)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case IMPERMANENCE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, IMPERMANENCE_VULNERABLE, true)));
                break;
            case GRADUAL_DECAY_ATTACK:
                for (int i = 0; i < GRADUAL_DECAY_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Wound(), this.gradualDecayWounds));
                break;
            case TEMPORAL_DISTORTION_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.temporalDistortionBlock));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(TEMPORAL_DISTORTION_ATTACK)) {
            this.setMove(MOVES[0], IMPERMANENCE_ATTACK, Intent.ATTACK_DEBUFF, this.impermanenceDamage);
        }
        else if (this.lastMove(IMPERMANENCE_ATTACK)) {
            this.setMove(MOVES[1], GRADUAL_DECAY_ATTACK, Intent.ATTACK_DEBUFF, this.gradualDecayDamage, GRADUAL_DECAY_HITS, true);
        }
        else {
            this.setMove(MOVES[2], TEMPORAL_DISTORTION_ATTACK, Intent.ATTACK_DEFEND, this.temporalDistortionDamage);
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