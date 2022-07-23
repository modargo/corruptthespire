package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
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

public class Gohei extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:Gohei";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int DAMAGE_THRESHOLD = 10;
    private static final int DAMAGE_REDUCTION = 5;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String DAMAGE_PREVENTED_STAT = "damagePrevented";
    private static final String WOUNDS_STAT = "wounds";

    public Gohei() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], DAMAGE_THRESHOLD, DAMAGE_REDUCTION);
    }

    public int onAttackedAfterToriiBeforeTungstenRod(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > DAMAGE_THRESHOLD) {
            incrementStats();
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new MakeTempCardInDiscardAction(new Wound(), 1));
            return damageAmount - DAMAGE_REDUCTION;
        } else {
            return damageAmount;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Gohei();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(DAMAGE_PREVENTED_STAT), stats.get(WOUNDS_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float strength = stats.get(DAMAGE_PREVENTED_STAT);
        float block = stats.get(WOUNDS_STAT);
        String damagePreventedPerTurn = format.format(strength / Math.max(totalTurns, 1));
        String woundsPerTurn = format.format(block / Math.max(totalTurns, 1));
        String damagePreventedPerCombat = format.format(strength / Math.max(totalCombats, 1));
        String woundsPerCombat = format.format(block / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], damagePreventedPerTurn, woundsPerTurn, damagePreventedPerCombat, woundsPerCombat);
    }

    public void resetStats() {
        stats.put(DAMAGE_PREVENTED_STAT, 0);
        stats.put(WOUNDS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DAMAGE_PREVENTED_STAT));
        statsToSave.add(stats.get(WOUNDS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DAMAGE_PREVENTED_STAT, jsonArray.get(0).getAsInt());
            stats.put(WOUNDS_STAT, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementStats() {
        stats.put(DAMAGE_PREVENTED_STAT, stats.getOrDefault(DAMAGE_PREVENTED_STAT, 0) + DAMAGE_REDUCTION);
        stats.put(WOUNDS_STAT, stats.getOrDefault(WOUNDS_STAT, 0) + 1);
    }
}
