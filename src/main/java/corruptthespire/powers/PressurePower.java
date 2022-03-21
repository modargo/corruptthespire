package corruptthespire.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class PressurePower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:Pressure";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int PLAY_LIMIT = 2;

    private int counter;

    public PressurePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.priority = 50;
        this.counter = 0;
        this.type = PowerType.BUFF;
        this.updateDescription();
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], PLAY_LIMIT);
    }

    public void atPlayerTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        this.counter++;
        if (this.counter >= PLAY_LIMIT) {
            this.flash();
        }
    }

    public boolean canPlayCard() {
        return this.counter < PLAY_LIMIT;
    }
}
