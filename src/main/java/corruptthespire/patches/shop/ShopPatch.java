package corruptthespire.patches.shop;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.corruptions.shop.ShopCorruption;
import corruptthespire.corruptions.shop.ShopCorruptionType;
import corruptthespire.patches.CorruptedField;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ShopPatch {
    public static String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:ShopCorruption").TEXT;

    @SpirePatch(clz = Merchant.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {float.class, float.class, int.class})
    public static class ChangeCardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void changeCards(Merchant __instance, float x, float y, int newShopScreen) {
            ArrayList<AbstractCard> coloredCards = ReflectionHacks.getPrivate(__instance, Merchant.class, "cards1");
            ArrayList<AbstractCard> colorlessCards = ReflectionHacks.getPrivate(__instance, Merchant.class, "cards2");
            ShopCorruption.handleCards(coloredCards, colorlessCards);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ShopScreen.class, "init");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "initRelics")
    public static class ChangeRelicsPatch {
        @SpirePrefixPatch
        public static SpireReturn changeRelics(ShopScreen __instance) {
            if (ShopCorruption.handleRelics(__instance)) {
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "initPotions")
    public static class ChangePotionsPatch {
        @SpirePrefixPatch
        public static SpireReturn changePotions(ShopScreen __instance) {
            if (ShopCorruption.handlePotions(__instance)) {
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "purchasePurge")
    public static class PurchaseTransformPatch {
        @SpirePrefixPatch
        public static SpireReturn purchaseTransform(ShopScreen __instance) {
            ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
                    ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                    : null;
            if (corruptionType == ShopCorruptionType.TransformReplacesRemove) {
                ReflectionHacks.setPrivate(__instance, ShopScreen.class, "purgeHovered", false);
                if (AbstractDungeon.player.gold >= ShopScreen.actualPurgeCost) {
                    AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
                    AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, TEXT[0], false, true, true, false);
                } else {
                    __instance.playCantBuySfx();
                    __instance.createSpeech(ShopScreen.getCantBuyMsg());
                }
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

    //TODO also patch the remove card image
    @SpirePatch(clz = ShopScreen.class, method = "updatePurge")
    public static class UpdateTransformPatch {
        @SpirePrefixPatch
        public static SpireReturn updateTransform(ShopScreen __instance) {
            ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())
                    ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                    : null;
            if (corruptionType == ShopCorruptionType.TransformReplacesRemove) {
                if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                    ShopScreen.purgeCard();
                    for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                        CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
                        AbstractDungeon.player.masterDeck.removeCard(card);
                        AbstractDungeon.transformCard(card, false, AbstractDungeon.miscRng);
                        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }

                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    AbstractDungeon.shopScreen.purgeAvailable = false;
                }
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}
