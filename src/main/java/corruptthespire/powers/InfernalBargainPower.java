package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.powers.GuidingStar;
import corruptthespire.cards.corrupted.powers.InfernalBargain;
import corruptthespire.cards.corrupted.powers.InfernalBargain.InfernalBargainChoice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InfernalBargainPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:InfernalBargain";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int block;
    private final int strength;
    private final int draw;
    private final int damage;

    private Set<InfernalBargainChoice> choices;

    public InfernalBargainPower(AbstractCreature owner, int block, int strength, int draw, int damage) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.block = block;
        this.strength = strength;
        this.draw = draw;
        this.damage = damage;
        this.choices = new HashSet<>();
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if (!this.choices.contains(InfernalBargainChoice.Block)) {
            this.description += MessageFormat.format(DESCRIPTIONS[1], this.block);
        }
        if (!this.choices.contains(InfernalBargainChoice.Strength)) {
            this.description += MessageFormat.format(DESCRIPTIONS[2], this.strength);
        }
        if (!this.choices.contains(InfernalBargainChoice.Draw)) {
            this.description += MessageFormat.format(DESCRIPTIONS[3], this.draw);
        }
        if (!this.choices.contains(InfernalBargainChoice.Damage)) {
            this.description += MessageFormat.format(DESCRIPTIONS[4], this.damage);
        }
    }

    @Override
    public void onInitialApplication() {
        this.trigger();
    }

    @Override
    public void atStartOfTurn() {
        this.trigger();
    }

    private void trigger() {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        if (!this.choices.contains(InfernalBargainChoice.Block)) {
            choices.add(new InfernalBargain.InfernalBargainBlockOption(this, this.block));
        }
        if (!this.choices.contains(InfernalBargainChoice.Strength)) {
            choices.add(new InfernalBargain.InfernalBargainStrengthOption(this, this.strength));
        }
        if (!this.choices.contains(InfernalBargainChoice.Draw)) {
            choices.add(new InfernalBargain.InfernalBargainDrawOption(this, this.draw));
        }
        if (!this.choices.contains(InfernalBargainChoice.Damage)) {
            choices.add(new InfernalBargain.InfernalBargainDamageOption(this, this.damage));
        }
        if (!choices.isEmpty()) {
            this.addToBot(new ChooseOneAction(choices));
        }
    }

    public void recordChoice(InfernalBargainChoice choice) {
        this.choices.add(choice);
        if (this.choices.size() >= 4) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
