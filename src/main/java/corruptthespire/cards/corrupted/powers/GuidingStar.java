package corruptthespire.cards.corrupted.powers;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.FocusPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;
import java.util.ArrayList;

public class GuidingStar extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:GuidingStar";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int FOCUS = 1;
    private static final int SLOTS = 1;
    private static final int ORBS = 1;

    public GuidingStar() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, SLOTS, ORBS), CardType.POWER, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FOCUS;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = MessageFormat.format(UPGRADE_DESCRIPTION, SLOTS, ORBS);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new FocusPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new IncreaseMaxOrbAction(SLOTS));
        if (this.upgraded) {
            ArrayList<AbstractCard> choices = new ArrayList<>();
            choices.add(new GuidingStar.GuidingStarLightningOption());
            choices.add(new GuidingStar.GuidingStarFrostOption());
            choices.add(new GuidingStar.GuidingStarDarkOption());
            this.addToBot(new ChooseOneAction(choices));
        }
    }

    private static class GuidingStarLightningOption extends CustomCard {
        public static final String ID = "CorruptTheSpire:GuidingStarLightningOption";
        public static final String IMG = GuidingStar.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String NAME = cardStrings.NAME;
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        public GuidingStarLightningOption() {
            super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
            this.magicNumber = this.baseMagicNumber = GuidingStar.ORBS;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            this.addToBot(new ChannelAction(new Lightning()));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new GuidingStar.GuidingStarLightningOption();
        }
    }

    private static class GuidingStarFrostOption extends CustomCard {
        public static final String ID = "CorruptTheSpire:GuidingStarFrostOption";
        public static final String IMG = GuidingStar.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String NAME = cardStrings.NAME;
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        public GuidingStarFrostOption() {
            super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
            this.magicNumber = this.baseMagicNumber = GuidingStar.ORBS;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            this.addToBot(new ChannelAction(new Frost()));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new GuidingStar.GuidingStarFrostOption();
        }
    }

    private static class GuidingStarDarkOption extends CustomCard {
        public static final String ID = "CorruptTheSpire:GuidingStarDarkOption";
        public static final String IMG = GuidingStar.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String NAME = cardStrings.NAME;
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        public GuidingStarDarkOption() {
            super(ID, NAME, IMG, COST, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
            this.magicNumber = this.baseMagicNumber = GuidingStar.ORBS;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            this.addToBot(new ChannelAction(new Dark()));
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new GuidingStar.GuidingStarDarkOption();
        }
    }
}
