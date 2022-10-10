package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.text.MessageFormat;

public class MalicePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:Malice";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int baseAmount;

    public MalicePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.baseAmount = amount;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.loadRegion("attackBurn");
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount, this.baseAmount);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.rarity != AbstractCard.CardRarity.BASIC && card.rarity != AbstractCard.CardRarity.COMMON) {
            this.flash();
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount, Settings.FAST_MODE));
            this.amount++;
            this.updateDescription();
        }
    }

    @Override
    public void atEndOfRound() {
        this.amount = this.baseAmount;
        this.updateDescription();
    }
}
