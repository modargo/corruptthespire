package corruptthespire.corruptions.shop;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import corruptthespire.Cor;
import corruptthespire.cards.AbstractCorruptedCard;
import corruptthespire.cards.CardUtil;
import corruptthespire.cards.CorruptedCardUtil;
import corruptthespire.patches.CorruptedField;
import corruptthespire.patches.shop.ShopCorruptionTypeField;
import corruptthespire.relics.FragmentOfCorruption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Objects;

public class ShopCorruption {
    public static final Logger logger = LogManager.getLogger(ShopCorruption.class.getName());

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
            //colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());
            //colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());
        }

        if (corruptionType == ShopCorruptionType.CorruptedCards) {
            coloredCards.clear();
            coloredCards.addAll(CorruptedCardUtil.getRandomCorruptedCards(2, AbstractCard.CardType.ATTACK));
            coloredCards.addAll(CorruptedCardUtil.getRandomCorruptedCards(2, AbstractCard.CardType.SKILL));
            coloredCards.addAll(CorruptedCardUtil.getRandomCorruptedCards(1, AbstractCard.CardType.POWER));
        }

        if (corruptionType == ShopCorruptionType.CorruptedCardAndFragment) {
            colorlessCards.clear();
            colorlessCards.add(CorruptedCardUtil.getRandomCorruptedCard());
        }
    }

    public static boolean handleRelics(ShopScreen shopScreen) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType != null) {
            ArrayList<StoreRelic> relics = ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "relics");
            relics.clear();

            int numRelics = corruptionType == ShopCorruptionType.CorruptedRelicsReplacePotions ? 6 : 3;
            for(int i = 0; i < numRelics; ++i) {
                AbstractRelic tempRelic;
                if ((i < 2 && corruptionType == ShopCorruptionType.Rare)
                    || (i < 1 && corruptionType == ShopCorruptionType.TransformReplacesRemove)
                    || (i < 1 && corruptionType == ShopCorruptionType.CorruptedCardAndFragment)
                ) {
                    tempRelic = new FragmentOfCorruption();
                }
                else if (i < 2) {
                    tempRelic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());
                } else if (i == 2) {
                    tempRelic = AbstractDungeon.returnRandomRelicEnd(AbstractRelic.RelicTier.SHOP);
                } else {
                    tempRelic = RelicLibrary.getRelic(Cor.returnRandomCorruptedRelicKey()).makeCopy();
                }

                StoreRelic relic = new StoreRelic(tempRelic, i, shopScreen);
                if (!Settings.isDailyRun) {
                    relic.price = MathUtils.round((float)relic.price * AbstractDungeon.merchantRng.random(0.95F, 1.05F));
                }

                relics.add(relic);
            }
            return true;
        }

        return false;
    }

    public static boolean handlePotions(ShopScreen shopScreen) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType == ShopCorruptionType.CorruptedRelicsReplacePotions || corruptionType == ShopCorruptionType.CorruptedCards) {
            return true;
        }

        return false;
    }

    public static int getCorruptedCardPrice(AbstractCorruptedCard card) {
        AbstractCard.CardRarity rarity = CorruptedCardUtil.getAllCorruptedCardInfos().get(card.cardID).rarity;
        return rarity == AbstractCard.CardRarity.COMMON ? CorruptedCardUtil.CORRUPTED_COMMON_PRICE : CorruptedCardUtil.CORRUPTED_RARE_PRICE;
    }
}
