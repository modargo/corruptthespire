package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import corruptthespire.cards.corrupted.CorruptedCardColor;
import corruptthespire.cards.corrupted.CorruptedCardUtil;

@SpirePatch(clz = AbstractDungeon.class, method = "returnTrulyRandomCardFromAvailable", paramtypez = {AbstractCard.class, Random.class})
public class TransformCorruptedCardPatch {
    @SpirePrefixPatch
    public static SpireReturn<AbstractCard> transformCorruptedCard(AbstractCard c, Random rng) {
        if (c.color != CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED) {
            return SpireReturn.Continue();
        }

        AbstractCard transformedCard = null;

        while (transformedCard == null || transformedCard.cardID.equals(c.cardID)) {
            transformedCard = CorruptedCardUtil.getRandomCorruptedCardForTransform(rng);
        }

        return SpireReturn.Return(transformedCard);
    }
}
