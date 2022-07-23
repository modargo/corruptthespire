package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeavySword extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:HeavySword";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int STRENGTH = 3;
    private static final int TRIGGER_TURN = 4;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String STRENGTH_STAT = "strength";

    public HeavySword() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter != -1) {
            this.counter++;
        }
        if (this.counter >= TRIGGER_TURN) {
            incrementStrengthStat();
            this.flash();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH), STRENGTH));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.counter = -1;
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], STRENGTH);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HeavySword();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(STRENGTH_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float strength = stats.get(STRENGTH_STAT);
        String strengthPerCombat = format.format(strength / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], strengthPerCombat);
    }

    public void resetStats() {
        stats.put(STRENGTH_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STRENGTH_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STRENGTH_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementStrengthStat() {
        stats.put(STRENGTH_STAT, stats.getOrDefault(STRENGTH_STAT, 0) + STRENGTH);
    }
}
