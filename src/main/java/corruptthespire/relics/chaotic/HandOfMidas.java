package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandOfMidas extends CustomRelic {
    public static final String ID = "CorruptTheSpire:HandOfMidas";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int GOLD = 175;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String RELICS_STAT = "relics";

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
            incrementRelicsStat();
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

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(RELICS_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(RELICS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(RELICS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(RELICS_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementRelicsStat() {
        stats.put(RELICS_STAT, stats.getOrDefault(RELICS_STAT, 0) + 1);
    }
}
