package corruptthespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.text.MessageFormat;

public class SoulLinkPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:SoulLink";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractMonster partner;

    public SoulLinkPower(AbstractCreature owner, AbstractMonster partner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.partner = partner;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        this.loadRegion("deva");
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], partner.name);
    }
}
