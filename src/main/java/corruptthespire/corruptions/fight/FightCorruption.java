package corruptthespire.corruptions.fight;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.patches.CorruptedField;
import corruptthespire.patches.fight.FightCorruptionInfosField;

import java.util.Collections;
import java.util.List;

public class FightCorruption {
    public static boolean shouldApplyCorruptions() {
        return AbstractDungeon.getCurrRoom() instanceof MonsterRoom && CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode());
    }

    public static void determineCorruptions(AbstractRoom room) {
        FightType fightType;
        if (room instanceof MonsterRoomBoss) {
            fightType = FightType.Boss;
        }
        else if (room instanceof MonsterRoomElite) {
            fightType = FightType.Elite;
        }
        else {
            //TODO: Need to separate the starting weak encounters from the normal encounters after them
            //This is the first 3 encounters in act 1 and the first 2 in acts 2 and 3
            //If there's no better way, just keep my own count (make another savable)
            //Can attach a postfix to getMonsterForRoomCreation to know, or in MonsterRoom.onPlayerEntry
            //(would just have to make sure it doesn't carry over to MonsterRoomElite and MonsterRoomBoss)
            //I think I can do that easily -- might happen naturally for a postfix patch, and if not I can look for
            //the specific call.
            fightType = FightType.Normal;
        }

        FightCorruptionInfo corruptionInfo = new FightCorruptionDistribution().roll(AbstractDungeon.actNum, fightType);
        FightCorruptionInfosField.corruptionInfos.set(room, Collections.singletonList(corruptionInfo));
    }

    public static void applyStartOfBattleCorruptions() {
        List<FightCorruptionInfo> corruptionInfos = FightCorruptionInfosField.corruptionInfos.get(AbstractDungeon.getCurrRoom());
        for (FightCorruptionInfo corruptionInfo : corruptionInfos) {
            applyStartOfBattleCorruption(corruptionInfo);
        }
    }

    public static void applyOnSpawnMonsterCorruptions(AbstractMonster m) {
        List<FightCorruptionInfo> corruptionInfos = FightCorruptionInfosField.corruptionInfos.get(AbstractDungeon.getCurrRoom());
        for (FightCorruptionInfo corruptionInfo : corruptionInfos) {
            applyOnSpawnMonsterCorruption(corruptionInfo, m);
        }
    }

    private static void applyStartOfBattleCorruption(FightCorruptionInfo corruptionInfo) {
        switch (corruptionInfo.corruptionType) {
            case CorruptionManifestMinion:
                break;
            case LouseMinion:
                break;
            case SlimeMinion:
                break;
            case GremlinMinion:
                break;
            case RepulsorMinion:
                break;
            case ByrdMinion:
                break;
            case SnakeDaggerMinion:
                break;
            case CultistMinion:
                break;
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
            case Intangible:
                //TODO: Implement Intangible
                break;
            case Buffer:
                apa(m, new BufferPower(m, corruptionInfo.amount));
                break;
            case Ritual:
                apa(m, new RitualPower(m, corruptionInfo.amount, false));
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
                //TODO: Implement Waryu
                break;
            case Curiosity:
                //TODO: Implement Curiosity
                break;
        }
    }

    private static void apa(AbstractMonster m, AbstractPower power) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, power));
    }
}
