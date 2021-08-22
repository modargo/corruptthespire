package corruptthespire;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardSave;
import corruptthespire.buttons.CorruptionDisplay;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.rewards.CustomRewardTypes;
import corruptthespire.rewards.MaxHealthReward;
import corruptthespire.rewards.RandomUpgradeReward;
import corruptthespire.savables.SavableCorruptedRelicPool;
import corruptthespire.savables.SavableCorruption;
import corruptthespire.savables.SavableRng;
import corruptthespire.subscribers.CorruptionHealthIncreaseOnStartBattleSubscriber;
import corruptthespire.subscribers.ResetIsBossCorruptedSubscriber;
import corruptthespire.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.megacrit.cardcrawl.core.Settings.GameLanguage;
import static com.megacrit.cardcrawl.core.Settings.language;

@SpireInitializer
public class CorruptTheSpire implements
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber {

    public static final Logger logger = LogManager.getLogger(CorruptTheSpire.class.getName());

    public CorruptTheSpire() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new CorruptTheSpire();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("corruptthespire/images/CorruptTheSpireBadge.png");
        BaseMod.registerModBadge(badgeTexture, "Corrupt the Spire", "modargo", "TODO", new ModPanel());

        BaseMod.addSaveField(SavableCorruptedRelicPool.SaveKey, new SavableCorruptedRelicPool());
        BaseMod.addSaveField(SavableCorruption.SaveKey, new SavableCorruption());
        BaseMod.addSaveField(SavableRng.SaveKey, new SavableRng());

        BaseMod.subscribe(new CorruptionHealthIncreaseOnStartBattleSubscriber());
        BaseMod.subscribe(new ResetIsBossCorruptedSubscriber());

        this.registerCustomRewards();

        Cor.display = new CorruptionDisplay();
        BaseMod.addTopPanelItem(Cor.display);
    }

    private void registerCustomRewards() {
        BaseMod.registerCustomReward(
            CustomRewardTypes.CORRUPTTHESPIRE_MAXHEALTH,
            (rewardSave) -> new MaxHealthReward(rewardSave.amount),
            (customReward) -> new RewardSave(customReward.type.toString(), null, ((MaxHealthReward)customReward).amount, 0));
        BaseMod.registerCustomReward(
            CustomRewardTypes.CORRUPTTHESPIRE_RANDOMUPGRADE,
            (rewardSave) -> new RandomUpgradeReward(),
            (customReward) -> new RewardSave(customReward.type.toString(), null, 0, 0));
    }

    @Override
    public void receiveEditCards() {
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new FragmentOfCorruption(), RelicType.SHARED);
        for (AbstractRelic r : Cor.getAllCorruptedRelics()) {
            BaseMod.addRelic(r, RelicType.SHARED);
        }
    }

    private static String makeLocPath(Settings.GameLanguage language, String filename)
    {
        String ret = "localization/";
        switch (language) {
            default:
                ret += "eng";
                break;
        }
        return "corruptthespire/" + ret + "/" + filename + ".json";
    }

    private void loadLocFiles(GameLanguage language)
    {
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocPath(language, "CorruptTheSpire-Card-Strings"));
        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(language, "CorruptTheSpire-Event-Strings"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, makeLocPath(language, "CorruptTheSpire-Monster-Strings"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocPath(language, "CorruptTheSpire-Relic-Strings"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocPath(language, "CorruptTheSpire-Power-Strings"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(language, "CorruptTheSpire-ui"));
        // We use the orb strings file as a convenient place to put other text
        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(language, "CorruptTheSpire-Orb-Strings"));
    }

    @Override
    public void receiveEditStrings()
    {
        loadLocFiles(GameLanguage.ENG);
        if (language != GameLanguage.ENG) {
            loadLocFiles(language);
        }
    }

    @Override
    public void receiveEditKeywords() {
    }

    public static String cardImage(String id) {
        return "corruptthespire/images/cards/" + removeModId(id) + ".png";
    }
    public static String eventImage(String id) {
        return "corruptthespire/images/events/" + removeModId(id) + ".png";
    }
    public static String relicImage(String id) {
        return "corruptthespire/images/relics/" + removeModId(id) + ".png";
    }
    public static String powerImage32(String id) {
        return "corruptthespire/images/powers/" + removeModId(id) + "32.png";
    }
    public static String powerImage84(String id) {
        return "corruptthespire/images/powers/" + removeModId(id) + "84.png";
    }
    public static String monsterImage(String id) {
        return "corruptthespire/images/monsters/" + removeModId(id) + "/" + removeModId(id) + ".png";
    }
    public static String relicOutlineImage(String id) {
        return "corruptthespire/images/relics/outline/" + removeModId(id) + ".png";
    }
    public static String uiImage(String id) {
        return "corruptthespire/images/ui/" + removeModId(id) + ".png";
    }

    public static String removeModId(String id) {
        if (id.startsWith("CorruptTheSpire:")) {
            return id.substring(id.indexOf(':') + 1);
        } else {
            logger.warn("Missing mod id on: " + id);
            return id;
        }
    }

    public static String addModId(String id) {
        if (id.startsWith("CorruptTheSpire:")) {
            return id;
        } else {
            return "CorruptTheSpire:" + id;
        }
    }

    public static void LoadPowerImage(AbstractPower power) {
        Texture tex84 = TextureLoader.getTexture(CorruptTheSpire.powerImage84(power.ID));
        Texture tex32 = TextureLoader.getTexture(CorruptTheSpire.powerImage32(power.ID));
        power.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        power.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }
}