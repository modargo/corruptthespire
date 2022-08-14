package corruptthespire.rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import corruptthespire.Cor;
import corruptthespire.cards.CardUtil;
import corruptthespire.savables.logs.RandomUpgradeRewardPerFloorLog;

import java.util.List;

public class RandomUpgradeReward extends AbstractCorruptTheSpireReward {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:Rewards").TEXT;
    private static final Texture ICON = ImageMaster.loadImage("images/ui/run_mods/shiny.png");

    public RandomUpgradeReward() {
        super(ICON, TEXT[1], CustomRewardTypes.CORRUPTTHESPIRE_RANDOMUPGRADE);
    }

    @Override
    public boolean claimReward() {
        AbstractCard c = CardUtil.upgradeRandomCard(Cor.rewardRng);
        if (c != null) {
            List<List<String>> logs = RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog;
            if (logs != null && logs.size() > 0) {
                List<String> log = logs.get(logs.size() - 1);
                if (log != null) {
                    log.add(c.cardID);
                }
            }
        }
        return true;
    }
}
