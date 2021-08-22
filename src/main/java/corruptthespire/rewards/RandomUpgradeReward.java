package corruptthespire.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import corruptthespire.cards.CardUtil;

import java.text.MessageFormat;

public class RandomUpgradeReward extends CustomReward {
    public static String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:Rewards").TEXT;
    //TODO Better icon
    private static final Texture ICON = ImageMaster.TP_HP;

    public RandomUpgradeReward() {
        super(ICON, TEXT[1], CustomRewardTypes.CORRUPTTHESPIRE_RANDOMUPGRADE);
    }

    @Override
    public boolean claimReward() {
        CardUtil.upgradeRandomCard();
        return true;
    }
}
