package corruptthespire.relics.corrupted;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorruptedEgg extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:CorruptedEgg";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final float UPGRADE_CHANCE_INCREASE = 33;
    private static final int CORRUPTION = 1;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String CORRUPTION_STAT = "corruption";

    public CorruptedEgg() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:corruption")), BaseMod.getKeywordDescription("corruptthespire:corruption")));
    }

    public static float modifyUpgradeChance(float chance) {
        return chance + (AbstractDungeon.player.hasRelic(ID) ? UPGRADE_CHANCE_INCREASE / 100.0F : 0.0F);
    }

    public static void afterObtainCard(AbstractCard c) {
        if (AbstractDungeon.player.hasRelic(ID) && c.upgraded) {
            incrementCorruptionStat();
            Cor.addCorruption(CORRUPTION);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], UPGRADE_CHANCE_INCREASE, CORRUPTION);
    }
    
    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CorruptedEgg();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(CORRUPTION_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(CORRUPTION_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(CORRUPTION_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(CORRUPTION_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementCorruptionStat() {
        stats.put(CORRUPTION_STAT, stats.getOrDefault(CORRUPTION_STAT, 0) + CORRUPTION);
    }
}
