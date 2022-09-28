package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.RitualPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.CorruptingRot;
import corruptthespire.powers.EssenceOfRotPower;

public class RottingShambler extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:RottingShambler";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = RottingShambler.monsterStrings.NAME;
    public static final String[] MOVES = RottingShambler.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte BASH_ATTACK = 1;
    private static final byte NOURISHING_CORRUPTION_MOVE = 2;
    private static final byte ROT_TOUCH_ATTACK = 3;
    private static final int BASH_DAMAGE = 11;
    private static final int A2_BASH_DAMAGE = 12;
    private static final int NOURISHING_CORRUPTION_BLOCK = 4;
    private static final int A7_NOURISHING_CORRUPTION_BLOCK = 6;
    private static final int NOURISHING_CORRUPTION_RITUAL = 1;
    private static final int A17_NOURISHING_CORRUPTION_RITUAL = 1;
    private static final int ROT_TOUCH_DAMAGE = 3;
    private static final int A2_ROT_TOUCH_DAMAGE = 4;
    private static final int CORRUPTING_ROTS = 2;
    private static final int A17_CORRUPTING_ROTS = 3;
    private static final int MIN_HP = 62;
    private static final int MAX_HP = 64;
    private static final int A7_MIN_HP = 65;
    private static final int A7_MAX_HP = 67;
    private final int bashDamage;
    private final int nourishingCorruptionBlock;
    private final int nourishingCorruptionRitual;
    private final int rotTouchDamage;
    private final int corruptingRots;

    private boolean usedNourishingCorruption = false;
    private int rotTouchCount = 0;

    public RottingShambler() {
        this(0.0f, 0.0f);
    }

    public RottingShambler(final float x, final float y) {
        super(RottingShambler.NAME, ID, MIN_HP, -5.0F, 0, 190.0f, 235.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_MIN_HP, A7_MAX_HP);
            this.nourishingCorruptionBlock = A7_NOURISHING_CORRUPTION_BLOCK;
        } else {
            this.setHp(MIN_HP, MAX_HP);
            this.nourishingCorruptionBlock = NOURISHING_CORRUPTION_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.bashDamage = A2_BASH_DAMAGE;
            this.rotTouchDamage = A2_ROT_TOUCH_DAMAGE;
        } else {
            this.bashDamage = BASH_DAMAGE;
            this.rotTouchDamage = ROT_TOUCH_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.bashDamage));
        this.damage.add(new DamageInfo(this, this.rotTouchDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.nourishingCorruptionRitual = A17_NOURISHING_CORRUPTION_RITUAL;
            this.corruptingRots = A17_CORRUPTING_ROTS;
        }
        else {
            this.nourishingCorruptionRitual = NOURISHING_CORRUPTION_RITUAL;
            this.corruptingRots = CORRUPTING_ROTS;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new EssenceOfRotPower(this, this.corruptingRots)));
        this.addToBot(new MakeTempCardInDrawPileAction(new CorruptingRot(), this.corruptingRots, true, true));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case BASH_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case NOURISHING_CORRUPTION_MOVE:
                this.addToBot(new FastShakeAction(this, 0.3F, 0.1F));
                this.addToBot(new GainBlockAction(this, this.nourishingCorruptionBlock));
                this.addToBot(new ApplyPowerAction(this, this, new RitualPower(this, this.nourishingCorruptionRitual, false)));
                this.usedNourishingCorruption = true;
                break;
            case ROT_TOUCH_ATTACK:
                for (int i = 0; i < this.getRotTouchHits(); i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.POISON));
                }
                this.rotTouchCount++;
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || (AbstractDungeon.ascensionLevel < 17 && this.lastMove(ROT_TOUCH_ATTACK) && this.lastMoveBefore(ROT_TOUCH_ATTACK))) {
            this.setMove(MOVES[0], BASH_ATTACK, Intent.ATTACK, this.bashDamage);
        }
        else if (!this.usedNourishingCorruption) {
            this.setMove(MOVES[1], NOURISHING_CORRUPTION_MOVE, Intent.DEFEND_BUFF);
        }
        else {
            this.setMove(MOVES[2], ROT_TOUCH_ATTACK, Intent.ATTACK, this.rotTouchDamage, this.getRotTouchHits(), true);
        }
    }

    private int getRotTouchHits() {
        return this.rotTouchCount + 2;
    }
}