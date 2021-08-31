package corruptthespire.corruptions.fight;

import corruptthespire.patches.BestiaryIntegrationPatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FightCorruptionDistributionReader {
    private static final Logger logger = LogManager.getLogger(BestiaryIntegrationPatch.class.getName());
    private static final String DISTRIBUTION_FILE = "corruptthespire/corruptions/fight.txt";

    private static ArrayList<FightCorruptionDistributionEntry> fightCorruptionDistributionEntries = null;

    public static ArrayList<FightCorruptionDistributionInfo> getFightCorruptionDistribution(int actNum, FightType fightType) {
        if (fightCorruptionDistributionEntries == null) {
            fightCorruptionDistributionEntries = readFightCorruptionDistributionFile();
            if (fightCorruptionDistributionEntries == null) {
                throw new RuntimeException("Could not read fightCorruptionDistributionEntries");
            }
        }

        return fightCorruptionDistributionEntries.stream()
            .filter(e -> e.actNum == actNum && e.fightType == fightType)
            .map(e -> new FightCorruptionDistributionInfo(e.corruptionType, e.size, e.weight, e.amount))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<FightCorruptionDistributionEntry> readFightCorruptionDistributionFile() {
        InputStream in = FightCorruptionDistributionReader.class.getClassLoader().getResourceAsStream(DISTRIBUTION_FILE);
        if (in == null) {
            logger.error("Failed to load fight.txt (not found?)");
            return null;
        }

        ArrayList<FightCorruptionDistributionEntry> fightCorruptionDistributionEntries = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String firstLine = br.readLine();
            String[] headers = firstLine.split("\t");
            int[] actNums = new int[16];
            FightType[] fightTypes = new FightType[16];
            for (int i = 1; i <= 16; i++) {
                String[] tuple = headers[i].split(",");
                actNums[i - 1] = Integer.parseInt(tuple[0], 10);
                fightTypes[i - 1] = FightType.valueOf(tuple[1]);
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] entries = line.split("\t");
                FightCorruptionType corruptionType = FightCorruptionType.valueOf(entries[0]);
                for (int i = 1; i <= 16; i++) {
                    if (entries[i].equals("") || entries[i].equals("N")) {
                        continue;
                    }
                    String[] entry = entries[i].split(",");
                    FightCorruptionSize size = FightCorruptionSize.valueOf(entry[0]);
                    int weight = Integer.parseInt(entry[1]);
                    int amount = Integer.parseInt(entry[2]);
                    FightCorruptionDistributionEntry e = new FightCorruptionDistributionEntry();
                    e.actNum = actNums[i - 1];
                    e.fightType = fightTypes[i - 1];
                    e.corruptionType = corruptionType;
                    e.size = size;
                    e.weight = weight;
                    e.amount = amount;
                    fightCorruptionDistributionEntries.add(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fightCorruptionDistributionEntries;
    }

    private static class FightCorruptionDistributionEntry {
        public int actNum;
        public FightType fightType;
        public FightCorruptionType corruptionType;
        public FightCorruptionSize size;
        public int weight;
        public int amount;
    }
}
