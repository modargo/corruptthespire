package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShimmeringFan extends CustomRelic {
    public static final String ID = "CorruptTheSpire:ShimmeringFan";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int COLORS = 3;
    private static final int BLOCK = 7;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String BLOCK_STAT = "block";

    private final ArrayList<AbstractCard.CardColor> colorsPlayed = new ArrayList<>();

    public ShimmeringFan() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        this.colorsPlayed.clear();
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction useCardAction) {
        if (this.counter >= COLORS) {
            return;
        }
        if (!colorsPlayed.contains(c.color)) {
            colorsPlayed.add(c.color);
            this.counter++;
        }
        if (this.counter == COLORS) {
            incrementBlockStat();
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GainBlockAction(AbstractDungeon.player, BLOCK));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], COLORS, BLOCK);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShimmeringFan();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(BLOCK_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float block = stats.get(BLOCK_STAT);
        String blockPerTurn = format.format(block / Math.max(totalTurns, 1));
        String blockPerCombat = format.format(block / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], blockPerTurn, blockPerCombat);
    }

    public void resetStats() {
        stats.put(BLOCK_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(BLOCK_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(BLOCK_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementBlockStat() {
        stats.put(BLOCK_STAT, stats.getOrDefault(BLOCK_STAT, 0) + BLOCK);
    }
}
