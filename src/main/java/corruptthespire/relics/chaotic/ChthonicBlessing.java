package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChthonicBlessing extends CustomRelic {
    public static final String ID = "CorruptTheSpire:ChthonicBlessing";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int REWARDS = 1;
    private static final int GOLD = 15;
    private static final int COMBATS = 6;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String GOLD_GAINED_STAT = "goldGained";
    private static final String RELICS_GAINED_STAT = "relicsGained";
    private static final String OPTIONS_LOST_STAT = "optionsLost";

    public ChthonicBlessing() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
            this.counter++;
        }
    }

    public void addRewards() {
        incrementGoldGainedStat();
        AbstractDungeon.getCurrRoom().addGoldToRewards(GOLD);
        if (this.counter >= COMBATS) {
            incrementRelicsGainedStat();
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
            this.counter -= COMBATS;
        }
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        incrementOptionsLostStat();
        return numberOfCards - REWARDS;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], GOLD, COMBATS, REWARDS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ChthonicBlessing();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(GOLD_GAINED_STAT), stats.get(RELICS_GAINED_STAT), stats.get(OPTIONS_LOST_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(GOLD_GAINED_STAT, 0);
        stats.put(RELICS_GAINED_STAT, 0);
        stats.put(OPTIONS_LOST_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(GOLD_GAINED_STAT));
        statsToSave.add(stats.get(RELICS_GAINED_STAT));
        statsToSave.add(stats.get(OPTIONS_LOST_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(GOLD_GAINED_STAT, jsonArray.get(0).getAsInt());
            stats.put(RELICS_GAINED_STAT, jsonArray.get(1).getAsInt());
            stats.put(OPTIONS_LOST_STAT, jsonArray.get(2).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementGoldGainedStat() {
        stats.put(GOLD_GAINED_STAT, stats.getOrDefault(GOLD_GAINED_STAT, 0) + GOLD);
    }

    public static void incrementRelicsGainedStat() {
        stats.put(RELICS_GAINED_STAT, stats.getOrDefault(RELICS_GAINED_STAT, 0) + 1);
    }

    public static void incrementOptionsLostStat() {
        stats.put(OPTIONS_LOST_STAT, stats.getOrDefault(OPTIONS_LOST_STAT, 0) + REWARDS);
    }
}
