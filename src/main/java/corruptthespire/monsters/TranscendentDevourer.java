package corruptthespire.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.Contagion;
import corruptthespire.powers.FetidBodyPower;
import corruptthespire.powers.ProtectiveMawPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranscendentDevourer extends CustomMonster
{
    public static final String ID = "CorruptTheSpire:TranscendentDevourer";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = TranscendentDevourer.monsterStrings.NAME;
    public static final String[] MOVES = TranscendentDevourer.monsterStrings.MOVES;
    private static final String IMG = CorruptTheSpire.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte SALIVATE_BUFF = 1;
    private static final byte NIBBLE_ATTACK = 2;
    private static final byte GORGE_ATTACK = 3;
    private static final int SALIVATE_STRENGTH = 1;
    private static final int A18_SALIVATE_STRENGTH = 2;
    private static final int NIBBLE_DAMAGE = 10;
    private static final int A3_NIBBLE_DAMAGE = 12;
    private static final int GORGE_DAMAGE = 3;
    private static final int GORGE_BASE_HITS = 4;
    private static final int A3_GORGE_BASE_HITS = 5;
    public static final int FETID_BODY_DAMAGE_THRESHOLD = 30;
    private static final int LESSER_DEVOURERS = 3;
    public static final int PROTECTIVE_MAW = 15;
    public static final int A18_PROTECTIVE_MAW = 20;
    private static final int VULNERABILITY = 5;
    private static final int HP = 100;
    private static final int A8_HP = 110;
    private final int salivateStrength;
    private final int nibbleDamage;
    private final int gorgeBaseHits;
    private final int protectiveMaw;

    public TranscendentDevourer() {
        this(0.0f, 0.0f);
    }

    public TranscendentDevourer(final float x, final float y) {
        super(TranscendentDevourer.NAME, ID, HP, -5.0F, 0, 325.0f, 355.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP, A8_HP);
        } else {
            this.setHp(HP, HP);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.nibbleDamage = A3_NIBBLE_DAMAGE;
            this.gorgeBaseHits = A3_GORGE_BASE_HITS;
        } else {
            this.nibbleDamage = NIBBLE_DAMAGE;
            this.gorgeBaseHits = GORGE_BASE_HITS;
        }
        this.damage.add(new DamageInfo(this, this.nibbleDamage));
        this.damage.add(new DamageInfo(this, GORGE_DAMAGE));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.salivateStrength = A18_SALIVATE_STRENGTH;
            this.protectiveMaw = A18_PROTECTIVE_MAW;
        }
        else {
            this.salivateStrength = SALIVATE_STRENGTH;
            this.protectiveMaw = PROTECTIVE_MAW;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new ProtectiveMawPower(this, this.protectiveMaw * LESSER_DEVOURERS)));
        this.addToBot(new ApplyPowerAction(this, this, new FetidBodyPower(this)));
        this.addToBot(new ApplyPowerAction(this, this, new SporeCloudPower(this, VULNERABILITY)));
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.output > 0 && info.type != DamageInfo.DamageType.NORMAL) {
            AbstractPower p = this.getPower(ProtectiveMawPower.POWER_ID);
            if (p != null) {
                info.output = (int)(info.output * (1.0f - (p.amount / 100.0f)));
            }
        }

        super.damage(info);
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case SALIVATE_BUFF:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.isDead) {
                        this.addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, this.salivateStrength)));
                    }
                }
                break;
            case NIBBLE_ATTACK:
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    this.addToBot(new MakeTempCardInDrawPileAction(new Contagion(), 1, true, true));
                }
                else {
                    this.addToBot(new MakeTempCardInDiscardAction(new Contagion(), 1));
                }
                break;
            case GORGE_ATTACK:
                int hits = this.getGorgeHits();
                for (int i = 0; i < hits; i++) {
                    AbstractPlayer p = AbstractDungeon.player;
                    this.addToBot(new VFXAction(new BiteEffect(p.hb.cX, p.hb.cY - 40.0F * Settings.scale, new Color(579543807)), Settings.FAST_MODE ? 0.1F : 0.3F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                }
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        int s = this.moveHistory.size();
        int movesToCheck = s % 3;
        List<Byte> potentialMoves = new ArrayList<>();
        potentialMoves.add(SALIVATE_BUFF);
        potentialMoves.add(NIBBLE_ATTACK);
        potentialMoves.add(GORGE_ATTACK);
        if (this.firstMove) {
            potentialMoves.remove((Object)SALIVATE_BUFF);
            potentialMoves.remove((Object)NIBBLE_ATTACK);
        }
        if (s > 0) {
            potentialMoves.remove(this.moveHistory.get(s - 1));
        }
        if (movesToCheck > 1 && s > 1) {
            potentialMoves.remove(this.moveHistory.get(s - 2));
        }

        if (potentialMoves.size() != 1) {
            Collections.shuffle(potentialMoves, AbstractDungeon.aiRng.random);
        }

        switch (potentialMoves.get(0)) {
            case SALIVATE_BUFF:
                this.setMove(MOVES[0], SALIVATE_BUFF, Intent.BUFF);
                break;
            case NIBBLE_ATTACK:
                this.setMove(MOVES[1], NIBBLE_ATTACK, Intent.ATTACK_DEBUFF, this.nibbleDamage);
                break;
            case GORGE_ATTACK:
                this.setGorgeMove();
                break;
        }
    }

    private void setGorgeMove() {
        this.setMove(MOVES[2], GORGE_ATTACK, Intent.ATTACK, GORGE_DAMAGE, this.getGorgeHits(), true);
    }

    private int getGorgeHits() {
        int livingDevourers = (int)AbstractDungeon.getMonsters().monsters.stream().filter(m ->
                !m.isDying && !m.isDead
                && (m.id.equals(LesserDevourerBlue.ID) || m.id.equals(LesserDevourerBrown.ID) || m.id.equals(LesserDevourerGreen.ID))
            ).count();
        return this.gorgeBaseHits - livingDevourers;
    }

    private void updateGorgeIntent() {
        if (this.nextMove == GORGE_ATTACK) {
            this.setGorgeMove();
            this.createIntent();
        }
    }

    public static void triggerOnLesserDevourerDeath(AbstractMonster lesserDevourer) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.id.equals(TranscendentDevourer.ID)) {
                int reductionAmount = AbstractDungeon.ascensionLevel >= 18 ? TranscendentDevourer.A18_PROTECTIVE_MAW : TranscendentDevourer.PROTECTIVE_MAW;
                lesserDevourer.addToTop(new ReducePowerAction(m, lesserDevourer, ProtectiveMawPower.POWER_ID, reductionAmount));
                ((TranscendentDevourer)m).updateGorgeIntent();
            }
        }
    }
}