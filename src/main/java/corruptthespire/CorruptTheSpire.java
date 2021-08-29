package corruptthespire;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.util.Condition;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardSave;
import corruptthespire.buttons.CorruptionDisplay;
import corruptthespire.cards.*;
import corruptthespire.cards.corrupted.CorruptedCardColor;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.events.CorruptedEventInfo;
import corruptthespire.events.CorruptedEventUtil;
import corruptthespire.monsters.*;
import corruptthespire.patches.cards.CheckFatedPostBattleSubscriber;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.relics.chaotic.BurningRing;
import corruptthespire.relics.chaotic.DeckOfManyFates;
import corruptthespire.rewards.CustomRewardTypes;
import corruptthespire.rewards.MaxHealthReward;
import corruptthespire.rewards.RandomUpgradeReward;
import corruptthespire.savables.*;
import corruptthespire.subscribers.CorruptionHealthIncreaseOnStartBattleSubscriber;
import corruptthespire.subscribers.ResetIsBossCorruptedSubscriber;
import corruptthespire.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    public static final String AbyssModId = "Abyss";

    public CorruptTheSpire() {
        BaseMod.addColor(CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED,
                Color.BLACK, Color.BLACK, Color.BLACK,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                "corruptthespire/images/corruptedcolor/corrupted_attack.png", "corruptthespire/images/corruptedcolor/corrupted_skill.png", "corruptthespire/images/corruptedcolor/corrupted_power.png", "corruptthespire/images/corruptedcolor/corrupted_energy.png",
                "corruptthespire/images/corruptedcolor/corrupted_attack_p.png", "corruptthespire/images/corruptedcolor/corrupted_skill_p.png", "corruptthespire/images/corruptedcolor/corrupted_power_p.png",
                "corruptthespire/images/corruptedcolor/corrupted_energy_p.png", "corruptthespire/images/corruptedcolor/corrupted_small_energy.png");
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new CorruptTheSpire();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("corruptthespire/images/CorruptTheSpireBadge.png");
        BaseMod.registerModBadge(badgeTexture, "Corrupt the Spire", "modargo", "Adds corrupted rooms to the Spire, which have increased rewards and new encounters, at the cost of increasing the corruption of the Spire and making all enemies more dangerous.", new ModPanel());

        addMonsters();
        addEvents();

        BaseMod.addSaveField(SavableCorruptedRelicPool.SaveKey, new SavableCorruptedRelicPool());
        BaseMod.addSaveField(SavableCorruption.SaveKey, new SavableCorruption());
        BaseMod.addSaveField(SavableCorruptionFlags.SaveKey, new SavableCorruptionFlags());
        BaseMod.addSaveField(SavableEventList.SaveKey, new SavableEventList());
        BaseMod.addSaveField(SavableRng.SaveKey, new SavableRng());

        BaseMod.subscribe(new CorruptionHealthIncreaseOnStartBattleSubscriber());
        BaseMod.subscribe(new ResetIsBossCorruptedSubscriber());
        BaseMod.subscribe(new CheckFatedPostBattleSubscriber());

        this.registerCustomRewards();

        Cor.display = new CorruptionDisplay();
        BaseMod.addTopPanelItem(Cor.display);
    }

    private static void addMonsters() {
        BaseMod.addMonster(MasterOfTimeAndSpace.ID, () -> new MasterOfTimeAndSpace(-400.0F, 40.0F));
        BaseMod.addMonster(PandemoniumArchfiend.ID, () -> new PandemoniumArchfiend(0.0F, 0.0F));
        BaseMod.addMonster(Encounters.TREASURE_WARDENS_ACT1, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new TreasureWardenWhite(-375.0F, 50.0F, TreasureWardenWhite.Version.Act1),
                        new TreasureWardenBlack(175.0F, 50.0F, TreasureWardenBlack.Version.Act1),
                }));
        BaseMod.addMonster(Encounters.TREASURE_WARDENS_ACT2, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new TreasureWardenWhite(-375.0F, 50.0F, TreasureWardenWhite.Version.Act2),
                        new TreasureWardenBlack(175.0F, 50.0F, TreasureWardenBlack.Version.Act2),
                }));
        BaseMod.addMonster(Encounters.TREASURE_WARDENS_ACT3, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new TreasureWardenWhite(-375.0F, 50.0F, TreasureWardenWhite.Version.Act3),
                        new TreasureWardenBlack(175.0F, 50.0F, TreasureWardenBlack.Version.Act3),
                }));
    }

    private static void addEvents() {
        // These events are only encountered through our own special logic, but we register them all here for ease of
        // debugging (thus the conditions that make them never show up)
        for (Map.Entry<String, CorruptedEventInfo> e : CorruptedEventUtil.getAllCorruptedEvents().entrySet()) {
            Condition alwaysFalseCondition = () -> false;
            BaseMod.addEvent(new AddEventParams.Builder(e.getKey(), e.getValue().cls).spawnCondition(alwaysFalseCondition).bonusCondition(alwaysFalseCondition).create());
        }
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
        BaseMod.addCard(new Nudge());
        BaseMod.addCard(new ShimmeringShield());
        BaseMod.addCard(new WheelOfFortune());
        BaseMod.addCard(new Bedeviled());
        BaseMod.addCard(new Fated());
        for (AbstractCard c : CorruptedCardUtil.getAllCorruptedCards()) {
            BaseMod.addCard(c);
        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new BurningRing(), RelicType.SHARED);
        BaseMod.addRelic(new DeckOfManyFates(), RelicType.SHARED);
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
        Gson gson = new Gson();
        String json = Gdx.files.internal(makeLocPath(Settings.language, "CorruptTheSpire-Keyword-Strings")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                //The modID here must be lowercase
                BaseMod.addKeyword("corruptthespire", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
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

    private static class Keyword
    {
        public String PROPER_NAME;
        public String[] NAMES;
        public String DESCRIPTION;
    }
}