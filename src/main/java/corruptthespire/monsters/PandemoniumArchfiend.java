package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.DevilsDueAction;
import corruptthespire.cards.Bedeviled;
import corruptthespire.powers.DevilsDuePower;
import corruptthespire.powers.PriceOfKnowledgePower;
import corruptthespire.powers.TormentingShacklesPower;

public class PandemoniumArchfiend extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:PandemoniumArchfiend";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = PandemoniumArchfiend.monsterStrings.NAME;
    public static final String[] MOVES = PandemoniumArchfiend.monsterStrings.MOVES;
    public static final String[] DIALOG = PandemoniumArchfiend.monsterStrings.DIALOG;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private boolean usedInfernalPower = false;
    private static final byte MINDSHATTER_DEBUFF = 1;
    private static final byte FOUR_ARM_STRIKE_ATTACK = 2;
    private static final byte TORMENTING_SHACKLES_ATTACK = 3;
    private static final byte INFERNAL_POWER_ATTACK = 4;
    private static final byte HELLFIRE_ATTACK = 5;
    private static final int DEVILS_DUE_STATUSES = 5;
    private static final int MINDSHATTER_AMOUNT = 1;
    private static final int A19_MINDSHATTER_AMOUNT = 2;
    private static final int FOUR_ARM_STRIKE_DAMAGE = 3;
    private static final int A4_FOUR_ARM_STRIKE_DAMAGE = 4;
    private static final int FOUR_ARM_STRIKE_HITS = 4;
    private static final int TORMENTING_SHACKLES_DAMAGE = 30;
    private static final int A4_TORMENTING_SHACKLES_DAMAGE = 33;
    private static final int TORMENTING_SHACKLES_AMOUNT = 1;
    private static final int INFERNAL_POWER_DAMAGE = 22;
    private static final int A4_INFERNAL_POWER_DAMAGE = 24;
    private static final int INFERNAL_POWER_RITUAL = 1;
    private static final int A19_INFERNAL_POWER_RITUAL = 2;
    private static final int HELLFIRE_DAMAGE = 12;
    private static final int A4_HELLFIRE_DAMAGE = 14;
    private static final int HELLFIRE_HITS = 2;
    private static final int HP = 425;
    private static final int A9_HP = 450;
    private final int mindshatterAmount;
    private final int fourArmStrikeDamage;
    private final int tormentingShacklesDamage;
    private final int infernalPowerDamage;
    private final int infernalPowerRitual;
    private final int hellfireDamage;

    public PandemoniumArchfiend() {
        this(0.0f, 0.0f);
    }

    public PandemoniumArchfiend(final float x, final float y) {
        super(PandemoniumArchfiend.NAME, ID, HP, -5.0F, 0, 425.0f, 555.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.fourArmStrikeDamage = A4_FOUR_ARM_STRIKE_DAMAGE;
            this.tormentingShacklesDamage = A4_TORMENTING_SHACKLES_DAMAGE;
            this.infernalPowerDamage = A4_INFERNAL_POWER_DAMAGE;
            this.hellfireDamage = A4_HELLFIRE_DAMAGE;
        } else {
            this.fourArmStrikeDamage = FOUR_ARM_STRIKE_DAMAGE;
            this.tormentingShacklesDamage = TORMENTING_SHACKLES_DAMAGE;
            this.infernalPowerDamage = INFERNAL_POWER_DAMAGE;
            this.hellfireDamage = HELLFIRE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.fourArmStrikeDamage));
        this.damage.add(new DamageInfo(this, this.tormentingShacklesDamage));
        this.damage.add(new DamageInfo(this, this.infernalPowerDamage));
        this.damage.add(new DamageInfo(this, this.hellfireDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.mindshatterAmount = A19_MINDSHATTER_AMOUNT;
            this.infernalPowerRitual = A19_INFERNAL_POWER_RITUAL;
        }
        else {
            this.mindshatterAmount = MINDSHATTER_AMOUNT;
            this.infernalPowerRitual = INFERNAL_POWER_RITUAL;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

        this.addToBot(new ApplyPowerAction(this, this, new DevilsDuePower(this, DEVILS_DUE_STATUSES, AbstractDungeon.ascensionLevel >= 19)));
        this.addToBot(new DevilsDueAction());
        this.addToBot(new MakeTempCardInDrawPileAction(new Bedeviled(), DEVILS_DUE_STATUSES, true, true));
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.addToBot(new MakeTempCardInDiscardAction(new Bedeviled(), DEVILS_DUE_STATUSES));
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case MINDSHATTER_DEBUFF:
                this.addToBot(new FastShakeAction(this, 0.5F, 0.2F));
                for (int i = 0; i < this.mindshatterAmount; i++) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new PriceOfKnowledgePower(AbstractDungeon.player, 1), 1));
                }
                break;
            case FOUR_ARM_STRIKE_ATTACK:
                for (int i = 0; i < FOUR_ARM_STRIKE_HITS; i++) {
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            case TORMENTING_SHACKLES_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new TormentingShacklesPower(AbstractDungeon.player, TORMENTING_SHACKLES_AMOUNT), TORMENTING_SHACKLES_AMOUNT));
                break;
            case INFERNAL_POWER_ATTACK:
                this.usedInfernalPower = true;
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.infernalPowerRitual), this.infernalPowerRitual));
                this.addToBot(new ApplyPowerAction(this, this, new RitualPower(this, this.infernalPowerRitual, false), this.infernalPowerRitual));
                if (MathUtils.randomBoolean(0.25F)) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[MathUtils.random(DIALOG.length - 1)], 0.5F, 3.0F));
                }
                break;
            case HELLFIRE_ATTACK:
                for (int i = 0; i < HELLFIRE_HITS; i++) {
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.FIRE));
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
                move = this.firstMove || num < 50 ? MINDSHATTER_DEBUFF : TORMENTING_SHACKLES_ATTACK;
                break;
            case 1:
                move = FOUR_ARM_STRIKE_ATTACK;
                break;
            case 2:
                move = MonsterUtil.lastMoveX(this, MINDSHATTER_DEBUFF, 2) ? TORMENTING_SHACKLES_ATTACK : MINDSHATTER_DEBUFF;
                break;
            case 3:
                move = !this.usedInfernalPower ? INFERNAL_POWER_ATTACK : HELLFIRE_ATTACK;
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case MINDSHATTER_DEBUFF:
                this.setMove(MOVES[0], MINDSHATTER_DEBUFF, Intent.STRONG_DEBUFF);
                break;
            case FOUR_ARM_STRIKE_ATTACK:
                this.setMove(MOVES[1], FOUR_ARM_STRIKE_ATTACK, Intent.ATTACK, this.fourArmStrikeDamage, FOUR_ARM_STRIKE_HITS, true);
                break;
            case TORMENTING_SHACKLES_ATTACK:
                this.setMove(MOVES[2], TORMENTING_SHACKLES_ATTACK, Intent.ATTACK_DEBUFF, this.tormentingShacklesDamage);
                break;
            case INFERNAL_POWER_ATTACK:
                this.setMove(MOVES[3], INFERNAL_POWER_ATTACK, Intent.ATTACK_BUFF, this.infernalPowerDamage);
                break;
            case HELLFIRE_ATTACK:
                this.setMove(MOVES[4], HELLFIRE_ATTACK, Intent.ATTACK, this.hellfireDamage, HELLFIRE_HITS, true);
                break;
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