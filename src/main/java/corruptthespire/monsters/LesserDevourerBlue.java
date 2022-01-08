package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import corruptthespire.CorruptTheSpire;

public class LesserDevourerBlue extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:LesserDevourerBlue";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = LesserDevourerBlue.monsterStrings.NAME;
    public static final String[] MOVES = LesserDevourerBlue.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte CHOMP_MOVE = 1;
    private static final byte BAD_BREATH_MOVE = 2;
    private static final byte DIGEST_MOVE = 3;
    private static final int CHOMP_DAMAGE = 5;
    private static final int A2_CHOMP_DAMAGE = 6;
    private static final int CHOMP_BLOCK = 6;
    private static final int A7_CHOMP_BLOCK = 8;
    private static final int BAD_BREATH_HEAL = 2;
    private static final int A17_BAD_BREATH_HEAL = 4;
    private static final int DIGEST_DAMAGE = 7;
    private static final int A2_DIGEST_DAMAGE = 8;
    private static final int HP = 30;
    private static final int A7_HP = 35;
    private final int chompDamage;
    private final int chompBlock;
    private final int badBreathHeal;
    private final int digestDamage;

    public LesserDevourerBlue() {
        this(0.0f, 0.0f);
    }

    public LesserDevourerBlue(final float x, final float y) {
        super(LesserDevourerBlue.NAME, ID, HP, -5.0F, 0, 130.0f, 130.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP);
            this.chompBlock = A7_CHOMP_BLOCK;
        } else {
            this.setHp(HP);
            this.chompBlock = CHOMP_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.chompDamage = A2_CHOMP_DAMAGE;
            this.digestDamage = A2_DIGEST_DAMAGE;
        } else {
            this.chompDamage = CHOMP_DAMAGE;
            this.digestDamage = DIGEST_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.chompDamage));
        this.damage.add(new DamageInfo(this, this.digestDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.badBreathHeal = A17_BAD_BREATH_HEAL;
        }
        else {
            this.badBreathHeal = BAD_BREATH_HEAL;
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
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SHIELD));
                this.addToBot(new GainBlockAction(this, this.chompBlock));
                break;
            case BAD_BREATH_MOVE:
                this.addToBot(new FastShakeAction(this, 0.3F, 0.1F));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.isDead) {
                        this.addToBot(new HealAction(m, this, this.badBreathHeal));
                    }
                }
                break;
            case DIGEST_MOVE:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(CHOMP_MOVE)) {
            this.setMove(MOVES[1], BAD_BREATH_MOVE, Intent.BUFF);
        }
        else if (this.lastMove(BAD_BREATH_MOVE)) {
            this.setMove(MOVES[2], DIGEST_MOVE, Intent.ATTACK, this.digestDamage);
        }
        else {
            this.setMove(MOVES[0], CHOMP_MOVE, Intent.ATTACK_DEFEND, this.chompDamage);
        }
    }

    @Override
    public void die() {
        super.die();
        TranscendentDevourer.triggerOnLesserDevourerDeath(this);
    }
}