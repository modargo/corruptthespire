package corruptthespire.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import corruptthespire.actions.ChooseOneCorruptedAction;
import corruptthespire.patches.potions.CorruptedPotionRarityEnum;

import java.text.MessageFormat;

public class CorruptedPotion extends CustomPotion {
    public static final String POTION_ID = "CorruptTheSpire:CorruptedPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    private static final int AMOUNT = 1;

    public CorruptedPotion() {
        super(potionStrings.NAME, POTION_ID, CorruptedPotionRarityEnum.CORRUPTED, PotionSize.CARD, PotionColor.SMOKE);
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = this.potency == 1 ? potionStrings.DESCRIPTIONS[0] : MessageFormat.format(potionStrings.DESCRIPTIONS[1], this.potency);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            this.addToBot(new ChooseOneCorruptedAction(this.potency, true));
        }
    }

    @Override
    public int getPotency(int i) {
        return AMOUNT;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new CorruptedPotion();
    }
}
