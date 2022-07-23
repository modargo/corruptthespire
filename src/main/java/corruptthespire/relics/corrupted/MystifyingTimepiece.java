package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MystifyingTimepiece extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:MystifyingTimepiece";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int COST = 0;
    private static final int CARDS = 1;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String DRAW_STAT = "draw";

    private boolean firstTurn = true;

    public MystifyingTimepiece() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
        this.firstTurn = true;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (this.counter == 0 && !this.firstTurn) {
            incrementDrawStat();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, CARDS));
        } else {
            this.firstTurn = false;
        }

        this.counter = 0;
        this.beginLongPulse();
    }


    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.costForTurn == COST) {
            this.counter++;
            this.stopPulse();
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], COST, CARDS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MystifyingTimepiece();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(DRAW_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float draw = stats.get(DRAW_STAT);
        String drawPerTurn = format.format(draw / Math.max(totalTurns, 1));
        String drawPerCombat = format.format(draw / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], drawPerTurn, drawPerCombat);
    }

    public void resetStats() {
        stats.put(DRAW_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DRAW_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DRAW_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementDrawStat() {
        stats.put(DRAW_STAT, stats.getOrDefault(DRAW_STAT, 0) + CARDS);
    }
}
