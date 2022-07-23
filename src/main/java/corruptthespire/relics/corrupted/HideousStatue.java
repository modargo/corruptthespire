package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.potions.PotionUtil;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HideousStatue extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:HideousStatue";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    public static final int FIGHTS_PER_POTION = 3;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String POTIONS_STAT = "potions";

    public HideousStatue() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    public static void handleRewards() {
        if (Cor.getActNum() >= 3 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            return;
        }

        HideousStatue r = (HideousStatue)AbstractDungeon.player.getRelic(HideousStatue.ID);
        if (r != null) {
            if (r.counter + 1 >= FIGHTS_PER_POTION) {
                incrementPotionsStat();
                AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(PotionUtil.getRandomCorruptedPotion(Cor.rewardRng)));
                r.counter = 0;
            }
            else {
                r.counter++;
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.actNum < 3;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], FIGHTS_PER_POTION);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HideousStatue();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(POTIONS_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(POTIONS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(POTIONS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(POTIONS_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementPotionsStat() {
        stats.put(POTIONS_STAT, stats.getOrDefault(POTIONS_STAT, 0) + 1);
    }
}
