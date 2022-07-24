package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OozingHeart extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:OozingHeart";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int HEAL = 16;
    private static final int HEALTH_LOSS = 1;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String HEALING_STAT = "healing";
    private static final String DAMAGE_STAT = "damage";

    public OozingHeart() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room instanceof MonsterRoomElite || room.eliteTrigger) {
            incrementHealingStat();
            this.flash();
            this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL, 0.0F));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (AbstractDungeon.player.currentHealth > HEALTH_LOSS) {
            incrementDamageStat();
            this.flash();
            AbstractDungeon.player.damage(new DamageInfo(null, HEALTH_LOSS));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], HEAL, HEALTH_LOSS);
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OozingHeart();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(HEALING_STAT), stats.get(DAMAGE_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(HEALING_STAT, 0);
        stats.put(DAMAGE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(HEALING_STAT));
        statsToSave.add(stats.get(DAMAGE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(HEALING_STAT, jsonArray.get(0).getAsInt());
            stats.put(DAMAGE_STAT, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementHealingStat() {
        stats.put(HEALING_STAT, stats.getOrDefault(HEALING_STAT, 0) + HEAL);
    }

    public static void incrementDamageStat() {
        stats.put(DAMAGE_STAT, stats.getOrDefault(DAMAGE_STAT, 0) + HEALTH_LOSS);
    }
}
