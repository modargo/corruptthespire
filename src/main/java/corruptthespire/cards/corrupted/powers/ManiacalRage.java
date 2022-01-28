package corruptthespire.cards.corrupted.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManiacalRage extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:ManiacalRage";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int TEMPORARY_HP = 6;
    private static final int UPGRADE_TEMPORARY_HP = 3;
    private static final int CORRUPTION_THRESHOLD = 150;
    private static final int CORRUPTION_TEMPORARY_HP = 3;
    private static final int STRENGTH = 1;

    public ManiacalRage() {
        super(ID, NAME, IMG, COST, getFormattedDescription(DESCRIPTION, null), CardType.POWER, CardTarget.SELF);
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
        int strength = this.getStrength();
        if (strength > 0) {
            this.addToBot(new VFXAction(new BorderLongFlashEffect(Color.FIREBRICK, true)));
            this.addToBot(new VFXAction(p, new InflameEffect(p), 1.0F));
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, strength), strength));
        }
    }

    @Override
    public void applyPowers() {
        int strength = this.getStrength();
        this.rawDescription = getFormattedDescription(cardStrings.EXTENDED_DESCRIPTION[0], strength);
        this.initializeDescription();
    }

    private int getStrength() {
        return Math.max(this.countDebuffs(), this.countStatuses());
    }

    private int countDebuffs() {
        List<String> positivePowerIds = Arrays.asList(WeakPower.POWER_ID, FrailPower.POWER_ID, VulnerablePower.POWER_ID);
        List<String> negativePowerIds = Arrays.asList(DexterityPower.POWER_ID, FocusPower.POWER_ID);
        List<String> numberlessPowers = Collections.singletonList(ConfusionPower.POWER_ID);
        return AbstractDungeon.player.powers.stream().map(p -> {
            if (positivePowerIds.contains(p.ID)) {
                return p.amount;
            }
            else if (negativePowerIds.contains(p.ID) && p.amount < 0) {
                return -p.amount;
            }
            else if (numberlessPowers.contains(p.ID)) {
                return 1;
            }
            return 0;
        }).reduce(0, Integer::sum);
    }

    private int countStatuses() {
        return (int)(AbstractDungeon.player.drawPile.group.stream().filter(c -> c.type == CardType.STATUS).count()
                + AbstractDungeon.player.discardPile.group.stream().filter(c -> c.type == CardType.STATUS).count());
    }

    private static String getFormattedDescription(String description, Integer strength) {
        return MessageFormat.format(description, STRENGTH, CORRUPTION_THRESHOLD, CORRUPTION_TEMPORARY_HP, strength);
    }
}
