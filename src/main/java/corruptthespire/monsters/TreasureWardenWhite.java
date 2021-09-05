package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.combat.SmallColorLaserEffect;
import corruptthespire.powers.DragonsHeartPower;

public class TreasureWardenWhite extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:TreasureWardenWhite";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private final Version version;
    private static final byte MAELSTROM_ATTACK = 1;
    private static final byte LIGHTNING_ATTACK = 2;
    private static final byte SCORCH_ATTACK = 3;
    private static final int[] MAELSTROM_DAMAGE = { 2, 3, 4 };
    private static final int[] A3_MAELSTROM_DAMAGE = { 2, 4, 5 };
    private static final int MAELSTROM_HITS = 3;
    private static final int MAELSTROM_FRAIL = 1;
    private static final int A18_MAELSTROM_FRAIL = 2;
    private static final int[] LIGHTNING_DAMAGE = { 3, 5, 7 };
    private static final int[] A3_LIGHTNING_DAMAGE = { 4, 6, 8 };
    private static final int LIGHTNING_HITS = 2;
    private static final int LIGHTNING_WEAK = 1;
    private static final int[] SCORCH_DAMAGE = { 9, 13, 17 };
    private static final int[] A3_SCORCH_DAMAGE = { 10, 15, 19 };
    private static final int SCORCH_ENTANGLE = 1;
    private static final int[] DRAGONS_HEART_AMOUNT = { 2, 3, 4 };
    private static final int[] A18_DRAGONS_HEART_AMOUNT = { 3, 4, 5 };
    private static final int[] HP_MIN = { 55, 92, 166 };
    private static final int[] HP_MAX = { 58, 96, 171 };
    private static final int[] A8_HP_MIN = { 57, 95, 170 };
    private static final int[] A8_HP_MAX = { 60, 99, 175 };
    private int maelstromDamage;
    private int maelstromFrail;
    private int lightningDamage;
    private int scorchDamage;
    private int dragonsHeartAmount;

    public TreasureWardenWhite() {
        this(0.0f, 0.0f, Version.Act1);
    }

    public TreasureWardenWhite(final float x, final float y, Version version) {
        super(TreasureWardenWhite.NAME, ID, HP_MAX[0], -5.0F, 0, 340.0f, 450.0f, IMG, x, y);
        this.version = version;
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(this.v(A8_HP_MIN), this.v(A8_HP_MAX));
        } else {
            this.setHp(this.v(HP_MIN), this.v(HP_MAX));
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.maelstromDamage = this.v(A3_MAELSTROM_DAMAGE);
            this.lightningDamage = this.v(A3_LIGHTNING_DAMAGE);
            this.scorchDamage = this.v(A3_SCORCH_DAMAGE);
        } else {
            this.maelstromDamage = this.v(MAELSTROM_DAMAGE);
            this.lightningDamage = this.v(LIGHTNING_DAMAGE);
            this.scorchDamage = this.v(SCORCH_DAMAGE);
        }
        this.damage.add(new DamageInfo(this, this.maelstromDamage));
        this.damage.add(new DamageInfo(this, this.lightningDamage));
        this.damage.add(new DamageInfo(this, this.scorchDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.maelstromFrail = A18_MAELSTROM_FRAIL;
            this.dragonsHeartAmount = this.v(A18_DRAGONS_HEART_AMOUNT);
        }
        else {
            this.maelstromFrail = MAELSTROM_FRAIL;
            this.dragonsHeartAmount = this.v(DRAGONS_HEART_AMOUNT);
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new DragonsHeartPower(this, this.dragonsHeartAmount), this.dragonsHeartAmount));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case MAELSTROM_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                for (int i = 0; i < MAELSTROM_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.maelstromFrail, true), this.maelstromFrail));
                break;
            case LIGHTNING_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                for (int i = 0; i < LIGHTNING_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, LIGHTNING_WEAK, true), LIGHTNING_WEAK));
                break;
            case SCORCH_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, Color.RED), Settings.FAST_MODE ? 0.1F : 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new EntanglePower(AbstractDungeon.player), SCORCH_ENTANGLE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        switch (this.moveHistory.size() % 4) {
            case 0:
                move = MAELSTROM_ATTACK;
                break;
            case 1:
            case 2:
                move = LIGHTNING_ATTACK;
                break;
            case 3:
                move = SCORCH_ATTACK;
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case MAELSTROM_ATTACK:
                this.setMove(MOVES[0], MAELSTROM_ATTACK, Intent.ATTACK_DEBUFF, this.maelstromDamage, MAELSTROM_HITS, true);
                break;
            case LIGHTNING_ATTACK:
                this.setMove(MOVES[1], LIGHTNING_ATTACK, Intent.ATTACK_DEBUFF, this.lightningDamage, LIGHTNING_HITS, true);
                break;
            case SCORCH_ATTACK:
                this.setMove(MOVES[2], SCORCH_ATTACK, Intent.ATTACK_DEBUFF, this.scorchDamage);
                break;
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