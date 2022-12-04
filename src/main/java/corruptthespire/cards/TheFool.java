package corruptthespire.cards;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import corruptthespire.CorruptTheSpire;

import java.util.ArrayList;

public class TheFool extends AbstractModCard {
    public static final String ID = "CorruptTheSpire:TheFool";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int DRAW = 3;
    private static final int UPGRADE_DRAW = 1;
    private static final int REDUCED_DRAW_NEXT_TURN = 1;

    public TheFool() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.magicNumber2 = this.baseMagicNumber2 = REDUCED_DRAW_NEXT_TURN;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new TheFoolNowOption(this.magicNumber, this.magicNumber2));
        choices.add(new TheFoolLaterOption(this.magicNumber));
        this.addToBot(new ChooseOneAction(choices));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeMagicNumber(UPGRADE_DRAW);
            this.upgradeName();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheFool();
    }

    private static class TheFoolNowOption extends AbstractModCard {
        public static final String ID = "CorruptTheSpire:TheFoolNowOption";
        public static final String IMG = TheFool.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String NAME = cardStrings.NAME;
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        public TheFoolNowOption(int draw, int reducedDrawNextTurn) {
            super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
            this.magicNumber = this.baseMagicNumber = draw;
            this.magicNumber2 = this.baseMagicNumber2 = reducedDrawNextTurn;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            this.addToBot(new DrawCardAction(this.magicNumber));
            DrawReductionPower power = new DrawReductionPower(AbstractDungeon.player, this.magicNumber2);
            ReflectionHacks.setPrivate(power, DrawReductionPower.class, "justApplied", false);
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new TheFoolNowOption(this.magicNumber, this.magicNumber2);
        }
    }

    private static class TheFoolLaterOption extends AbstractModCard {
        public static final String ID = "CorruptTheSpire:TheFoolLaterOption";
        public static final String IMG = TheFool.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String NAME = cardStrings.NAME;
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        public TheFoolLaterOption(int draw) {
            super(ID, NAME, IMG, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
            this.magicNumber = this.baseMagicNumber = draw;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawCardNextTurnPower(AbstractDungeon.player, this.magicNumber)));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new TheFoolLaterOption(this.magicNumber);
        }
    }
}
