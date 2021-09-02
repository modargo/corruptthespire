package corruptthespire.cards.corrupted.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.powers.PowerUtil;

import java.text.MessageFormat;

public class BlasphemousHymn extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:BlasphemousHymn";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int AMOUNT = 1;
    private static final int CORRUPTION_THRESHOLD = 10;

    public BlasphemousHymn() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, CORRUPTION_THRESHOLD), CardType.SKILL, CardTarget.ALL_ENEMY);
        this.magicNumber = this.baseMagicNumber = AMOUNT;
        this.isEthereal = true;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.exhaust = false;
            this.upgradeName();
            this.rawDescription = MessageFormat.format(cardStrings.UPGRADE_DESCRIPTION, CORRUPTION_THRESHOLD);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        int amount = this.magicNumber * (Cor.corruption / CORRUPTION_THRESHOLD);
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying && !m.halfDead && !m.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, amount, false), amount));
                this.addToBot(new ApplyPowerAction(m, p, PowerUtil.abysstouched(m, amount), amount));
            }
        }
    }
}
