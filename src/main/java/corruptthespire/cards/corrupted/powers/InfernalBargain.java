package corruptthespire.cards.corrupted.powers;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.powers.InfernalBargainDamagePower;
import corruptthespire.powers.InfernalBargainPower;

import java.text.MessageFormat;
import java.util.ArrayList;

public class InfernalBargain extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:InfernalBargain";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int BLOCK = 10;
    private static final int UPGRADE_BLOCK = 3;
    private static final int STRENGTH = 2;
    private static final int UPGRADE_STRENGTH = 1;
    private static final int DRAW = 2;
    private static final int UPGRADE_DRAW = 1;
    private static final int DAMAGE = 20;

    public InfernalBargain() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, DAMAGE), CardType.POWER, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = STRENGTH;
        this.magicNumber2 = this.baseMagicNumber2 = DRAW;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_STRENGTH);
            this.upgradeMagicNumber2(UPGRADE_DRAW);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new InfernalBargainPower(p, this.block, this.magicNumber, this.magicNumber2, DAMAGE)));
    }

    public enum InfernalBargainChoice {
        Block,
        Strength,
        Draw,
        Damage
    }

    public static abstract class AbstractInfernalBargainOption extends CustomCard {
        public static final String IMG = InfernalBargain.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        protected final InfernalBargainPower power;
        protected final InfernalBargainChoice choice;

        public AbstractInfernalBargainOption(String id, String description, InfernalBargainPower power, InfernalBargainChoice choice) {
            super(id, NAME, IMG, COST, description, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
            this.power = power;
            this.choice = choice;
        }

        @Override
        public final void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public final void onChoseThisOption() {
            this.applyChoice();
            this.power.recordChoice(this.choice);
        }

        public abstract void applyChoice();

        @Override
        public void upgrade() {}

        @Override
        public abstract AbstractCard makeCopy();
    }

    public static class InfernalBargainBlockOption extends AbstractInfernalBargainOption {
        public static final String ID = "CorruptTheSpire:InfernalBargainBlockOption";
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;

        private final int block;

        public InfernalBargainBlockOption(InfernalBargainPower power, int block) {
            super(ID, MessageFormat.format(DESCRIPTION, block), power, InfernalBargainChoice.Block);
            this.block = block;
        }

        @Override
        public void applyChoice() {
            this.addToTop(new GainBlockAction(AbstractDungeon.player, this.block));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new InfernalBargainStrengthOption(this.power, this.block);
        }
    }

    public static class InfernalBargainStrengthOption extends AbstractInfernalBargainOption {
        public static final String ID = "CorruptTheSpire:InfernalBargainStrengthOption";
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;

        private final int strength;

        public InfernalBargainStrengthOption(InfernalBargainPower power, int strength) {
            super(ID, MessageFormat.format(DESCRIPTION, strength), power, InfernalBargainChoice.Strength);
            this.strength = strength;
        }

        @Override
        public void applyChoice() {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.strength)));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new InfernalBargainStrengthOption(this.power, this.strength);
        }
    }

    public static class InfernalBargainDrawOption extends AbstractInfernalBargainOption {
        public static final String ID = "CorruptTheSpire:InfernalBargainDrawOption";
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;

        private final int draw;

        public InfernalBargainDrawOption(InfernalBargainPower power, int draw) {
            super(ID, MessageFormat.format(DESCRIPTION, draw), power, InfernalBargainChoice.Draw);
            this.draw = draw;
        }

        @Override
        public void applyChoice() {
            this.addToTop(new DrawCardAction(this.draw));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new InfernalBargainStrengthOption(this.power, this.draw);
        }
    }

    public static class InfernalBargainDamageOption extends AbstractInfernalBargainOption {
        public static final String ID = "CorruptTheSpire:InfernalBargainDamageOption";
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;

        private final int damage;

        public InfernalBargainDamageOption(InfernalBargainPower power, int damage) {
            super(ID, MessageFormat.format(DESCRIPTION, damage), power, InfernalBargainChoice.Damage);
            this.damage = damage;
        }

        @Override
        public void applyChoice() {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new InfernalBargainDamagePower(AbstractDungeon.player, this.damage)));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new InfernalBargainStrengthOption(this.power, this.damage);
        }
    }
}
