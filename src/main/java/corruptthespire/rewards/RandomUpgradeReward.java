package corruptthespire.rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import corruptthespire.Cor;
import corruptthespire.cards.CardUtil;

public class RandomUpgradeReward extends AbstractCorruptTheSpireReward {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:Rewards").TEXT;
    private static final Texture ICON = ImageMaster.loadImage("images/ui/run_mods/shiny.png");

    public RandomUpgradeReward() {
        super(ICON, TEXT[1], CustomRewardTypes.CORRUPTTHESPIRE_RANDOMUPGRADE);
    }

    @Override
    public boolean claimReward() {
        CardUtil.upgradeRandomCard(Cor.rewardRng);
        return true;
    }
}
