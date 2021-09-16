package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.DragonsHeartPower;

public class TreasureWardenBlack extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:TreasureWardenBlack";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private final Version version;
    private static final byte SEARING_BREATH_ATTACK = 1;
    private static final byte ACID_BREATH_ATTACK = 2;
    private static final byte BLACK_BREATH_ATTACK = 3;
    private static final int[] SEARING_BREATH_DAMAGE = { 2, 3, 4 };
    private static final int[] A3_SEARING_BREATH_DAMAGE = { 2, 4, 5 };
    private static final int SEARING_BREATH_HITS = 3;
    private static final int[] SEARING_BREATH_BURNS = { 0, 1, 1 };
    private static final int[] A18_SEARING_BREATH_BURNS = { 1, 2, 2 };
    private static final int[] ACID_BREATH_DAMAGE = { 3, 5, 7 };
    private static final int[] A3_ACID_BREATH_DAMAGE = { 4, 6, 8 };
    private static final int ACID_BREATH_HITS = 2;
    private static final int ACID_BREATH_WOUNDS = 1;
    private static final int A18_ACID_BREATH_WOUNDS = 2;
    private static final int[] BLACK_BREATH_DAMAGE = { 9, 13, 17 };
    private static final int[] A3_BLACK_BREATH_DAMAGE = { 10, 15, 19 };
    private static final int[] BLACK_BREATH_DRAW_VOIDS = { 0, 1, 1 };
    private static final int[] A18_BLACK_BREATH_DRAW_VOIDS = { 1, 1, 1};
    private static final int[] BLACK_BREATH_DISCARD_VOIDS = { 1, 0, 0 };
    private static final int[] A18_BLACK_BREATH_DISCARD_VOIDS = { 0, 0, 1};
    private static final int[] DRAGONS_HEART_AMOUNT = { 2, 3, 4 };
    private static final int[] A18_DRAGONS_HEART_AMOUNT = { 3, 4, 5 };
    private static final int[] HP_MIN = { 55, 92, 166 };
    private static final int[] HP_MAX = { 58, 96, 171 };
    private static final int[] A8_HP_MIN = { 57, 95, 170 };
    private static final int[] A8_HP_MAX = { 60, 99, 175 };
    private final int searingBreathDamage;
    private final int searingBreathBurns;
    private final int acidBreathWounds;
    private final int acidBreathDamage;
    private final int blackBreathDamage;
    private final int blackBreathDrawVoids;
    private final int blackBreathDiscardVoids;
    private final int dragonsHeartAmount;

    public TreasureWardenBlack() {
        this(0.0f, 0.0f, Version.Act1);
    }

    public TreasureWardenBlack(final float x, final float y, Version version) {
        super(TreasureWardenBlack.NAME, ID, HP_MAX[0], -5.0F, 0, 480.0f, 450.0f, IMG, x, y);
        this.version = version;
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(this.v(A8_HP_MIN), this.v(A8_HP_MAX));
        } else {
            this.setHp(this.v(HP_MIN), this.v(HP_MAX));
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.searingBreathDamage = this.v(A3_SEARING_BREATH_DAMAGE);
            this.acidBreathDamage = this.v(A3_ACID_BREATH_DAMAGE);
            this.blackBreathDamage = this.v(A3_BLACK_BREATH_DAMAGE);
        } else {
            this.searingBreathDamage = this.v(SEARING_BREATH_DAMAGE);
            this.acidBreathDamage = this.v(ACID_BREATH_DAMAGE);
            this.blackBreathDamage = this.v(BLACK_BREATH_DAMAGE);
        }
        this.damage.add(new DamageInfo(this, this.searingBreathDamage));
        this.damage.add(new DamageInfo(this, this.acidBreathDamage));
        this.damage.add(new DamageInfo(this, this.blackBreathDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.searingBreathBurns = this.v(A18_SEARING_BREATH_BURNS);
            this.acidBreathWounds = A18_ACID_BREATH_WOUNDS;
            this.dragonsHeartAmount = this.v(A18_DRAGONS_HEART_AMOUNT);
            this.blackBreathDrawVoids = this.v(A18_BLACK_BREATH_DRAW_VOIDS);
            this.blackBreathDiscardVoids = this.v(A18_BLACK_BREATH_DISCARD_VOIDS);
        }
        else {
            this.searingBreathBurns = this.v(SEARING_BREATH_BURNS);
            this.acidBreathWounds = ACID_BREATH_WOUNDS;
            this.dragonsHeartAmount = this.v(DRAGONS_HEART_AMOUNT);
            this.blackBreathDrawVoids = this.v(BLACK_BREATH_DRAW_VOIDS);
            this.blackBreathDiscardVoids = this.v(BLACK_BREATH_DISCARD_VOIDS);
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new DragonsHeartPower(this, this.dragonsHeartAmount), this.dragonsHeartAmount));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case SEARING_BREATH_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                for (int i = 0; i < SEARING_BREATH_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                if (this.searingBreathBurns > 0) {
                    this.addToBot(new MakeTempCardInDiscardAction(new Burn(), this.searingBreathBurns));
                }
                break;
            case ACID_BREATH_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                for (int i = 0; i < ACID_BREATH_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                if (this.acidBreathWounds > 0) {
                    this.addToBot(new MakeTempCardInDiscardAction(new Wound(), this.acidBreathWounds));
                }
                break;
            case BLACK_BREATH_ATTACK:
                this.addToBot(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                if (this.blackBreathDrawVoids > 0) {
                    this.addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), this.blackBreathDrawVoids, true, true));
                }
                if (this.blackBreathDiscardVoids > 0) {
                    this.addToBot(new MakeTempCardInDiscardAction(new VoidCard(), this.blackBreathDiscardVoids));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        switch (this.moveHistory.size() % 4) {
            case 0:
            case 2:
                move = SEARING_BREATH_ATTACK;
                break;
            case 1:
                move = ACID_BREATH_ATTACK;
                break;
            case 3:
                move = BLACK_BREATH_ATTACK;
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case SEARING_BREATH_ATTACK:
                this.setMove(MOVES[0], SEARING_BREATH_ATTACK, this.searingBreathBurns > 0 ? Intent.ATTACK_DEBUFF : Intent.ATTACK, this.searingBreathDamage, SEARING_BREATH_HITS, true);
                break;
            case ACID_BREATH_ATTACK:
                this.setMove(MOVES[1], ACID_BREATH_ATTACK, Intent.ATTACK_DEBUFF, this.acidBreathDamage, ACID_BREATH_HITS, true);
                break;
            case BLACK_BREATH_ATTACK:
                this.setMove(MOVES[2], BLACK_BREATH_ATTACK, Intent.ATTACK_DEBUFF, this.blackBreathDamage);
                break;
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