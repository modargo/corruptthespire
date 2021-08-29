package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.CustomTags;

import java.text.MessageFormat;

public class NightmareDefensePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:NightmareDefense";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public NightmareDefensePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.tags != null && card.hasTag(CustomTags.CORRUPTED)) {
            if (Settings.FAST_MODE) {
                this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.amount, true));
            } else {
                this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.amount));
            }

            this.flash();
        }
    }
}
