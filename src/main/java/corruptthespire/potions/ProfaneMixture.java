package corruptthespire.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import corruptthespire.patches.potions.CorruptedPotionRarityEnum;
import corruptthespire.powers.PowerUtil;

import java.text.MessageFormat;

public class ProfaneMixture extends CustomPotion {
    public static final String POTION_ID = "CorruptTheSpire:ProfaneMixture";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    private static final int AMOUNT = 1;

    public ProfaneMixture() {
        super(potionStrings.NAME, POTION_ID, CorruptedPotionRarityEnum.CORRUPTED, PotionSize.MOON, PotionColor.SMOKE);
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = MessageFormat.format(potionStrings.DESCRIPTIONS[0], this.potency);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:hand\u00a0of\u00a0the\u00a0abyss")), BaseMod.getKeywordDescription("corruptthespire:hand\u00a0of\u00a0the\u00a0abyss")));
    }

    @Override
    public void use(AbstractCreature target) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, PowerUtil.handOfTheAbyss(AbstractDungeon.player, this.potency)));
    }

    @Override
    public int getPotency(int i) {
        return AMOUNT;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ProfaneMixture();
    }
}
