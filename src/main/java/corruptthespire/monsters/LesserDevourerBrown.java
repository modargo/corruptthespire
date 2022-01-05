package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;

public class LesserDevourerBrown extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:LesserDevourerBrown";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = LesserDevourerBrown.monsterStrings.NAME;
    public static final String[] MOVES = LesserDevourerBrown.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte CHOMP_MOVE = 1;
    private static final byte BAD_BREATH_MOVE = 2;
    private static final byte DIGEST_MOVE = 3;
    private static final int CHOMP_DAMAGE = 5;
    private static final int A3_CHOMP_DAMAGE = 6;
    private static final int CHOMP_STRENGTH = 1;
    private static final int A18_CHOMP_STRENGTH = 2;
    private static final int BAD_BREATH_DAMAGE = 10;
    private static final int A3_BAD_BREATH_DAMAGE = 12;
    private static final int DIGEST_DAMAGE = 2;
    private static final int A3_DIGEST_DAMAGE = 3;
    private static final int DIGEST_HITS = 2;
    private static final int HP = 40;
    private static final int A8_HP = 45;
    private final int chompDamage;
    private final int chompStrength;
    private final int badBreathDamage;
    private final int digestDamage;

    public LesserDevourerBrown() {
        this(0.0f, 0.0f);
    }

    public LesserDevourerBrown(final float x, final float y) {
        super(LesserDevourerBrown.NAME, ID, HP, -5.0F, 0, 130.0f, 130.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.chompDamage = A3_CHOMP_DAMAGE;
            this.badBreathDamage = A3_BAD_BREATH_DAMAGE;
            this.digestDamage = A3_DIGEST_DAMAGE;
        } else {
            this.chompDamage = CHOMP_DAMAGE;
            this.badBreathDamage = BAD_BREATH_DAMAGE;
            this.digestDamage = DIGEST_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.chompDamage));
        this.damage.add(new DamageInfo(this, this.badBreathDamage));
        this.damage.add(new DamageInfo(this, this.digestDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.chompStrength = A18_CHOMP_STRENGTH;
        }
        else {
            this.chompStrength = CHOMP_STRENGTH;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CHOMP_MOVE:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.chompStrength)));
                break;
            case BAD_BREATH_MOVE:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case DIGEST_MOVE:
                for (int i = 0; i < DIGEST_HITS; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.POISON));
                }
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(CHOMP_MOVE)) {
            this.setMove(MOVES[1], BAD_BREATH_MOVE, Intent.ATTACK, this.badBreathDamage);
        }
        else if (this.lastMove(BAD_BREATH_MOVE)) {
            this.setMove(MOVES[2], DIGEST_MOVE, Intent.ATTACK, this.digestDamage);
        }
        else {
            this.setMove(MOVES[0], CHOMP_MOVE, Intent.ATTACK_BUFF, this.chompDamage);
        }
    }

    @Override
    public void die() {
        super.die();
        TranscendentDevourer.triggerOnLesserDevourerDeath(this);
    }
}