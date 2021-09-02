package corruptthespire.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;

public abstract class AbstractCorruptTheSpireReward extends CustomReward {
    public AbstractCorruptTheSpireReward(Texture icon, String text, RewardType type) {
        super(icon, text, type);
    }
}
