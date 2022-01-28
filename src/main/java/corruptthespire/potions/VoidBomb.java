package corruptthespire.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import corruptthespire.Cor;
import corruptthespire.patches.potions.CorruptedPotionRarityEnum;

import java.text.MessageFormat;

public class VoidBomb extends CustomPotion {
    public static final String POTION_ID = "CorruptTheSpire:VoidBomb";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    private static final int CORRUPTION_PERCENT = 30;

    public VoidBomb() {
        super(potionStrings.NAME, POTION_ID, CorruptedPotionRarityEnum.CORRUPTED, PotionSize.SPHERE, PotionColor.SMOKE);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = MessageFormat.format(potionStrings.DESCRIPTIONS[0], this.potency);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:corruption")), BaseMod.getKeywordDescription("corruptthespire:corruption")));
    }

    @Override
    public void use(AbstractCreature target) {
        int damage = (int)(Cor.corruption * this.potency / 100.0f);
        DamageInfo info = new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS);
        info.applyEnemyPowersOnly(target);
        this.addToBot(new DamageAction(target, info, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public int getPotency(int i) {
        return CORRUPTION_PERCENT;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new VoidBomb();
    }
}
