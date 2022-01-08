package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.WeakPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.BadBreath;

public class LesserDevourerGreen extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:LesserDevourerGreen";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = LesserDevourerGreen.monsterStrings.NAME;
    public static final String[] MOVES = LesserDevourerGreen.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte CHOMP_MOVE = 1;
    private static final byte BAD_BREATH_MOVE = 2;
    private static final byte DIGEST_MOVE = 3;
    private static final int CHOMP_DAMAGE = 5;
    private static final int A2_CHOMP_DAMAGE = 6;
    private static final int CHOMP_DAZED = 1;
    private static final int BAD_BREATH_STATUSES = 1;
    private static final int DIGEST_BLOCK = 6;
    private static final int A7_DIGEST_BLOCK = 8;
    private static final int DIGEST_WEAK = 1;
    private static final int HP = 30;
    private static final int A7_HP = 35;
    private final int chompDamage;
    private final int digestBlock;

    public LesserDevourerGreen() {
        this(0.0f, 0.0f);
    }

    public LesserDevourerGreen(final float x, final float y) {
        super(LesserDevourerGreen.NAME, ID, HP, -5.0F, 0, 130.0f, 130.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A7_HP);
            this.digestBlock = A7_DIGEST_BLOCK;
        } else {
            this.setHp(HP);
            this.digestBlock = DIGEST_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.chompDamage = A2_CHOMP_DAMAGE;
        } else {
            this.chompDamage = CHOMP_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.chompDamage));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CHOMP_MOVE:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), CHOMP_DAZED, true, true));
                }
                else {
                    this.addToBot(new MakeTempCardInDiscardAction(new Dazed(), CHOMP_DAZED));
                }
                break;
            case BAD_BREATH_MOVE:
                this.addToBot(new FastShakeAction(this, 0.3F, 0.1F));
                this.addToBot(new MakeTempCardInHandAction(new BadBreath(), BAD_BREATH_STATUSES));
                break;
            case DIGEST_MOVE:
                this.addToBot(new GainBlockAction(this, this.digestBlock));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, DIGEST_WEAK, true)));
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(CHOMP_MOVE)) {
            this.setMove(MOVES[1], BAD_BREATH_MOVE, Intent.DEBUFF);
        }
        else if (this.lastMove(BAD_BREATH_MOVE)) {
            this.setMove(MOVES[2], DIGEST_MOVE, Intent.DEFEND_DEBUFF);
        }
        else {
            this.setMove(MOVES[0], CHOMP_MOVE, Intent.ATTACK_DEBUFF, this.chompDamage);
        }
    }

    @Override
    public void die() {
        super.die();
        TranscendentDevourer.triggerOnLesserDevourerDeath(this);
    }
}