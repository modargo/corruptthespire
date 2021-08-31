package corruptthespire.relics.corrupted;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

public abstract class AbstractCorruptedRelic extends CustomRelic {
    public static final int CORRUPTED_RELIC_PRICE = 200;

    public AbstractCorruptedRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
    }

    @Override
    public int getPrice() {
        return CORRUPTED_RELIC_PRICE;
    }
}
