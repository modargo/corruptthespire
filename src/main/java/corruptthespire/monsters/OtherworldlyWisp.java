package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
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
import corruptthespire.powers.RadiantPower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OtherworldlyWisp extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:OtherworldlyWisp";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = OtherworldlyWisp.monsterStrings.NAME;
    public static final String[] MOVES = OtherworldlyWisp.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte DANCING_LIGHTS_ATTACK = 1;
    private static final byte RAY_ATTACK = 2;
    private static final byte ENERGIZE_ATTACK = 3;
    private static final int DANCING_LIGHTS_DAMAGE = 3;
    private static final int A2_DANCING_LIGHTS_DAMAGE = 4;
    private static final int DANCING_LIGHTS_BLOCK = 3;
    private static final int A7_DANCING_LIGHTS_BLOCK = 4;
    private static final int RAY_DAMAGE = 5;
    private static final int A2_RAY_DAMAGE = 6;
    private static final int ENERGIZE_DAMAGE = 2;
    private static final int A2_ENERGIZE_DAMAGE = 3;
    private static final int ENERGIZE_STRENGTH = 1;
    private static final int ARTIFACT = 1;
    private static final int A17_ARTIFACT = 1;
    private static final int RADIANT_STRENGTH = 1;
    private static final int A17_RADIANT_STRENGTH = 2;
    private static final int RADIANT_HEAL = 4;
    private static final int A17_RADIANT_HEAL = 6;
    private static final int MIN_HP = 20;
    private static final int MAX_HP = 21;
    private static final int A7_MIN_HP = 22;
    private static final int A7_MAX_HP = 23;
    private final int dancingLightsDamage;
    private final int dancingLightsBlock;
    private final int rayDamage;
    private final int energizeDamage;
    private final int artifact;
    private final int radiantStrength;
    private final int radiantHeal;

    public OtherworldlyWisp() {
        this(0.0f, 0.0f);
    }

    public OtherworldlyWisp(final float x, final float y) {
        super(OtherworldlyWisp.NAME, ID, MIN_HP, -5.0F, 0, 105.0f, 235.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_MIN_HP, A7_MAX_HP);
            this.dancingLightsBlock = A7_DANCING_LIGHTS_BLOCK;
        } else {
            this.setHp(MIN_HP, MAX_HP);
            this.dancingLightsBlock = DANCING_LIGHTS_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.dancingLightsDamage = A2_DANCING_LIGHTS_DAMAGE;
            this.rayDamage = A2_RAY_DAMAGE;
            this.energizeDamage = A2_ENERGIZE_DAMAGE;
        } else {
            this.dancingLightsDamage = DANCING_LIGHTS_DAMAGE;
            this.rayDamage = RAY_DAMAGE;
            this.energizeDamage = ENERGIZE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.dancingLightsDamage));
        this.damage.add(new DamageInfo(this, this.rayDamage));
        this.damage.add(new DamageInfo(this, this.energizeDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.artifact = A17_ARTIFACT;
            this.radiantStrength = A17_RADIANT_STRENGTH;
            this.radiantHeal = A17_RADIANT_HEAL;
        }
        else {
            this.artifact = ARTIFACT;
            this.radiantStrength = RADIANT_STRENGTH;
            this.radiantHeal = RADIANT_HEAL;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, this.artifact)));
        this.addToBot(new ApplyPowerAction(this, this, new RadiantPower(this, this.radiantStrength, this.radiantHeal)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case DANCING_LIGHTS_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                this.addToBot(new GainBlockAction(this, this.dancingLightsBlock));
                break;
            case RAY_ATTACK:
                this.addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                this.addToBot(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, Color.WHITE), Settings.FAST_MODE ? 0.1F : 0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
            case ENERGIZE_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, ENERGIZE_STRENGTH)));

                List<AbstractMonster> targets = AbstractDungeon.getMonsters().monsters.stream().filter(m -> m.id.equals(GreaterWisp.ID)).collect(Collectors.toList());
                if (targets.isEmpty()) {
                    targets = AbstractDungeon.getMonsters().monsters.stream().filter(m -> m != this).collect(Collectors.toList());
                }
                if (!targets.isEmpty()) {
                    AbstractMonster target = targets.get(AbstractDungeon.aiRng.random(targets.size() - 1));
                    this.addToBot(new ApplyPowerAction(target, this, PowerUtil.gainStrengthBuff(target, ENERGIZE_STRENGTH)));
                }
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        switch (this.moveHistory.size() % 3) {
            case 0:
                move = num < 50 ? DANCING_LIGHTS_ATTACK : ENERGIZE_ATTACK;
                break;
            case 1:
                move = num < 50 ? (this.lastMove(DANCING_LIGHTS_ATTACK) ? ENERGIZE_ATTACK : DANCING_LIGHTS_ATTACK) : RAY_ATTACK;
                break;
            case 2:
                ArrayList<Byte> possibleMoves = new ArrayList<>(Arrays.asList(DANCING_LIGHTS_ATTACK, ENERGIZE_ATTACK, RAY_ATTACK));
                possibleMoves.remove(this.moveHistory.get(this.moveHistory.size() - 1));
                possibleMoves.remove(this.moveHistory.get(this.moveHistory.size() - 2));
                move = possibleMoves.get(0);
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case DANCING_LIGHTS_ATTACK:
                this.setMove(MOVES[0], DANCING_LIGHTS_ATTACK, Intent.ATTACK_DEFEND, this.dancingLightsDamage);
                break;
            case RAY_ATTACK:
                this.setMove(MOVES[1], RAY_ATTACK, Intent.ATTACK, this.rayDamage);
                break;
            case ENERGIZE_ATTACK:
                this.setMove(MOVES[2], ENERGIZE_ATTACK, Intent.ATTACK_BUFF, this.energizeDamage);
                break;
        }
    }
}