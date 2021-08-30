package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.AstralCoreCounterPower;
import corruptthespire.powers.AstralCorePower;
import corruptthespire.powers.EldritchGraspPower;

public class Harbinger extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:Harbinger";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = Harbinger.monsterStrings.NAME;
    public static final String[] MOVES = Harbinger.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte COSMIC_BURST_ATTACK = 1;
    private static final byte ALIEN_CLAWS_ATTACK = 2;
    private static final int COSMIC_BURST_DAMAGE = 18;
    private static final int A3_COSMIC_BURST_DAMAGE = 20;
    private static final int ALIEN_CLAWS_DAMAGE = 2;
    private static final int A3_ALIEN_CLAWS_DAMAGE = 3;
    private static final int ALIEN_CLAWS_HITS = 5;
    private static final int ASTRAL_CORE_STRENGTH = 3;
    private static final int A18_ASTRAL_CORE_STRENGTH = 4;
    public static final int ASTRAL_CORE_DAMAGE_THRESHOLD = 30;
    private static final int PULSE = 1;
    private static final int A18_PULSE = 2;
    private static final int REGEN = 5;
    private static final int HP_MIN = 160;
    private static final int HP_MAX = 160;
    private static final int A8_HP_MIN = 180;
    private static final int A8_HP_MAX = 180;
    private int cosmicBurstDamage;
    private int alienClawsDamage;
    private int astralCoreStrength;
    private int pulse;

    public Harbinger() {
        this(0.0f, 0.0f);
    }

    public Harbinger(final float x, final float y) {
        super(Harbinger.NAME, ID, HP_MAX, -5.0F, 0, 475.0f, 405.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.cosmicBurstDamage = A3_COSMIC_BURST_DAMAGE;
            this.alienClawsDamage = A3_ALIEN_CLAWS_DAMAGE;
        } else {
            this.cosmicBurstDamage = COSMIC_BURST_DAMAGE;
            this.alienClawsDamage = ALIEN_CLAWS_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.cosmicBurstDamage));
        this.damage.add(new DamageInfo(this, this.alienClawsDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.astralCoreStrength = A18_ASTRAL_CORE_STRENGTH;
            this.pulse = A18_PULSE;
        }
        else {
            this.astralCoreStrength = ASTRAL_CORE_STRENGTH;
            this.pulse = PULSE;
        }
    }

    @Override
    public void usePreBattleAction() {
        AstralCorePower astralCorePower = new AstralCorePower(this, this.astralCoreStrength);
        this.addToBot(new ApplyPowerAction(this, this, astralCorePower));
        this.addToBot(new ApplyPowerAction(this, this, new AstralCoreCounterPower(this, astralCorePower)));
        this.addToBot(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, REGEN)));
        this.addToBot(new ApplyPowerAction(this, this, new EldritchGraspPower(this, this.pulse)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case COSMIC_BURST_ATTACK:
                //TODO Graphical effect for cosmic burst?
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case ALIEN_CLAWS_ATTACK:
                for (int i = 0; i < ALIEN_CLAWS_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!this.lastMove(COSMIC_BURST_ATTACK)) {
            this.setMove(MOVES[0], COSMIC_BURST_ATTACK, Intent.ATTACK, this.cosmicBurstDamage);
        }
        else {
            this.setMove(MOVES[1], ALIEN_CLAWS_ATTACK, Intent.ATTACK, this.alienClawsDamage, ALIEN_CLAWS_HITS, true);
        }
    }
}