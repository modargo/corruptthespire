package corruptthespire.relics.corrupted;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.BagOfTricksAfterImagePower;
import corruptthespire.util.TextureLoader;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BagOfTricks extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:BagOfTricks";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int AFTERIMAGE = 1;
    private static final int DRAW_REDUCTION = 1;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String BLOCK_STAT = "block";

    public BagOfTricks() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:after\u00a0image")), BaseMod.getKeywordDescription("corruptthespire:after\u00a0image")));
    }

    @Override
    public void atBattleStartPreDraw() {
        AbstractDungeon.player.gameHandSize -= DRAW_REDUCTION;
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BagOfTricksAfterImagePower(AbstractDungeon.player, AFTERIMAGE), AFTERIMAGE));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.player.gameHandSize += DRAW_REDUCTION;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], AFTERIMAGE, DRAW_REDUCTION);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BagOfTricks();
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

    public static void incrementBlockStat(int amount) {
        stats.put(BLOCK_STAT, stats.getOrDefault(BLOCK_STAT, 0) + amount);
    }
}
