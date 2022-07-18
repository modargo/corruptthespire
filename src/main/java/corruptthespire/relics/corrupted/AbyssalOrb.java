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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.PowerUtil;
import corruptthespire.util.TextureLoader;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbyssalOrb extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:AbyssalOrb";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int ABYSSTOUCHED_AT_START_OF_TURN = 1;
    public static final int ABYSSTOUCHED_INCREASE = 1;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String ABYSSTOUCHED_STAT = "abysstouched";

    public AbyssalOrb() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("corruptthespire:abysstouched")), BaseMod.getKeywordDescription("corruptthespire:abysstouched")));
    }

    @Override
    public void atTurnStart() {
        this.flash();
        AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (m != null) {
            incrementAbysstouchedStat(ABYSSTOUCHED_AT_START_OF_TURN);
            this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, PowerUtil.abysstouched(m, ABYSSTOUCHED_AT_START_OF_TURN)));
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], ABYSSTOUCHED_AT_START_OF_TURN, ABYSSTOUCHED_INCREASE);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AbyssalOrb();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(ABYSSTOUCHED_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float abysstouched = stats.get(ABYSSTOUCHED_STAT);
        String abysstouchedPerTurn = format.format(abysstouched / Math.max(totalTurns, 1));
        String abysstouchedPerCombat = format.format(abysstouched / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], abysstouchedPerTurn, abysstouchedPerCombat);
    }

    public void resetStats() {
        stats.put(ABYSSTOUCHED_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(ABYSSTOUCHED_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(ABYSSTOUCHED_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementAbysstouchedStat(int amount) {
        stats.put(ABYSSTOUCHED_STAT, stats.getOrDefault(ABYSSTOUCHED_STAT, 0) + amount);
    }
}
