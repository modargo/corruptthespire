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
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import corruptthespire.buttons.CorruptionDisplay;
import corruptthespire.cards.*;
import corruptthespire.cards.corrupted.CorruptedCardColor;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.events.CorruptedEventInfo;
import corruptthespire.events.CorruptedEventUtil;
import corruptthespire.events.special.DevourerEvent;
import corruptthespire.events.special.DoomEvent;
import corruptthespire.events.special.HarbingerEvent;
import corruptthespire.monsters.*;
import corruptthespire.patches.run.CorruptionTypePerFloorRunHistoryPatch;
import corruptthespire.potions.*;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.relics.chaotic.*;
import corruptthespire.relics.elite.*;
import corruptthespire.rewards.CorruptedCardReward;
import corruptthespire.rewards.CustomRewardTypes;
import corruptthespire.rewards.MaxHealthReward;
import corruptthespire.rewards.RandomUpgradeReward;
import corruptthespire.savables.*;
import corruptthespire.savables.logs.*;
import corruptthespire.subscribers.*;
import corruptthespire.util.TextureLoader;
import corruptthespire.variables.MagicNumber2Variable;
import corruptthespire.variables.MagicNumber3Variable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.core.Settings.GameLanguage;
import static com.megacrit.cardcrawl.core.Settings.language;

