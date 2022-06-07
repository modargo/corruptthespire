package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.WheelOfFortune;
import corruptthespire.relics.chaotic.HandOfMidas;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GoldenVision extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:GoldenVision";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int MAX_HEALTH = 2;
    private static final int A15_MAX_HEALTH = 1;

    private final AbstractRelic relic;
    private final AbstractRelic bossRelic;

    // The intent of this event is to exchange a boss relic that still has active value
    // To ensure this, we keep a whitelist of boss relics from the base game and a selection of mods
    // Relics are excluded from this list for three reasons:
    // (1) They have an immediate benefit or cost (e.g. Pandora's Box, or Leader Voucher from Downfall which gives you an energy for the rest of the game but causes you to immediately lose one of your gremlins)
    // (2) They are good enough that wanting to trade would be very rare (e.g. Runic Pyramid)
    // (3) They are somewhere in between active and used up (e.g. Black Star)
    // Exclusions for reason 1 are judged to be clear enough to not bother noting, but exclusions for reasons 2 and 3 are noted
    private static final String[] IDs = {
            // Base game
            // Too good: Runic Pyramid, Snecko Eye
            // In between: Black Star
            BustedCrown.ID, CoffeeDripper.ID, CursedKey.ID, Ectoplasm.ID, FusionHammer.ID, PhilosopherStone.ID, RunicDome.ID, SlaversCollar.ID, Sozu.ID, VelvetChoker.ID, MarkOfPain.ID, RunicCube.ID, RingOfTheSerpent.ID, WristBlade.ID, HoveringKite.ID, FrozenCore.ID, Inserter.ID, NuclearBattery.ID, HolyWater.ID, VioletLotus.ID,
            // Shaman
            // Too good: Twig of the World Tree
            "Shaman:EternalTotem", "Shaman:ShiningOrb",
            // Animator
            // In between: Ancient Chalice
            "animator:VividPicture", "animator:CrumblingOrb", "animator:CursedBlade",
            // Replay
            // In between: Honey Jar, Ring of Chaos
            //  Hamster Wheel, Ooze Armor
            "Abe's Treasure", "Replay:Bronze Core", "ChewingGum", "ReplayTheSpireMod:DimensionalGlitch", "Electric Blood", "ReplayTheSpireMod:Onyx Gauntlets", "Ooze Armor", "Replay:Drink Me", "Snecko Heart", "Snecko Scales", "Painkiller Herb", "Replay:Wanted Poster",
            // Conspire
            // Royal Goblet, Runic Octahedron, Severed Torchhead, Slow Cooker, Special Sausage
            "conspire:RoyalGoblet", "conspire:RunicOctahedron", "conspire:SeveredTorchhead", "conspire:SlowCooker", "conspire:SpecialSausage",
            // Hubris
            // In between: Scarier Mask, Stopwatch
            "hubris:BlackHole", "hubris:CrackedHourglass", "hubris:GrandSneckoEye", "hubris:HollowSoul", "hubris:RunicObelisk", "hubris:VirtuousBlindfold",
            // Downfall
            // In between: Clasped Locket, Lucky Horseshoe
            "hermit:BartenderGlass", "hermit:DentedPlate", "bronze:ElectromagneticCoil", "Guardian:ModeShifterPlus", "downfall:Hecktoplasm", "sneckomod:SneckoTalon", "bronze:MakeshiftBattery", "sneckomod:CrystallizedMud", "Gremlin:GremlinKnobUpgrade", "bronze:PlatinumCore", "champ:PowerArmor", "Gremlin:ShortStature", "sneckomod:SuperSneckoSoul", "Slimebound:TarBlob", "expansioncontent:StudyCardRelic", "hexamod:UnbrokenSoul", "champ:ChampionCrownUpgraded", "Guardian:StasisSlotReductionRelic", "hexamod:IceCube",
            // Aspiration
            // In between: all skillbooks
            "aspiration:BursterCore", "aspiration:FutureDiary", "aspiration:Mageblood", "aspiration:MechanicalEye", "aspiration:PoetsPen", "aspiration:PoetsPen_weak", "aspiration:RandomNobGenerator", "aspiration:RunicSpoon", "aspiration:SecretTechniqueScroll", "aspiration:Stellarator"
    };
    private static final Set<String> knownActiveBossRelicIDs = new HashSet<>(Arrays.asList(IDs));

    private int screenNum = 0;

    public GoldenVision() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relic = new HandOfMidas();
        this.bossRelic = getBossRelicToLose();
        if (this.bossRelic == null) {
            throw new RuntimeException("Golden Vision event requires a boss relic that is valid to exchange");
        }

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic.name, this.bossRelic.name), this.relic);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Grasp
                        AbstractDungeon.player.loseRelic(this.bossRelic.relicId);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.relic);
                        logMetricRelicSwap(ID, "Grasp", this.relic, this.bossRelic);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Ignore
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    public static AbstractRelic getBossRelicToLose() {
        if (AbstractDungeon.player == null) {
            return null;
        }
        AbstractRelic ectoplasm = AbstractDungeon.player.getRelic(Ectoplasm.ID);
        ectoplasm = ectoplasm == null ? AbstractDungeon.player.getRelic("downfall:Hecktoplasm") : ectoplasm;
        if (ectoplasm != null) {
            return ectoplasm;
        }
        List<AbstractRelic> validRelics = AbstractDungeon.player.relics.stream().filter(r -> knownActiveBossRelicIDs.contains(r.relicId)).collect(Collectors.toList());
        if (validRelics.size() > 0) {
            return validRelics.get(0);
        }
        return null;
    }


}