package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.combat.SmallColorLaserEffect;
import corruptthespire.powers.PowerUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class GreaterWisp extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:GreaterWisp";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = GreaterWisp.monsterStrings.NAME;
    public static final String[] MOVES = GreaterWisp.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte TRIPLE_RAY_ATTACK = 1;
    private static final byte RAY_ATTACK = 2;
    private static final byte SOOTHING_LIGHT_MOVE = 3;
    private static final int TRIPLE_RAY_DAMAGE = 1;
    private static final int A2_TRIPLE_RAY_DAMAGE = 2;
    private static final int TRIPLE_RAY_HITS = 3;
    private static final int RAY_DAMAGE = 6;
    private static final int A2_RAY_DAMAGE = 7;
    private static final int SOOTHING_LIGHT_STRENGTH = 1;
    private static final int SOOTHING_LIGHT_HEAL = 3;
    private static final int A17_SOOTHING_LIGHT_HEAL = 3;
    private static final int ARTIFACT = 1;
    private static final int A17_ARTIFACT = 2;
    private static final int MIN_HP = 40;
    private static final int MAX_HP = 41;
    private static final int A7_MIN_HP = 42;
    private static final int A7_MAX_HP = 43;
    private final int tripleRayDamage;
    private final int rayDamage;
    private final int soothingLightHeal;
    private final int artifact;

    public GreaterWisp() {
        this(0.0f, 0.0f);
    }

    public GreaterWisp(final float x, final float y) {
        super(GreaterWisp.NAME, ID, MIN_HP, -5.0F, 0, 245.0f, 314.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_MIN_HP, A7_MAX_HP);
        } else {
            this.setHp(MIN_HP, MAX_HP);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.tripleRayDamage = A2_TRIPLE_RAY_DAMAGE;
            this.rayDamage = A2_RAY_DAMAGE;
        } else {
            this.tripleRayDamage = TRIPLE_RAY_DAMAGE;
            this.rayDamage = RAY_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.tripleRayDamage));
        this.damage.add(new DamageInfo(this, this.rayDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.soothingLightHeal = A17_SOOTHING_LIGHT_HEAL;
            this.artifact = A17_ARTIFACT;
        }
        else {
            this.soothingLightHeal = SOOTHING_LIGHT_HEAL;
            this.artifact = ARTIFACT;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, this.artifact)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case TRIPLE_RAY_ATTACK:
                for (int i = 0; i < TRIPLE_RAY_HITS; i++) {
                    this.addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                    this.addToBot(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, Color.WHITE), Settings.FAST_MODE ? 0.1F : 0.3F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                }
                break;
            case RAY_ATTACK:
                this.addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                this.addToBot(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, Color.WHITE), Settings.FAST_MODE ? 0.1F : 0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
            case SOOTHING_LIGHT_MOVE:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                        if (m == this) {
                            this.addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, SOOTHING_LIGHT_STRENGTH)));
                        }
                        else {
                            this.addToBot(new ApplyPowerAction(m, this, PowerUtil.gainStrengthBuff(m, SOOTHING_LIGHT_STRENGTH)));
                        }
                        this.addToBot(new HealAction(m, this, this.soothingLightHeal));
                    }
                }
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        int remainingMonsters = this.getRemainingMonsters();
        byte move;
        switch (this.moveHistory.size() % 3) {
            case 0:
                move = num < 50 ? TRIPLE_RAY_ATTACK : RAY_ATTACK;
                break;
            case 1:
                move = num < 50 ? (this.lastMove(TRIPLE_RAY_ATTACK) ? RAY_ATTACK : TRIPLE_RAY_ATTACK) : SOOTHING_LIGHT_MOVE;
                break;
            case 2:
                ArrayList<Byte> possibleMoves = new ArrayList<>(Arrays.asList(TRIPLE_RAY_ATTACK, RAY_ATTACK, SOOTHING_LIGHT_MOVE));
                possibleMoves.remove(this.moveHistory.get(this.moveHistory.size() - 1));
                possibleMoves.remove(this.moveHistory.get(this.moveHistory.size() - 2));
                move = possibleMoves.get(0);
                break;
            default:
                throw new RuntimeException("Impossible case");
        }
        if (remainingMonsters <= 3 && move == SOOTHING_LIGHT_MOVE) {
            move = TRIPLE_RAY_ATTACK;
        }

        switch (move) {
            case TRIPLE_RAY_ATTACK:
                this.setMove(MOVES[0], TRIPLE_RAY_ATTACK, Intent.ATTACK, this.tripleRayDamage, TRIPLE_RAY_HITS, true);
                break;
            case RAY_ATTACK:
                this.setMove(MOVES[1], RAY_ATTACK, Intent.ATTACK, this.rayDamage);
                break;
            case SOOTHING_LIGHT_MOVE:
                this.setMove(MOVES[2], SOOTHING_LIGHT_MOVE, Intent.BUFF);
                break;
        }
    }

    private int getRemainingMonsters() {
        int count = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
               count++;
            }
        }
        return count;
    }
}