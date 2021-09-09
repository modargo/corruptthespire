package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.actions.GainCorruptionAction;

public class CorruptionManifest extends CustomMonster {
    public static final String ID = "CorruptTheSpire:CorruptionManifest";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = CorruptionManifest.monsterStrings.NAME;
    public static final String[] MOVES = CorruptionManifest.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private final Version version;
    private static final byte CORRUPT_DEBUFF = 1;
    private static final byte MANIFEST_MOVE = 2;
    private static final int CORRUPT_CORRUPTION = 1;
    private static final int[] HP_MIN = { 20, 35, 50, 65 };
    private static final int[] HP_MAX = { 23, 38, 54, 69 };
    private static final int[] A9_HP_MIN = { 22, 37, 53, 68 };
    private static final int[] A9_HP_MAX = { 25, 41, 57, 72 };

    public CorruptionManifest() {
        this(0.0f, 0.0f, Version.Act1);
    }

    public CorruptionManifest(final float x, final float y, Version version) {
        super(CorruptionManifest.NAME, ID, HP_MAX[0], -5.0F, 0, 105.0f, 105.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        this.version = version;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(this.v(A9_HP_MIN), this.v(A9_HP_MAX));
        } else {
            this.setHp(this.v(HP_MIN), this.v(HP_MAX));
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CORRUPT_DEBUFF:
                this.addToBot(new FastShakeAction(this, 0.5F, 0.2F));
                this.addToBot(new VFXAction(new BorderLongFlashEffect(Color.PURPLE.cpy())));
                this.addToBot(new GainCorruptionAction(CORRUPT_CORRUPTION));
                break;
            case MANIFEST_MOVE:
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (AbstractDungeon.ascensionLevel < 17) {
            this.setMove(MOVES[1], MANIFEST_MOVE, Intent.UNKNOWN);
        }

        this.setMove(MOVES[0], CORRUPT_DEBUFF, Intent.STRONG_DEBUFF);
    }

    private int v(int[] a) {
        switch (this.version) {
            case Act1: return a[0];
            case Act2: return a[1];
            case Act3: return a[2];
            default: return a[3];
        }
    }

    public enum Version {
        Act1,
        Act2,
        Act3,
        Act4
    }
}