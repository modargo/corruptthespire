package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaskOfNightmares extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:MaskOfNightmares";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int MAX_HP = 2;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String MAX_HP_STAT = "maxHp";

    public MaskOfNightmares() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], MAX_HP);
    }

    public void onEnterCorruptedRoom() {
        incrementMaxHpStat();
        // Some modded relics check for combat by checking the room phase, which isn't actually enough to ensure that
        // doing things like ApplyPowerAction is safe -- to confirm that the fight has been initialized, checking that
        // AbstractDungeon.getMonsters() is non-null is also necessary.
        // Healing from Mask of Nightmares is intended to be non-combat healing anyway, so to enforce that and guard
        // against relics that try to do invalid things, we manipulate the room phase here.
        boolean needToReset = false;
        if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            needToReset = true;
        }
        AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
        if (needToReset) {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
        }
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(MAX_HP_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(MAX_HP_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(MAX_HP_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(MAX_HP_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementMaxHpStat() {
        stats.put(MAX_HP_STAT, stats.getOrDefault(MAX_HP_STAT, 0) + MAX_HP);
    }
}
