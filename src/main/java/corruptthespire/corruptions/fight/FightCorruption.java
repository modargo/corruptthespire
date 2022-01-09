package corruptthespire.corruptions.fight;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Repulsor;
import com.megacrit.cardcrawl.monsters.beyond.SnakeDagger;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.monsters.exordium.GremlinThief;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.Cor;
import corruptthespire.corruptions.fight.rewards.FightCorruptionReward;
import corruptthespire.monsters.*;
import corruptthespire.patches.CorruptedField;
import corruptthespire.patches.fight.FightCorruptionInfosField;
import corruptthespire.powers.ThoughtStealerPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class FightCorruption {
    private static final Logger logger = LogManager.getLogger(FightCorruption.class.getName());
    private static final HashSet<AbstractMonster> handledMonsters = new HashSet<>();

    public static boolean shouldApplyCorruptions() {
        return AbstractDungeon.getCurrRoom() instanceof MonsterRoom && CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode());
    }

    public static void determineCorruptions(AbstractRoom room) {
        FightType fightType = getFightType(room);
        FightCorruptionInfo corruptionInfo = new FightCorruptionDistribution().roll(Cor.getActNum(), fightType);
        FightCorruptionInfosField.corruptionInfos.set(room, Collections.singletonList(corruptionInfo));
    }

    public static void addRewards() {
        if (Cor.getActNum() >= 3 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            return;
        }
        List<FightCorruptionInfo> corruptionInfos = FightCorruptionInfosField.corruptionInfos.get(AbstractDungeon.getCurrRoom());
        for (FightCorruptionInfo corruptionInfo : corruptionInfos) {
            FightCorruptionReward.addReward(corruptionInfo.size);
        }

        FightType fightType = getFightType(AbstractDungeon.getCurrRoom());
        if (fightType == FightType.Easy || fightType == FightType.Hard) {
            Cor.flags.hadFirstCorruptedNormalMonsterFight = true;
        }
    }

    private static FightType getFightType(AbstractRoom room) {
        FightType fightType;
        if (room instanceof MonsterRoomBoss) {
            fightType = FightType.Boss;
        }
        else if (room instanceof MonsterRoomElite) {
            fightType = FightType.Elite;
        }
        else {
            int numEasyFights = Cor.getRealActNum() == 1 ? 3 : 2;
            fightType = Cor.getActNum() == 4 || Cor.flags.normalMonsterCount > numEasyFights ? FightType.Hard : FightType.Easy;
            logger.info("Normal monster count: " + Cor.flags.normalMonsterCount + ", fight type: " + fightType);
        }

        return fightType;
    }

    public static void applyStartOfBattleCorruptions() {
        List<FightCorruptionInfo> corruptionInfos = FightCorruptionInfosField.corruptionInfos.get(AbstractDungeon.getCurrRoom());
        for (FightCorruptionInfo corruptionInfo : corruptionInfos) {
            applyStartOfBattleCorruption(corruptionInfo);
        }
    }

    private static void applyStartOfBattleCorruption(FightCorruptionInfo corruptionInfo) {
        AbstractMonster primaryMonster = getPrimaryMonster();
        switch (corruptionInfo.corruptionType) {
            case BeatOfDeath:
                apa(primaryMonster, new BeatOfDeathPower(primaryMonster, corruptionInfo.amount));
                break;
            case ThoughtStealer:
                apa(primaryMonster, new ThoughtStealerPower(primaryMonster));
                break;
        }
    }

    private static AbstractMonster getPrimaryMonster() {
        AbstractMonster primaryMonster = null;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (primaryMonster == null || m.currentHealth >= primaryMonster.currentHealth) {
                primaryMonster = m;
            }
        }
        return primaryMonster;
    }

    public static void applyOnSpawnMonsterCorruptions(AbstractMonster m) {
        if (!handledMonsters.contains(m)) {
            List<FightCorruptionInfo> corruptionInfos = FightCorruptionInfosField.corruptionInfos.get(AbstractDungeon.getCurrRoom());
            for (FightCorruptionInfo corruptionInfo : corruptionInfos) {
                applyOnSpawnMonsterCorruption(corruptionInfo, m);
            }
            handledMonsters.add(m);
        }
    }

    private static void applyOnSpawnMonsterCorruption(FightCorruptionInfo corruptionInfo, AbstractMonster m) {
        switch (corruptionInfo.corruptionType) {
            case Metallicize:
                apa(m, new MetallicizePower(m, corruptionInfo.amount));
                break;
            case Malleable:
                apa(m, new MalleablePower(m, corruptionInfo.amount));
                break;
            case Artifact:
                apa(m, new ArtifactPower(m, corruptionInfo.amount));
                break;
            case Thorns:
                apa(m, new ThornsPower(m, corruptionInfo.amount));
                break;
            case Strength:
                apa(m, new StrengthPower(m, corruptionInfo.amount));
                break;
            case Buffer:
                apa(m, new BufferPower(m, corruptionInfo.amount));
                break;
            case Ritual:
                RitualPower ritualPower = new RitualPower(m, corruptionInfo.amount, false);
                ritualPower.atEndOfRound(); //To bypass the skip first turn logic
                apa(m, ritualPower);
                break;
            case Curiosity:
                apa(m, new CuriosityPower(m, corruptionInfo.amount));
                break;
        }
    }

    private static void apa(AbstractMonster m, AbstractPower power) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, power));
    }

    public static ArrayList<AbstractMonster> getExtraMonsterCorruptions() {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        List<FightCorruptionInfo> corruptionInfos = FightCorruptionInfosField.corruptionInfos.get(AbstractDungeon.getCurrRoom());
        for (FightCorruptionInfo corruptionInfo : corruptionInfos) {
            for (AbstractMonster m : getExtraMonsterCorruption(corruptionInfo)) {
                if (m != null) {
                    monsters.add(m);
                }
            }
        }
        return monsters;
    }

    private static List<AbstractMonster> getExtraMonsterCorruption(FightCorruptionInfo corruptionInfo) {
        Coordinate c = getSpawnXY();
        float x = c.x;
        float y = c.y;
        List<AbstractMonster> r = new ArrayList<>();
        switch (corruptionInfo.corruptionType) {
            case DevourerMinion:
                int actNum = Cor.getActNum();
                AbstractMonster devourer = getDevourer(x, y, null);
                r.add(devourer);
                if (actNum == 4) {
                    r.add(getDevourer(x + 200.0f, y, devourer.id));
                }
            default:
                r.add(getSingleMonster(corruptionInfo, x, y));
                break;
        }

        return r;
    }

    private static AbstractMonster getSingleMonster(FightCorruptionInfo corruptionInfo, float x, float y) {
        switch (corruptionInfo.corruptionType) {
            case CorruptionManifestMinion:
                return new CorruptionManifest(x, y, getCorruptionManifestVersion());
            case BurningRevenantMinion:
                return new BurningRevenant(x, y, getBurningRevenantVersion());
            case SlimeMinion:
                return new SpikeSlime_S(x, y, 0);
            case GremlinMinion:
                return new GremlinThief(x, y);
            case RepulsorMinion:
                return new Repulsor(x, y);
            case SnakeDaggerMinion:
                return new SnakeDagger(x, y);
            case CultistMinion:
                return new Cultist(x, y);
            default:
                return null;
        }
    }

    private static CorruptionManifest.Version getCorruptionManifestVersion() {
        int actNum = Cor.getActNum();
        return actNum <= 1 ? CorruptionManifest.Version.Act1
                : actNum == 2 ? CorruptionManifest.Version.Act2
                : actNum == 3 ? CorruptionManifest.Version.Act3
                : CorruptionManifest.Version.Act4;
    }

    private static BurningRevenant.Version getBurningRevenantVersion() {
        int actNum = Cor.getActNum();
        return actNum <= 1 ? BurningRevenant.Version.Act1
                : actNum == 2 ? BurningRevenant.Version.Act2
                : BurningRevenant.Version.Act3;
    }

    private static AbstractMonster getDevourer(float x, float y, String excludedId) {
        Random rng = new Random(Settings.seed + AbstractDungeon.floorNum);
        List<String> options = new ArrayList<>();
        options.add(LesserDevourerGreen.ID);
        options.add(LesserDevourerBrown.ID);
        options.add(LesserDevourerBlue.ID);
        if (excludedId != null) {
            options.remove(excludedId);
        }

        Collections.shuffle(options, rng.random);
        String id = options.get(0);
        switch (id) {
            case LesserDevourerGreen.ID: return new LesserDevourerGreen(x, y);
            case LesserDevourerBrown.ID: return new LesserDevourerBrown(x, y);
            case LesserDevourerBlue.ID: return new LesserDevourerBlue(x, y);
            default: throw new RuntimeException("Unrecognized ID for a Devourer: " + id);
        }
    }

    private static Coordinate getSpawnXY() {
        //TODO: Consider adding logic here to look at the existing monsters and figure out where is safe to spawn
        //Nothing will be perfect, but we can have some decent heuristics
        return new Coordinate(-600.0F, 0.0F);
    }

    private static class Coordinate {
        public final float x;
        public final float y;
        public Coordinate(float x, float y) { this.x = x; this.y = y; }
    }
}
