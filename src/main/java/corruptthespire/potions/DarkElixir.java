package corruptthespire.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import corruptthespire.actions.GainCorruptionAction;
import corruptthespire.patches.potions.CorruptedPotionRarityEnum;

import java.text.MessageFormat;

public class DarkElixir extends CustomPotion {
    public static final String POTION_ID = "CorruptTheSpire:DarkElixir";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    private static final int AMOUNT = 2;

    public DarkElixir() {
        super(potionStrings.NAME, POTION_ID, CorruptedPotionRarityEnum.CORRUPTED, PotionSize.S, PotionColor.SMOKE);
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = MessageFormat.format(potionStrings.DESCRIPTIONS[0], this.potency);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]), GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0])));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.DEXTERITY.NAMES[0]), GameDictionary.keywords.get(GameDictionary.DEXTERITY.NAMES[0])));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:corruption")), BaseMod.getKeywordDescription("corruptthespire:corruption")));
    }

    @Override
    public void use(AbstractCreature target) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.potency)));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.potency)));
        this.addToBot(new GainCorruptionAction(this.potency));
    }

    @Override
    public int getPotency(int i) {
        return AMOUNT;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new DarkElixir();
    }
}
