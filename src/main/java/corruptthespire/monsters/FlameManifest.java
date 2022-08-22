package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.FieryDemisePower;

public class FlameManifest extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:FlameManifest";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private final Version version;
    private static final byte BIG_BURN_DEBUFF = 1;
    private static final byte SMALL_BURN_ATTACK = 2;
    private static final byte FIERCE_BURN_ATTACK = 3;
    private static final int[] BIG_BURN_BURNS = { 1, 2, 3 };
    private static final int[] A17_BIG_BURN_BURNS = { 2, 3, 4 };
    private static final int[] SMALL_BURN_DAMAGE = { 2, 4, 6 };
    private static final int[] A2_SMALL_BURN_DAMAGE = { 3, 5, 7 };
    private static final int FIERCE_BURN_DAMAGE = 2;
    private static final int A2_FIERCE_BURN_DAMAGE = 3;
    private static final int[] FIERCE_BURN_HITS = { 2, 3, 4 };
    private static final int BURNS_ON_DEATH = 1;
    private static final int[] HP_MIN = { 13, 20, 27 };
    private static final int[] HP_MAX = { 15, 22, 29 };
    private static final int[] A7_HP_MIN = { 14, 22, 30 };
    private static final int[] A7_HP_MAX = { 16, 24, 32 };
    private final int bigBurnBurns;
    private final int smallBurnDamage;
    private final int fierceBurnDamage;

    public FlameManifest() {
        this(0.0f, 0.0f, Version.Act1);
    }

    public FlameManifest(final float x, final float y, Version version) {
        super(FlameManifest.NAME, ID, HP_MAX[0], -5.0F, 0, 155.0f, 180.0f, IMG, x, y);
        this.version = version;
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(this.v(A7_HP_MIN), this.v(A7_HP_MAX));
        } else {
            this.setHp(this.v(HP_MIN), this.v(HP_MAX));
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.smallBurnDamage = this.v(A2_SMALL_BURN_DAMAGE);
            this.fierceBurnDamage = A2_FIERCE_BURN_DAMAGE;
        } else {
            this.smallBurnDamage = this.v(SMALL_BURN_DAMAGE);
            this.fierceBurnDamage = FIERCE_BURN_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.smallBurnDamage));
        this.damage.add(new DamageInfo(this, this.fierceBurnDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.bigBurnBurns = this.v(A17_BIG_BURN_BURNS);
        }
        else {
            this.bigBurnBurns = this.v(BIG_BURN_BURNS);
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new FieryDemisePower(this, BURNS_ON_DEATH)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case BIG_BURN_DEBUFF:
                this.addToBot(new AnimateShakeAction(this, 0.3F, 0.1F));
                int drawBurns = this.bigBurnBurns / 2;
                int discardBurns = (this.bigBurnBurns + 1) / 2;
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), drawBurns, true, true));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), discardBurns));
                break;
            case SMALL_BURN_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case FIERCE_BURN_ATTACK:
                this.addToBot(new AnimateSlowAttackAction(this));
                for (int i = 0; i < this.v(FIERCE_BURN_HITS); i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(MOVES[0], BIG_BURN_DEBUFF, Intent.DEBUFF);
        }
        else if (this.lastMove(BIG_BURN_DEBUFF) || this.lastMove(FIERCE_BURN_ATTACK)) {
            this.setMove(MOVES[1], SMALL_BURN_ATTACK, Intent.ATTACK, this.smallBurnDamage);
        }
        else {
            this.setMove(MOVES[2], FIERCE_BURN_ATTACK, Intent.ATTACK, this.fierceBurnDamage, this.v(FIERCE_BURN_HITS), true);
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