@SpireInitializer
public class CorruptTheSpire implements
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber {
    private static final Logger logger = LogManager.getLogger(CorruptTheSpire.class.getName());

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
        Config.initialize();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("corruptthespire/images/CorruptTheSpireBadge.png");
        BaseMod.registerModBadge(badgeTexture, "Corrupt the Spire", "modargo", "Adds corrupted rooms to the Spire, which have increased rewards and new encounters, at the cost of increasing the corruption of the Spire and making all enemies more dangerous.", new ModPanel());

        addMonsters();
        addEvents();
        addPotions();

        BaseMod.addSaveField(SavableActive.SaveKey, new SavableActive());
        BaseMod.addSaveField(SavableCorruptedRelicPool.SaveKey, new SavableCorruptedRelicPool());
        BaseMod.addSaveField(SavableCorruption.SaveKey, new SavableCorruption());
        BaseMod.addSaveField(SavableCorruptionFlags.SaveKey, new SavableCorruptionFlags());
        BaseMod.addSaveField(SavableEventList.SaveKey, new SavableEventList());
        BaseMod.addSaveField(SavableRng.SaveKey, new SavableRng());

        BaseMod.addSaveField(CorruptionPerFloorLog.SaveKey, new CorruptionPerFloorLog());
        BaseMod.addSaveField(CorruptionTypePerFloorLog.SaveKey, new CorruptionTypePerFloorLog());
        BaseMod.addSaveField(RandomUpgradeRewardPerFloorLog.SaveKey, new RandomUpgradeRewardPerFloorLog());
        BaseMod.addSaveField(ShopServiceLog.SaveKey, new ShopServiceLog());

        BaseMod.subscribe(new ApplyCorruptionHealthIncreaseOnStartBattleSubscriber());
        BaseMod.subscribe(new ApplyCorruptionsOnStartBattleSubscriber());
        BaseMod.subscribe(new CheckFatedPostBattleSubscriber());
        BaseMod.subscribe(new IncreaseInvincibleOnStartBattleSubscriber());
        BaseMod.subscribe(new ResetIsBossCorruptedSubscriber());
        BaseMod.subscribe(new ResetNormalMonsterCountSubscriber());
        BaseMod.subscribe(new ResetPathsInSmokeSubscriber());
        BaseMod.subscribe(new TriggerMonsterPowersPostDrawSubscriber());

        this.registerCustomRewards();

        Cor.display = new CorruptionDisplay();
        BaseMod.addTopPanelItem(Cor.display);

        CorruptionTypePerFloorRunHistoryPatch.buildCorruptionTypeMapping();
    }

    private static void addMonsters() {
        BaseMod.addMonster(Encounters.TIME_AND_SPACE, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new MasterOfTimeAndSpace(-300.0F, -25.0F),
                        new SpatialPhantasm(250.0F, 325.0F),
                        new TemporalPhantasm(250.0F, 0.0F)
                }));
        BaseMod.addMonster(Encounters.DEVOURER, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new LesserDevourerGreen(-500.0F, 0.0F),
                        new LesserDevourerBrown(-300.0F, 0.0F),
                        new LesserDevourerBlue(-100.0F, 0.0F),
                        new TranscendentDevourer(160.0F, 0.0F)
                }));
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
        BaseMod.addMonster(CorruptionManifest.ID, (BaseMod.GetMonster) CorruptionManifest::new);
        BaseMod.addMonster(FlameManifest.ID, (BaseMod.GetMonster) FlameManifest::new);
        BaseMod.addMonster(StrifeManifest.ID, (BaseMod.GetMonster) StrifeManifest::new);
        BaseMod.addMonster(DeliriumManifest.ID, (BaseMod.GetMonster) DeliriumManifest::new);
        BaseMod.addMonster(Harbinger.ID, (BaseMod.GetMonster) Harbinger::new);
        BaseMod.addMonster(Doom.ID, () -> new Doom(0.0F, 150.0F));
        BaseMod.addMonster(RottingShambler.ID, (BaseMod.GetMonster) RottingShambler::new);
        BaseMod.addMonster(Encounters.WISPS, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new OtherworldlyWisp(-550.0F, 100.0F),
                        new GreaterWisp(-350.0F, 150.0F),
                        new OtherworldlyWisp(-150.0F, 100.0F),
                        new GreaterWisp(50.0F, 150.0F),
                        new OtherworldlyWisp(250.0F, 100.0F)
                }));
        BaseMod.addMonster(Encounters.WAR_AND_FEAR, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new RelentlessWar(-1000.0F, 0.0F),
                        new EternalFear(70.0F, 0.0F),
                }));

        BaseMod.addBoss("", Encounters.WAR_AND_FEAR, "corruptthespire/images/map/bosses/WarAndFear.png", "corruptthespire/images/map/bosses/WarAndFearOutline.png");
    }

    private static void addEvents() {
        // These events are only encountered through our own special logic, but we register them all here for ease of
        // debugging (thus the conditions that make them never show up) and to have the corrupted events show up for
        // Event Filter (using EventFilterSkipSpecialEventsPatch to prevent it from seeing Harbinger/Devourer/Doom)
        addEvent(HarbingerEvent.ID, HarbingerEvent.class);
        addEvent(DevourerEvent.ID, DevourerEvent.class);
        addEvent(DoomEvent.ID, DoomEvent.class);
        for (Map.Entry<String, CorruptedEventInfo> e : CorruptedEventUtil.getAllCorruptedEvents().entrySet()) {
            addEvent(e.getKey(), e.getValue().cls);
        }
        //We leave the following events unregistered:
        //* Fragment of Corruption event, which is a wrapper around other events rather than a real event itself
        //* SealedChestEvent, which is a substitute for a Treasure Room
        //* TreasureWardensEvent, which is a substitute for a Treasure Room
    }

    private static void addEvent(String eventId, Class<? extends AbstractEvent> eventClass) {
        Condition alwaysFalseCondition = () -> false;
        BaseMod.addEvent(new AddEventParams.Builder(eventId, eventClass).spawnCondition(alwaysFalseCondition).bonusCondition(alwaysFalseCondition).create());
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
        BaseMod.registerCustomReward(
            CustomRewardTypes.CORRUPTTHESPIRE_CORRUPTEDCARD,
            (rewardSave) -> new CorruptedCardReward(),
            (customReward) -> new RewardSave(customReward.type.toString(), null, 0, 0));
    }

    private static void addPotions() {
        addPotion(AbyssalBrew.class, Color.BLACK.cpy(), null, null, AbyssalBrew.POTION_ID);
        addPotion(CorruptedPotion.class, Color.PURPLE.cpy(), null, null, CorruptedPotion.POTION_ID);
        addPotion(VoidBomb.class, Color.PURPLE.cpy(), Color.BLACK.cpy(), null, VoidBomb.POTION_ID);
        addPotion(DarkElixir.class, Color.BLACK.cpy(), null, Color.PURPLE.cpy(), DarkElixir.POTION_ID);
        addPotion(ProfaneMixture.class, Color.BLACK.cpy(), Color.PURPLE.cpy(), null, ProfaneMixture.POTION_ID);
    }

    private static void addPotion(Class<? extends AbstractPotion> potionClass, Color liquidColor, Color hybridColor, Color spotsColor, String potionID) {
        if (!PotionUtil.corruptedPotionIds.contains(potionID)) {
            throw new RuntimeException("Must add new PotionID to list of corruptedPotionIds to filter out: " + potionID);
        }
        BaseMod.addPotion(potionClass, liquidColor, hybridColor, spotsColor, potionID);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new MagicNumber2Variable());
        BaseMod.addDynamicVariable(new MagicNumber3Variable());

        addCard(new CelestialAegis());
        addCard(new Boon());
        addCard(new Nudge());
        addCard(new ShimmeringShield());
        addCard(new TheFool());
        addCard(new WheelOfFortune());
        addCard(new BadBreath());
        addCard(new Bedeviled());
        addCard(new Contagion());
        addCard(new CorruptingRot());
        addCard(new Fated());
        ArrayList<AbstractCard> corruptedCards = CorruptedCardUtil.getAllCorruptedCardInfos(true)
                .values()
                .stream()
                .map(cci -> cci.card)
                .collect(Collectors.toCollection(ArrayList::new));
        for (AbstractCard c : corruptedCards) {
            addCard(c);
        }
    }

    private void addCard(AbstractCard c) {
        BaseMod.addCard(c);
        UnlockTracker.markCardAsSeen(c.cardID);
    }

    @Override
    public void receiveEditRelics() {
        addRelic(new BurningRing());
        addRelic(new ChthonicBlessing());
        addRelic(new DeckOfManyFates());
        addRelic(new DoomSight());
        addRelic(new DoomWing());
        addRelic(new GnosticBlessing());
        addRelic(new HandOfMidas());
        addRelic(new HarbingersClaw());
        addRelic(new HarbingersSkull());
        addRelic(new ShimmeringFan());
        addRelic(new TranscendentEye());
        addRelic(new TranscendentPet());
        addRelic(new FragmentOfCorruption());
        for (AbstractRelic r : Cor.getAllCorruptedRelics()) {
            addRelic(r);
        }
    }

    private void addRelic(AbstractRelic relic) {
        BaseMod.addRelic(relic, RelicType.SHARED);
        UnlockTracker.markRelicAsSeen(relic.relicId);
    }

    private static String makeLocPath(Settings.GameLanguage language, String filename)
    {
        String ret = "localization/";
        switch (language) {
            case ZHS:
                ret += "zhs";
                break;
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
        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(language, "CorruptTheSpire-Potion-Strings"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(language, "CorruptTheSpire-ui"));
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
    public static String campfireImage(String id) {
        return "corruptthespire/images/campfire/" + removeModId(id) + ".png";
    }
    public static String effectImage(String id) {
        return "corruptthespire/images/effects/" + removeModId(id) + ".png";
    }

    public static String removeModId(String id) {
        if (id.startsWith("CorruptTheSpire:")) {
            return id.substring(id.indexOf(':') + 1);
        } else {
            logger.warn("Missing mod id on: " + id);
            return id;
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