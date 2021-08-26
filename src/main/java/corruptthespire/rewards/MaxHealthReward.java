package corruptthespire.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.text.MessageFormat;

public class MaxHealthReward extends CustomReward {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:Rewards").TEXT;
    //TODO Better icon
    private static final Texture ICON = ImageMaster.TP_HP;

    public final int amount;

    public MaxHealthReward(int amount) {
        super(ICON, MessageFormat.format(TEXT[0], amount), CustomRewardTypes.CORRUPTTHESPIRE_MAXHEALTH);
        this.amount = amount;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.player.increaseMaxHp(this.amount, true);
        return true;
    }
}
