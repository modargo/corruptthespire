package corruptthespire.corruptions.campfire;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import corruptthespire.Cor;
import corruptthespire.corruptions.campfire.options.*;
import corruptthespire.patches.core.CorruptedField;
import corruptthespire.patches.campfire.CampfireInfoField;
import corruptthespire.relics.corrupted.ObsidianShovel;
import corruptthespire.util.CollectionsUtil;

import java.util.ArrayList;
import java.util.Collections;

public class CampfireCorruption {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:CampfireCorruption").TEXT;
    private static final int RANDOM_OPTIONS = 2;

    public static void initializeCampfireInfo(CampfireInfo info) {
        ArrayList<CampfireCorruptionOption> options = generateOptions();

        if (options.contains(CampfireCorruptionOption.GainFragment)) {
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
        if (options.contains(CampfireCorruptionOption.RareCard)) {
            info.options.add(new RareCardOption());
        }
        if (options.contains(CampfireCorruptionOption.CorruptedRelic)) {
            info.options.add(new CorruptedRelicOption());
        }
    }

    private static ArrayList<CampfireCorruptionOption> generateOptions() {
        ArrayList<CampfireCorruptionOption> options = new ArrayList<>();
        options.add(CampfireCorruptionOption.GainFragment);
        if (AbstractDungeon.player.hasRelic(ObsidianShovel.ID)) {
            options.add(CampfireCorruptionOption.CorruptedRelic);
        }

        ArrayList<CampfireCorruptionOption> randomOptions = new ArrayList<>();
        randomOptions.add(CampfireCorruptionOption.CommonRelic);
        randomOptions.add(CampfireCorruptionOption.UncommonRelic);
        randomOptions.add(CampfireCorruptionOption.RareRelic);
        randomOptions.add(CampfireCorruptionOption.CorruptedCard);
        randomOptions.add(CampfireCorruptionOption.RareCard);
        CollectionsUtil.shuffle(randomOptions, Cor.rng);
        for (int i = 0; i < RANDOM_OPTIONS; i++) {
            options.add(randomOptions.get(i));
        }

        return options;
    }

    public static boolean handleInitializeButtons(CampfireUI campfireUI) {
        if (!CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
            return false;
        }
        CampfireInfo campfireInfo = CampfireInfoField.campfireInfo.get(campfireUI);
        CampfireCorruption.initializeCampfireInfo(campfireInfo);
        ArrayList<AbstractCampfireOption> buttons = ReflectionHacks.getPrivate(campfireUI, CampfireUI.class, "buttons");
        buttons.addAll(campfireInfo.options);
        return true;
    }
}
