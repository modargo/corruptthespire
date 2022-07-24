package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GnosticBlessing extends CustomRelic {
    public static final String ID = "CorruptTheSpire:GnosticBlessing";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int DRAW = 1;
    private static final int METALLICIZE = 2;
    private static final int REWARDS = 1;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String OPTIONS_LOST_STAT = "optionsLost";

    public GnosticBlessing() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.masterHandSize += DRAW;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.masterHandSize -= DRAW;
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MetallicizePower(AbstractDungeon.player, METALLICIZE)));
    }

    @Override
    public void atTurnStart() {
        this.flash();
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        incrementOptionsLostStat();
        return numberOfCards - REWARDS;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], DRAW, METALLICIZE, REWARDS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GnosticBlessing();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(OPTIONS_LOST_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(OPTIONS_LOST_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(OPTIONS_LOST_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(OPTIONS_LOST_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementOptionsLostStat() {
        stats.put(OPTIONS_LOST_STAT, stats.getOrDefault(OPTIONS_LOST_STAT, 0) + REWARDS);
    }
}
