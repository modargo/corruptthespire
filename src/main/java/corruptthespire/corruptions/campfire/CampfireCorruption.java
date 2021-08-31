package corruptthespire.corruptions.campfire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import corruptthespire.Cor;
import corruptthespire.corruptions.campfire.options.*;
import corruptthespire.relics.corrupted.ObsidianShovel;

import java.util.ArrayList;
import java.util.Collections;

public class CampfireCorruption {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:CampfireCorruption").TEXT;
    private static final int RANDOM_OPTIONS = 2;

    public static void initializeCampfireInfo(CampfireInfo info) {
        ArrayList<CampfireCorruptionOption> options = generateOptions();

        if (options.contains(CampfireCorruptionOption.CommonRelic)) {
            info.options.add(new GainFragmentOption());
        }
        if (options.contains(CampfireCorruptionOption.CommonRelic)) {
            info.options.add(new CommonRelicOption());
        }
        if (options.contains(CampfireCorruptionOption.UncommonRelic)) {
            info.options.add(new UncommonRelicOption());
        }
        if (options.contains(CampfireCorruptionOption.RareRelic)) {
            info.options.add(new RareRelicOption());
        }
        if (options.contains(CampfireCorruptionOption.CorruptedCard)) {
            info.options.add(new CorruptedCardOption());
        }
        if (options.contains(CampfireCorruptionOption.BuryFragment)) {
            info.options.add(new BuryFragmentOption());
        }
    }

    private static ArrayList<CampfireCorruptionOption> generateOptions() {
        ArrayList<CampfireCorruptionOption> options = new ArrayList<>();
        options.add(CampfireCorruptionOption.GainFragment);
        if (AbstractDungeon.player.hasRelic(ObsidianShovel.ID)) {
            options.add(CampfireCorruptionOption.BuryFragment);
        }

        ArrayList<CampfireCorruptionOption> randomOptions = new ArrayList<>();
        randomOptions.add(CampfireCorruptionOption.CommonRelic);
        randomOptions.add(CampfireCorruptionOption.UncommonRelic);
        randomOptions.add(CampfireCorruptionOption.RareRelic);
        randomOptions.add(CampfireCorruptionOption.CorruptedCard);
        Collections.shuffle(randomOptions, Cor.rng.random);
        for (int i = 0; i < RANDOM_OPTIONS; i++) {
            options.add(randomOptions.get(i));
        }

        return options;
    }
}
