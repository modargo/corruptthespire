package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class MaskOfNightmares extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:MaskOfNightmares";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int MAX_HP = 2;

    public MaskOfNightmares() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], MAX_HP);
    }

    public void onEnterCorruptedRoom(AbstractRoom room) {
        AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
    }
}
