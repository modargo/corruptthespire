package corruptthespire.corruptions.shop;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import corruptthespire.Cor;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.cards.CardUtil;
import corruptthespire.cards.corrupted.CorruptedCardColor;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.patches.core.CorruptedField;
import corruptthespire.patches.shop.ShopCorruptionTypeField;
import corruptthespire.relics.FragmentOfCorruption;

import java.util.ArrayList;
import java.util.Objects;

public class ShopCorruption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:ShopCorruption");
    private static final String[] TEXT = uiStrings.TEXT;

    public static void handleCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
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
            while (Objects.equals(c.cardID, (coloredCards.get(coloredCards.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.ATTACK, true).makeCopy();
            coloredCards.add(c);
            c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            while (c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            coloredCards.add(c);
            c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            while (Objects.equals(c.cardID, (coloredCards.get(coloredCards.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.SKILL, true).makeCopy();
            coloredCards.add(c);
            c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.POWER, true).makeCopy();
            while (c.color == AbstractCard.CardColor.COLORLESS)
                c = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.RARE, AbstractCard.CardType.POWER, true).makeCopy();
            coloredCards.add(c);

            colorlessCards.clear();
        }

        if (corruptionType == ShopCorruptionType.CorruptedCards) {
            coloredCards.clear();
            coloredCards.addAll(CorruptedCardUtil.getRandomCorruptedCards(2, AbstractCard.CardType.ATTACK));
            coloredCards.addAll(CorruptedCardUtil.getRandomCorruptedCards(2, AbstractCard.CardType.SKILL));
            coloredCards.addAll(CorruptedCardUtil.getRandomCorruptedCards(1, AbstractCard.CardType.POWER));
        }

        if (corruptionType == ShopCorruptionType.CorruptedCardAndFragment) {
            colorlessCards.clear();
            if (AbstractDungeon.cardRng.randomBoolean()) {
                colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.UNCOMMON).makeCopy());
                colorlessCards.add(CorruptedCardUtil.getRandomCorruptedCard());
            }
            else {
                colorlessCards.add(CorruptedCardUtil.getRandomCorruptedCard());
                colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());
            }
        }
    }

    public static boolean handleRelics(ShopScreen shopScreen) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType != null) {
            ArrayList<StoreRelic> relics = ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "relics");
            relics.clear();

            int numRelics = corruptionType == ShopCorruptionType.CorruptedRelicsReplacePotions || Loader.isModLoaded("spicyShops") ? 6 : 3;
            for(int i = 0; i < numRelics; ++i) {
                AbstractRelic tempRelic;
                if (i < 3 && corruptionType == ShopCorruptionType.CorruptedRelics) {
                    tempRelic = Cor.returnEndRandomCorruptedRelic();
                }
                else if ((i < 2 && corruptionType == ShopCorruptionType.Rare)
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
                    if (corruptionType == ShopCorruptionType.CorruptedRelicsReplacePotions) {
                        tempRelic = Cor.returnEndRandomCorruptedRelic();
                    }
                    else {
                        tempRelic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());
                    }
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
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType == ShopCorruptionType.CorruptedRelicsReplacePotions || corruptionType == ShopCorruptionType.CorruptedCards) {
            ReflectionHacks.setPrivate(shopScreen, ShopScreen.class, "potions", new ArrayList<StorePotion>());
            return true;
        }

        return false;
    }

    public static boolean handlePurgeCardTooltip(float x, float y) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;
        if (corruptionType == ShopCorruptionType.TransformReplacesRemove) {
            TipHelper.renderGenericTip(x, y, TEXT[1], TEXT[2].replace("{0}", "25"));
            return true;
        }

        return false;
    }

    public static int getCorruptedCardPrice(AbstractCorruptedCard card) {
        AbstractCard.CardRarity rarity = CorruptedCardUtil.getAllCorruptedCardInfos().get(card.cardID).rarity;
        return rarity == AbstractCard.CardRarity.COMMON ? CorruptedCardUtil.CORRUPTED_COMMON_PRICE : CorruptedCardUtil.CORRUPTED_RARE_PRICE;
    }

    public static boolean handleCourier(ShopScreen shopScreen, AbstractCard purchasedCard) {
        boolean cardIsCorrupted = purchasedCard.color == CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED;
        boolean shopIsPrismatic = AbstractDungeon.getCurrRoom() instanceof ShopRoom && ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom()) == ShopCorruptionType.Prismatic;
        if (!cardIsCorrupted && !shopIsPrismatic) {
            return false;
        }

        int coloredIndex = shopScreen.coloredCards.indexOf(purchasedCard);
        if (coloredIndex != -1) {
            shopScreen.coloredCards.set(coloredIndex, getCardForShop(purchasedCard));
        }

        int colorlessIndex = shopScreen.colorlessCards.indexOf(purchasedCard);
        if (colorlessIndex != -1) {
            shopScreen.colorlessCards.set(colorlessIndex, getCardForShop(purchasedCard));
        }

        return true;
    }

    public static AbstractCard getCardForShop(AbstractCard purchasedCard) {
        boolean cardIsCorrupted = purchasedCard.color == CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED;
        AbstractCard c;
        if (cardIsCorrupted) {
            c = CorruptedCardUtil.getRandomCorruptedCard();
            c.price = getCorruptedCardPrice((AbstractCorruptedCard)c);
        }
        else {
            c = CardUtil.getOtherColorCard(AbstractDungeon.rollRarity(), new ArrayList<>(), purchasedCard.type);
            c.price = AbstractCard.getPrice(c.rarity);
        }
        c.current_x = purchasedCard.current_x;
        c.current_y = purchasedCard.current_y;
        c.target_x = c.current_x;
        c.target_y = c.current_y;
        return c;
    }
}
