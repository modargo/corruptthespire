package corruptthespire.corruptions.shop;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import corruptthespire.cards.CardUtil;
import corruptthespire.patches.CorruptedField;
import corruptthespire.patches.shop.ShopCorruptionTypeField;

import java.util.ArrayList;
import java.util.Objects;

public class ShopCorruption {
    public static void handleCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType == null) {
            return;
        }

        if (corruptionType == ShopCorruptionType.Prismatic) {
            coloredCards.clear();

            AbstractCard skill1 = CardUtil.getOtherColorCard(AbstractDungeon.rollRarity(), new ArrayList<>(), AbstractCard.CardType.SKILL);
            AbstractCard skill2 = skill1;
            while (skill1.cardID.equals(skill2.cardID)) {
                skill2 = CardUtil.getOtherColorCard(AbstractDungeon.rollRarity(), new ArrayList<>(), AbstractCard.CardType.SKILL);
            }

            AbstractCard attack1 = CardUtil.getOtherColorCard(AbstractDungeon.rollRarity(), new ArrayList<>(), AbstractCard.CardType.ATTACK);
            AbstractCard attack2 = attack1;
            while (attack1.cardID.equals(attack2.cardID)) {
                attack2 = CardUtil.getOtherColorCard(AbstractDungeon.rollRarity(), new ArrayList<>(), AbstractCard.CardType.ATTACK);
            }

            AbstractCard power = CardUtil.getOtherColorCard(AbstractDungeon.rollRarity(), new ArrayList<>(), AbstractCard.CardType.POWER);

            coloredCards.add(skill1);
            coloredCards.add(skill2);
            coloredCards.add(attack1);
            coloredCards.add(attack2);
            coloredCards.add(power);

            colorlessCards.clear();
            AbstractCard uncommon = null;
            while (uncommon == null
                    || skill1.cardID.equals(uncommon.cardID)
                    || skill2.cardID.equals(uncommon.cardID)
                    || attack1.cardID.equals(uncommon.cardID)
                    || attack2.cardID.equals(uncommon.cardID)
                    || power.cardID.equals(uncommon.cardID)) {
                uncommon = CardUtil.getOtherColorCard(AbstractCard.CardRarity.UNCOMMON);
            }

            AbstractCard rare = null;
            while (rare == null
                    || skill1.cardID.equals(rare.cardID)
                    || skill2.cardID.equals(rare.cardID)
                    || attack1.cardID.equals(rare.cardID)
                    || attack2.cardID.equals(rare.cardID)
                    || power.cardID.equals(rare.cardID)) {
                rare = CardUtil.getOtherColorCard(AbstractCard.CardRarity.RARE);
            }
            colorlessCards.add(uncommon);
            colorlessCards.add(rare);
        }

        if (corruptionType == ShopCorruptionType.Rare) {
            coloredCards.clear();
            //Copied directly from Merchant.cs, just changing every AbstractDungeon.rollRarity() to AbstractCard.CardRarity.RARE
            AbstractCard c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.ATTACK, true).makeCopy();
            while (c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.ATTACK, true).makeCopy();
            coloredCards.add(c);
            c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.ATTACK, true).makeCopy();
            while (Objects.equals(c.cardID, ((AbstractCard)coloredCards.get(coloredCards.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.ATTACK, true).makeCopy();
            coloredCards.add(c);
            c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            while (c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            coloredCards.add(c);
            c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            while (Objects.equals(c.cardID, ((AbstractCard)coloredCards.get(coloredCards.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            coloredCards.add(c);
            c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.POWER, true).makeCopy();
            while (c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.POWER, true).makeCopy();
            coloredCards.add(c);

            colorlessCards.clear();
            colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());
            colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());
        }
    }

    public static boolean handleRelics(ShopScreen shopScreen) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        //TODO implement relic options
        if (corruptionType == null) {

            return true;
        }

        return false;
    }
}
