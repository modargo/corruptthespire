package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
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
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.effects.combat.SmallColorLaserEffect;
import corruptthespire.powers.ImpendingDoomPower;
import corruptthespire.powers.PressurePower;

public class Doom extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:Doom";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = Doom.monsterStrings.NAME;
    public static final String[] MOVES = Doom.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte DARK_CLAW_ATTACK = 1;
    private static final byte DEATHBEAM_ATTACK = 2;
    private static final byte KARMIC_JUDGEMENT_ATTACK = 3;
    private static final int DARK_CLAW_DAMAGE = 3;
    private static final int A3_DARK_CLAW_DAMAGE = 4;
    private static final int DARK_CLAW_HITS = 3;
    private static final int DARK_CLAW_FRAIL = 1;
    private static final int DEATHBEAM_DAMAGE = 16;
    private static final int A3_DEATHBEAM_DAMAGE = 18;
    private static final int KARMIC_JUDGEMENT_DAMAGE = 10;
    private static final int A3_KARMIC_JUDGEMENT_DAMAGE = 12;
    private static final int KARMIC_JUDGEMENT_STRENGTH = 2;
    private static final int A18_KARMIC_JUDGEMENT_STRENGTH = 3;
    private static final int IMPENDING_DOOM_VULNERABLE = 1;
    private static final int A18_IMPENDING_DOOM_VULNERABLE = 3;
    private static final int HP_MIN = 110;
    private static final int HP_MAX = 110;
    private static final int A8_HP_MIN = 120;
    private static final int A8_HP_MAX = 120;
    private final int darkClawDamage;
    private final int deathbeamDamage;
    private final int karmicJudgementDamage;
    private final int karmicJudgementStrength;
    private final int impendingDoomVulnerable;

    public Doom() {
        this(0.0f, 0.0f);
    }

    public Doom(final float x, final float y) {
        super(Doom.NAME, ID, HP_MAX, -5.0F, 0, 285.0f, 255.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.darkClawDamage = A3_DARK_CLAW_DAMAGE;
            this.deathbeamDamage = A3_DEATHBEAM_DAMAGE;
            this.karmicJudgementDamage = A3_KARMIC_JUDGEMENT_DAMAGE;
        } else {
            this.darkClawDamage = DARK_CLAW_DAMAGE;
            this.deathbeamDamage = DEATHBEAM_DAMAGE;
            this.karmicJudgementDamage = KARMIC_JUDGEMENT_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.darkClawDamage));
        this.damage.add(new DamageInfo(this, this.deathbeamDamage));
        this.damage.add(new DamageInfo(this, this.karmicJudgementDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.karmicJudgementStrength = A18_KARMIC_JUDGEMENT_STRENGTH;
            this.impendingDoomVulnerable = A18_IMPENDING_DOOM_VULNERABLE;
        }
        else {
            this.karmicJudgementStrength = KARMIC_JUDGEMENT_STRENGTH;
            this.impendingDoomVulnerable = IMPENDING_DOOM_VULNERABLE;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new PressurePower(this)));
        this.addToBot(new ApplyPowerAction(this, this, new ImpendingDoomPower(this, this.impendingDoomVulnerable)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case DARK_CLAW_ATTACK:
                for (int i = 0; i < DARK_CLAW_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DARK_CLAW_FRAIL, true)));
                break;
            case DEATHBEAM_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, Color.BLACK), Settings.FAST_MODE ? 0.1F : 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
            case KARMIC_JUDGEMENT_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.karmicJudgementStrength)));
                break;

        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || (!this.lastMove(DARK_CLAW_ATTACK) && !this.lastMoveBefore(DARK_CLAW_ATTACK))) {
            this.setMove(MOVES[0], DARK_CLAW_ATTACK, Intent.ATTACK_DEBUFF, this.darkClawDamage, DARK_CLAW_HITS, true);
        }
        else if ((this.lastMove(DARK_CLAW_ATTACK) && num > 50) || this.lastMove(KARMIC_JUDGEMENT_ATTACK) && this.lastMoveBefore(DARK_CLAW_ATTACK)) {
            this.setMove(MOVES[1], DEATHBEAM_ATTACK, Intent.ATTACK, this.deathbeamDamage);
        }
        else {
            this.setMove(MOVES[2], KARMIC_JUDGEMENT_ATTACK, Intent.ATTACK_BUFF, this.karmicJudgementDamage);
        }
    }
}