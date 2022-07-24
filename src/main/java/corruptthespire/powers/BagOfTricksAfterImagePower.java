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
import corruptthespire.relics.corrupted.BagOfTricks;

import java.text.MessageFormat;

public class BagOfTricksAfterImagePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:BagOfTricksAfterImage";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BagOfTricksAfterImagePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("afterImage");
    }

    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        BagOfTricks.incrementBlockStat(this.amount);
        this.flash();
        this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.amount, Settings.FAST_MODE));
    }
}
