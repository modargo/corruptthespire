package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;

public class NightsWhisper extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:NightsWhisper";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int AMOUNT = 1;
    private static final int CORRUPTION_THRESHOLD_STRENGTH = 20;
    private static final int CORRUPTION_THRESHOLD_VULNERABLE = 40;
    private static final int CORRUPTION_THRESHOLD_DOUBLE = 60;
    private static final int CORRUPTION_THRESHOLD_TRIPLE = 100;

    public NightsWhisper() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION_THRESHOLD_STRENGTH, CORRUPTION_THRESHOLD_VULNERABLE, CORRUPTION_THRESHOLD_DOUBLE, CORRUPTION_THRESHOLD_TRIPLE), CardType.SKILL, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = AMOUNT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBaseCost(UPGRADE_COST);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = this.magicNumber * (Cor.corruption >= CORRUPTION_THRESHOLD_TRIPLE ? 3 : Cor.corruption >= CORRUPTION_THRESHOLD_DOUBLE ? 2 : 1);
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, amount, false), amount));
        if (Cor.corruption >= CORRUPTION_THRESHOLD_STRENGTH) {
            this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -amount), -amount));
        }
        if (Cor.corruption >= CORRUPTION_THRESHOLD_VULNERABLE) {
            this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, amount, false), amount));
        }
    }
}
