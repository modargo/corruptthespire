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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import corruptthespire.patches.potions.CorruptedPotionRarityEnum;
import corruptthespire.powers.PowerUtil;

import java.text.MessageFormat;

public class AbyssalBrew extends CustomPotion {
    public static final String POTION_ID = "CorruptTheSpire:AbyssalBrew";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    private static final int AYBSSTOUCHED = 6;

    public AbyssalBrew() {
        super(potionStrings.NAME, POTION_ID, CorruptedPotionRarityEnum.CORRUPTED, PotionSize.H, PotionColor.SMOKE);
        this.isThrown = true;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = MessageFormat.format(potionStrings.DESCRIPTIONS[0], this.potency);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:abysstouched")), BaseMod.getKeywordDescription("corruptthespire:abysstouched")));
    }

    @Override
    public void use(AbstractCreature target) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, PowerUtil.abysstouched(m, this.potency)));
        }
    }

    @Override
    public int getPotency(int i) {
        return AYBSSTOUCHED;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new AbyssalBrew();
    }
}
