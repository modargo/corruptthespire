package corruptthespire.cards.corrupted.powers;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManiacalRage extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:ManiacalRage";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int TEMPORARY_HP = 10;
    private static final int UPGRADE_TEMPORARY_HP = 4;
    private static final int CORRUPTION_THRESHOLD = 80;
    private static final int CORRUPTION_TEMPORARY_HP = 4;
    private static final int STRENGTH = 1;

    public ManiacalRage() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, STRENGTH, CORRUPTION_THRESHOLD, CORRUPTION_TEMPORARY_HP), CardType.POWER, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = TEMPORARY_HP;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_TEMPORARY_HP);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int temporaryHp = this.magicNumber + (Cor.corruption >= CORRUPTION_THRESHOLD ? CORRUPTION_TEMPORARY_HP: 0);
        this.addToBot(new AddTemporaryHPAction(p, p, temporaryHp));
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new ManiacalRageDebuffsOption());
        choices.add(new ManiacalRageStatusesOption());
        this.addToBot(new ChooseOneAction(choices));
    }

    private static class ManiacalRageDebuffsOption extends CustomCard {
        public static final String ID = "CorruptTheSpire:ManiacalRageDebuffsOption";
        public static final String IMG = ManiacalRage.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String NAME = cardStrings.NAME;
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        public ManiacalRageDebuffsOption() {
            super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, STRENGTH), CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
            this.magicNumber = this.baseMagicNumber = this.countDebuffs();
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            AbstractPlayer p = AbstractDungeon.player;
            if (this.magicNumber > 0) {
                this.addToBot(new VFXAction(new BorderLongFlashEffect(Color.FIREBRICK, true)));
                this.addToBot(new VFXAction(p, new InflameEffect(p), 1.0F));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
            }
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new ManiacalRageDebuffsOption();
        }

        private int countDebuffs() {
            List<String> powerIds = Arrays.asList(WeakPower.POWER_ID, FrailPower.POWER_ID, VulnerablePower.POWER_ID);
            return (int)AbstractDungeon.player.powers.stream().filter(p -> powerIds.contains(p.ID)).count();
        }
    }

    private static class ManiacalRageStatusesOption extends CustomCard {
        public static final String ID = "CorruptTheSpire:ManiacalRageStatusesOption";
        public static final String IMG = ManiacalRage.IMG;
        private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        public static final String NAME = cardStrings.NAME;
        public static final String DESCRIPTION = cardStrings.DESCRIPTION;
        private static final int COST = -2;

        public ManiacalRageStatusesOption() {
            super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, STRENGTH), CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
            this.magicNumber = this.baseMagicNumber = this.countStatuses();
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            AbstractPlayer p = AbstractDungeon.player;
            if (this.magicNumber > 0) {
                this.addToBot(new VFXAction(new BorderLongFlashEffect(Color.FIREBRICK, true)));
                this.addToBot(new VFXAction(p, new InflameEffect(p), 1.0F));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
            }
        }

        @Override
        public void upgrade() {}

        @Override
        public AbstractCard makeCopy() {
            return new ManiacalRageStatusesOption();
        }

        private int countStatuses() {
            return (int)(AbstractDungeon.player.drawPile.group.stream().filter(c -> c.type == CardType.STATUS).count()
                + AbstractDungeon.player.discardPile.group.stream().filter(c -> c.type == CardType.STATUS).count());
        }
    }
}
