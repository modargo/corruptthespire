package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.PermanentDrawReductionPower;
import corruptthespire.powers.TemporaryConfusionPower;
import corruptthespire.powers.TemporaryHexPower;

public class DeliriumManifest extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:DeliriumManifest";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private final Version version;
    private static final byte CONFUSION_DEBUFF = 1;
    private static final byte HEX_ATTACK = 2;
    private static final byte HARM_ATTACK = 3;
    private static final byte DECREPIFY_DEBUFF = 4;
    private static final int CONFUSION_CONFUSION = 1;
    private static final int[] HEX_DAMAGE = { 3, 5 };
    private static final int[] A2_HEX_DAMAGE = { 4, 6 };
    private static final int[] HEX_HEX = { 1, 2 };
    private static final int[] A17_HEX_HEX = { 2, 3 };
    private static final int[] HARM_DAMAGE = { 7, 10 };
    private static final int[] A2_HARM_DAMAGE = { 8, 12 };
    private static final int DECREPIFY_DRAW_DOWN = 1;
    private static final int DECREPIFY_TEMPORARY_DRAW_DOWN = 1;
    private static final int[] HP_MIN = { 28, 36 };
    private static final int[] HP_MAX = { 30, 38 };
    private static final int[] A7_HP_MIN = { 31, 39 };
    private static final int[] A7_HP_MAX = { 33, 41 };
    private final int hexDamage;
    private final int hexHex;
    private final int harmDamage;

    private boolean usedHex = false;
    private boolean usedDecrepify = false;

    public DeliriumManifest() {
        this(0.0f, 0.0f, Version.Act2);
    }

    public DeliriumManifest(final float x, final float y, Version version) {
        super(DeliriumManifest.NAME, ID, HP_MAX[0], -5.0F, 0, 170.0f, 145.0f, IMG, x, y);
        this.version = version;
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(this.v(A7_HP_MIN), this.v(A7_HP_MAX));
        } else {
            this.setHp(this.v(HP_MIN), this.v(HP_MAX));
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.hexDamage = this.v(A2_HEX_DAMAGE);
            this.harmDamage = this.v(A2_HARM_DAMAGE);
        } else {
            this.hexDamage = this.v(HEX_DAMAGE);
            this.harmDamage = this.v(HARM_DAMAGE);
        }
        this.damage.add(new DamageInfo(this, this.hexDamage));
        this.damage.add(new DamageInfo(this, this.harmDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.hexHex = this.v(A17_HEX_HEX);
        }
        else {
            this.hexHex = this.v(HEX_HEX);
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CONFUSION_DEBUFF:
                this.addToBot(new AnimateShakeAction(this, 0.3F, 0.1F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new TemporaryConfusionPower(AbstractDungeon.player)));
                break;
            case HEX_ATTACK:
                this.usedHex = true;
                this.addToBot(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new TemporaryHexPower(AbstractDungeon.player, this.hexHex, true)));
                break;
            case HARM_ATTACK:
                this.addToBot(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.POISON));
                break;
            case DECREPIFY_DEBUFF:
                this.usedDecrepify = true;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new PermanentDrawReductionPower(AbstractDungeon.player, DECREPIFY_DRAW_DOWN)));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, DECREPIFY_TEMPORARY_DRAW_DOWN)));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || (this.lastMove(HARM_ATTACK) && this.usedDecrepify)) {
            this.setMove(MOVES[0], CONFUSION_DEBUFF, Intent.DEBUFF);
        }
        else if (this.lastMove(CONFUSION_DEBUFF) && !this.usedHex) {
            this.setMove(MOVES[1], HEX_ATTACK, Intent.ATTACK_DEBUFF, this.hexDamage);
        }
        else if (this.lastMove(HEX_ATTACK) || this.lastMove(CONFUSION_DEBUFF) || this.lastMove(DECREPIFY_DEBUFF)) {
            this.setMove(MOVES[2], HARM_ATTACK, Intent.ATTACK, this.harmDamage);
        }
        else {
            this.setMove(MOVES[3], DECREPIFY_DEBUFF, Intent.STRONG_DEBUFF);
        }
    }

    private int v(int[] a) {
        switch (this.version) {
            case Act2: return a[0];
            default: return a[1];
        }
    }

    public enum Version {
        Act2,
        Act3
    }
}