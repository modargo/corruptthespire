package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
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

public class BeanSprouts extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:BeanSprouts";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    public static final int STRENGTH = 1;
    public static final int BLOCK = 3;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String STRENGTH_STAT = "strength";
    private static final String BLOCK_STAT = "block";

    public BeanSprouts() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], STRENGTH, BLOCK);
    }

    public void trigger() {
        incrementStrengthAndBlockStat();
        this.flash();
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NextTurnBlockPower(AbstractDungeon.player, BLOCK)));
        }
        else {
            this.addToTop(new GainBlockAction(AbstractDungeon.player, BLOCK));
        }
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1)));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BeanSprouts();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(STRENGTH_STAT), stats.get(BLOCK_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float strength = stats.get(STRENGTH_STAT);
        float block = stats.get(BLOCK_STAT);
        String strengthPerTurn = format.format(strength / Math.max(totalTurns, 1));
        String blockPerTurn = format.format(block / Math.max(totalTurns, 1));
        String strengthPerCombat = format.format(strength / Math.max(totalCombats, 1));
        String blockPerCombat = format.format(block / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], strengthPerTurn, blockPerTurn, strengthPerCombat, blockPerCombat);
    }

    public void resetStats() {
        stats.put(STRENGTH_STAT, 0);
        stats.put(BLOCK_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STRENGTH_STAT));
        statsToSave.add(stats.get(BLOCK_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STRENGTH_STAT, jsonArray.get(0).getAsInt());
            stats.put(BLOCK_STAT, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementStrengthAndBlockStat() {
        stats.put(STRENGTH_STAT, stats.getOrDefault(STRENGTH_STAT, 0) + STRENGTH);
        stats.put(BLOCK_STAT, stats.getOrDefault(BLOCK_STAT, 0) + BLOCK);
    }
}
