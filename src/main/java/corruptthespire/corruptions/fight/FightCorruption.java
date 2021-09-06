package corruptthespire.corruptions.fight;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Repulsor;
import com.megacrit.cardcrawl.monsters.beyond.SnakeDagger;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.monsters.exordium.GremlinThief;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.Cor;
import corruptthespire.corruptions.fight.rewards.FightCorruptionReward;
import corruptthespire.monsters.CorruptionManifest;
import corruptthespire.patches.CorruptedField;
import corruptthespire.patches.fight.FightCorruptionInfosField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FightCorruption {
    private static final Logger logger = LogManager.getLogger(FightCorruption.class.getName());

    public static boolean shouldApplyCorruptions() {
        return AbstractDungeon.getCurrRoom() instanceof MonsterRoom && CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode());
    }

    public static void determineCorruptions(AbstractRoom room) {
        FightType fightType = getFightType(room);
        FightCorruptionInfo corruptionInfo = new FightCorruptionDistribution().roll(AbstractDungeon.actNum, fightType);
        FightCorruptionInfosField.corruptionInfos.set(room, Collections.singletonList(corruptionInfo));
    }

    public static void addRewards() {
        if (AbstractDungeon.actNum >= 3 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
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
            int numEasyFights = AbstractDungeon.actNum == 1 ? 3 : 2;
            fightType = Cor.flags.normalMonsterCount > numEasyFights ? FightType.Hard : FightType.Easy;
            logger.info("Normal monster count: " + Cor.flags.normalMonsterCount + ", fight type: " + fightType);
        }

        return fightType;
    }

    public static void applyStartOfBattleCorruptions() {
        //None of these exist yet, and unsure if they will exist, but this is here as scaffolding
    }

    public static void applyOnSpawnMonsterCorruptions(AbstractMonster m) {
        List<FightCorruptionInfo> corruptionInfos = FightCorruptionInfosField.corruptionInfos.get(AbstractDungeon.getCurrRoom());
        for (FightCorruptionInfo corruptionInfo : corruptionInfos) {
            applyOnSpawnMonsterCorruption(corruptionInfo, m);
        }
    }

    private static void applyOnSpawnMonsterCorruption(FightCorruptionInfo corruptionInfo, AbstractMonster m) {
        switch (corruptionInfo.corruptionType) {
            case Metallicize:
                apa(m, new MetallicizePower(m, corruptionInfo.amount));
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
            case PainfulStabs:
                apa(m, new PainfulStabsPower(m));
                break;
            case Buffer:
                apa(m, new BufferPower(m, corruptionInfo.amount));
                break;
            case Ritual:
                RitualPower ritualPower = new RitualPower(m, corruptionInfo.amount, false);
                ritualPower.atEndOfRound(); //To bypass the skip first turn logic
                apa(m, ritualPower);
                break;
            case BeatOfDeath:
                apa(m, new BeatOfDeathPower(m, corruptionInfo.amount));
                break;
            case ThoughtStealer:
                //TODO: Implement Thought Stealer
                break;
            case Cleanse:
                //TODO: Implement Cleanse
                break;
            case Anger:
                //TODO: Implement Anger
                break;
            case Wary:
                //TODO: Implement Wary
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
            AbstractMonster m = getExtraMonsterCorruption(corruptionInfo);
            if (m != null) {
                monsters.add(m);
            }
        }
        return monsters;
    }

    private static AbstractMonster getExtraMonsterCorruption(FightCorruptionInfo corruptionInfo) {
        Coordinate c = getSpawnXY();
        float x = c.x;
        float y = c.y;
        switch (corruptionInfo.corruptionType) {
            case CorruptionManifestMinion:
                CorruptionManifest.Version version = AbstractDungeon.actNum <= 1 ? CorruptionManifest.Version.Act1
                        : AbstractDungeon.actNum == 2 ? CorruptionManifest.Version.Act2
                        : AbstractDungeon.actNum == 3 ? CorruptionManifest.Version.Act3
                        : CorruptionManifest.Version.Act4;
                return new CorruptionManifest(x, y, version);
            case LouseMinion:
                return new LouseDefensive(x, y);
            case SlimeMinion:
                return new SpikeSlime_S(x, y, 0);
            case GremlinMinion:
                return new GremlinThief(x, y);
            case RepulsorMinion:
                return new Repulsor(x, y);
            case ByrdMinion:
                return new Byrd(x, y);
            case SnakeDaggerMinion:
                return new SnakeDagger(x, y);
            case CultistMinion:
                return new Cultist(x, y);
            default:
                return null;
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
