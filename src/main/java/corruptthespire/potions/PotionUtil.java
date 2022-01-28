package corruptthespire.potions;

import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.random.Random;

import java.util.Arrays;
import java.util.List;

public class PotionUtil {
    public static final List<String> corruptedPotionIds = Arrays.asList(AbyssalBrew.POTION_ID, CorruptedPotion.POTION_ID, VoidBomb.POTION_ID, DarkElixir.POTION_ID, ProfaneMixture.POTION_ID);

    public static AbstractPotion getRandomCorruptedPotion(Random rng) {
        String potionId = corruptedPotionIds.get(rng.random(corruptedPotionIds.size() - 1));
        return PotionHelper.getPotion(potionId);
    }
}
