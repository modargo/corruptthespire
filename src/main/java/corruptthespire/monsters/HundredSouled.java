package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.Revelation;
import corruptthespire.powers.MalicePower;

public class HundredSouled extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:HundredSouled";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = HundredSouled.monsterStrings.NAME;
    public static final String[] MOVES = HundredSouled.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte REVEAL_THE_HUNDRED_ATTACK = 1;
    private static final byte WITNESS_ATTACK = 2;
    private static final byte RAPTURE_ATTACK = 3;
    private static final int REVEAL_THE_HUNDRED_DAMAGE = 6;
    private static final int A2_REVEAL_THE_HUNDRED_DAMAGE = 7;
    private static final int REVEAL_THE_HUNDRED_STATUSES = 3;
    private static final int REVEAL_THE_HUNDRED_HITS = 4;
    private static final int WITNESS_DAMAGE = 10;
    private static final int A2_WITNESS_DAMAGE = 12;
    private static final int WITNESS_STATUSES = 1;
    private static final int WITNESS_HITS = 2;
    private static final int RAPTURE_DAMAGE = 32;
    private static final int A2_RAPTURE_DAMAGE = 35;
    private static final int MALICE = 1;
    private static final int A17_MALICE = 2;
    private static final int MIN_HP = 190;
    private static final int MAX_HP = 190;
    private static final int A7_MIN_HP = 210;
    private static final int A7_MAX_HP = 210;
    private final int revealTheHundredDamage;
    private final int witnessDamage;
    private final int raptureDamage;
    private final int malice;

    public HundredSouled() {
        this(0.0f, 0.0f);
    }

    public HundredSouled(final float x, final float y) {
        super(HundredSouled.NAME, ID, MIN_HP, -5.0F, 0, 255.0f, 295.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_MIN_HP, A7_MAX_HP);
        } else {
            this.setHp(MIN_HP, MAX_HP);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.revealTheHundredDamage = A2_REVEAL_THE_HUNDRED_DAMAGE;
            this.witnessDamage = A2_WITNESS_DAMAGE;
            this.raptureDamage = A2_RAPTURE_DAMAGE;
        } else {
            this.revealTheHundredDamage = REVEAL_THE_HUNDRED_DAMAGE;
            this.witnessDamage = WITNESS_DAMAGE;
            this.raptureDamage = RAPTURE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.revealTheHundredDamage));
        this.damage.add(new DamageInfo(this, this.witnessDamage));
        this.damage.add(new DamageInfo(this, this.raptureDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.malice = A17_MALICE;
        }
        else {
            this.malice = MALICE;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MalicePower(this, this.malice)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case REVEAL_THE_HUNDRED_ATTACK:
                for (int i = 0; i < REVEAL_THE_HUNDRED_HITS; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                this.addToBot(new MakeTempCardInDrawPileAction(new Revelation(), REVEAL_THE_HUNDRED_STATUSES, true, true));
                break;
            case WITNESS_ATTACK:
                for (int i = 0; i < WITNESS_HITS; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                this.addToBot(new MakeTempCardInDrawPileAction(new Revelation(), WITNESS_STATUSES, false, true));
                break;
            case RAPTURE_ATTACK:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SMASH));
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(RAPTURE_ATTACK)) {
            this.setMove(MOVES[0], REVEAL_THE_HUNDRED_ATTACK, Intent.ATTACK_DEBUFF, this.revealTheHundredDamage, REVEAL_THE_HUNDRED_HITS, true);
        }
        else if (this.lastMove(REVEAL_THE_HUNDRED_ATTACK)) {
            this.setMove(MOVES[1], WITNESS_ATTACK, Intent.ATTACK_DEBUFF, this.witnessDamage, WITNESS_HITS, true);
        }
        else {
            this.setMove(MOVES[2], RAPTURE_ATTACK, Intent.ATTACK, this.raptureDamage);
        }
    }
}