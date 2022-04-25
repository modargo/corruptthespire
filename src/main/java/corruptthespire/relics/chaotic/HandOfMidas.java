package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class HandOfMidas extends CustomRelic {
    public static final String ID = "CorruptTheSpire:HandOfMidas";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int GOLD = 175;

    public HandOfMidas() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof MonsterRoomElite) {
            this.pulse = true;
            this.beginPulse();
        } else {
            this.pulse = false;
        }

    }

    @Override
    public void onVictory() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            this.flash();
            this.pulse = false;
        }

    }

    public static boolean handleEliteReward(AbstractRoom room) {
        if (AbstractDungeon.player.hasRelic(HandOfMidas.ID)) {
            room.addGoldToRewards(GOLD);
            return true;
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], GOLD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfMidas();
    }
}
