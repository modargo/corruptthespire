package corruptthespire.cards.corrupted;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class CorruptedCardUtil {
    private static final float CORRUPTED_RARE_CHANCE = 0.2F;
    public static final int CORRUPTED_COMMON_PRICE = 50;
    public static final int CORRUPTED_RARE_PRICE = 125;

    public static ArrayList<AbstractCard> getAllCorruptedCards() {
        return getAllCorruptedCardInfos().values().stream().map(cci -> cci.card).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Map<String, CorruptedCardInfo> getAllCorruptedCardInfos() {
        ArrayList<CorruptedCardInfo> corruptedCardInfos = new ArrayList<>();

        //Attacks
        corruptedCardInfos.add(new CorruptedCardInfo(new Shadowblast(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new EssenceRip(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new Condemn(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new Recoil(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new DrainLife(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new Fragmentize(), AbstractCard.CardRarity.RARE));

        //Skills
        corruptedCardInfos.add(new CorruptedCardInfo(new WickedWard(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new ProfaneShield(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new VoidArmor(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new DevilsBargain(), AbstractCard.CardRarity.RARE));

        //Powers
        corruptedCardInfos.add(new CorruptedCardInfo(new ForbiddenRitual(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new CorruptedForm(), AbstractCard.CardRarity.RARE));

        return corruptedCardInfos.stream()
            .collect(Collectors.toMap(cci -> cci.card.cardID, cci -> cci));
    }

    public static AbstractCard getRandomCorruptedCard() {
        return getRandomCorruptedCards(1, null).get(0);
    }

    public static ArrayList<AbstractCard> getRandomCorruptedCards(int n, AbstractCard.CardType type) {
        ArrayList<AbstractCard.CardRarity> rarities = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rarities.add(rollCorruptedRarity());
        }

        ArrayList<AbstractCard> cards = new ArrayList<>();
        ArrayList<AbstractCard> corruptedCommons = null;
        ArrayList<AbstractCard> corruptedRares = null;
        for (AbstractCard.CardRarity rarity : rarities) {
            AbstractCard card;
            if (rarity == AbstractCard.CardRarity.COMMON) {
                if (corruptedCommons == null) {
                    corruptedCommons = getAllCorruptedCardInfos().values().stream()
                            .filter(cci -> cci.rarity == AbstractCard.CardRarity.COMMON)
                            .filter(cci -> type == null || cci.card.type == type)
                            .map(cci -> cci.card)
                            .collect(Collectors.toCollection(ArrayList::new));
                    Collections.shuffle(corruptedCommons, AbstractDungeon.cardRng.random);
                }
                card = corruptedCommons.remove(0);
            }
            else {
                if (corruptedRares == null) {
                    corruptedRares = getAllCorruptedCardInfos().values().stream()
                            .filter(cci -> cci.rarity == AbstractCard.CardRarity.RARE)
                            .filter(cci -> type == null || cci.card.type == type)
                            .map(cci -> cci.card)
                            .collect(Collectors.toCollection(ArrayList::new));
                    Collections.shuffle(corruptedRares, AbstractDungeon.cardRng.random);
                }
                card = corruptedRares.remove(0);
            }
            cards.add(card.makeCopy());
        }

        return cards;
    }

    private static AbstractCard.CardRarity rollCorruptedRarity() {
        return AbstractDungeon.cardRng.randomBoolean(CORRUPTED_RARE_CHANCE) ? AbstractCard.CardRarity.RARE : AbstractCard.CardRarity.COMMON;
    }
}
