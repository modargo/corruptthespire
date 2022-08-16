package corruptthespire.patches.shop;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.corruptions.shop.ShopCorruption;
import corruptthespire.corruptions.shop.ShopCorruptionType;
import corruptthespire.corruptions.shop.ShopScreenServiceInfo;
import corruptthespire.corruptions.shop.ShopServiceType;
import corruptthespire.patches.core.CorruptedField;
import corruptthespire.savables.logs.ShopServiceLog;
import corruptthespire.util.TextureLoader;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShopPatch {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:ShopCorruption").TEXT;

    @SpirePatch(clz = ShopScreen.class, method = "init")
    public static class InitFieldsPatch {
        @SpirePrefixPatch
        public static void initFields(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                    ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                    : null;
            if (corruptionType == ShopCorruptionType.Service) {
                ShopScreenServiceInfoField.serviceInfo.set(__instance, new ShopScreenServiceInfo());
            }
        }
    }

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

    @SpirePatch(cls = "downfall.util.HeartMerchant", method = SpirePatch.CONSTRUCTOR, paramtypez = {float.class, float.class, int.class}, optional = true)
    public static class DownfallChangeCardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void changeCards(Object __instance, float x, float y, int newShopScreen) {
            ArrayList<AbstractCard> coloredCards = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "cards1");
            ArrayList<AbstractCard> colorlessCards = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "cards2");
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
        public static SpireReturn<Void> changeRelics(ShopScreen __instance) {
            if (ShopCorruption.handleRelics(__instance)) {
                if (Loader.isModLoaded("spicyShops")) {
                    //This field is normally set in a prefix patch to initRelics in SpicyShops
                    //However, if our prefix patch goes first, and terminates with this SpireReturn.Return(),
                    //the SpicyShops prefix patch may never be called.
                    //The symptom of this not being set is that potions don't show up, because lowestYPos is used to
                    //position the potions and it never gets initialized
                    try {
                        Class<?> clz = Class.forName("SpicyShops.patches.ExtraRelicChoicePatches");
                        Field f = clz.getField("lowestYPos");
                        f.set(null, 2.14748365E9F);
                    } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "initPotions")
    public static class ChangePotionsPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> changePotions(ShopScreen __instance) {
            if (ShopCorruption.handlePotions(__instance)) {
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "renderPurge")
    public static class RenderServicesPatch {
        @SpirePostfixPatch
        public static void renderServices(ShopScreen __instance, SpriteBatch sb) {
            ShopCorruption.renderServices(__instance, sb);
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "init")
    public static class SetTransformImagePatch {
        @SpirePostfixPatch
        public static void setTransformImage(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards, @ByRef Texture[] ___removeServiceImg) {
            ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                    ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                    : null;
            if (corruptionType == ShopCorruptionType.TransformReplacesRemove) {
                ___removeServiceImg[0] = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:ShopTransform"));
            }
            else {
                switch(Settings.language) {
                    case DEU:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/deu.png");
                        break;
                    case EPO:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/epo.png");
                        break;
                    case FRA:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/fra.png");
                        break;
                    case ITA:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/ita.png");
                        break;
                    case JPN:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/jpn.png");
                        break;
                    case KOR:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/kor.png");
                        break;
                    case RUS:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/rus.png");
                        break;
                    case THA:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/tha.png");
                        break;
                    case UKR:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/ukr.png");
                        break;
                    case ZHS:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/zhs.png");
                        break;
                    default:
                        ___removeServiceImg[0] = ImageMaster.loadImage("images/npcs/purge/eng.png");
                }
            }
        }
    }
    @SpirePatch(clz = ShopScreen.class, method = "updatePurgeCard")
    public static class TransformImageTooltipPatch {
        public static class TransformImageTooltipPatchExprEditor extends ExprEditor {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(TipHelper.class.getName()) && methodCall.getMethodName().equals("renderGenericTip")) {
                    methodCall.replace(String.format("{ if (!%1$s.handlePurgeCardTooltip($1, $2)) { $proceed($$); } }", ShopCorruption.class.getName()));
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor transformImageTooltipPatch() {
            return new TransformImageTooltipPatchExprEditor();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "purchasePurge")
    public static class PurchaseTransformResetServicePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> purchaseTransform(ShopScreen __instance) {
            ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
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
                return SpireReturn.Return();
            }
            else if (corruptionType == ShopCorruptionType.Service) {
                ShopScreenServiceInfo screenInfo = ShopScreenServiceInfoField.serviceInfo.get(__instance);
                screenInfo.currentService = null;
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "updatePurge")
    public static class UpdateTransformAndServicesPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> updateTransform(ShopScreen __instance) {
            ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                    ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                    : null;
            if (corruptionType == ShopCorruptionType.TransformReplacesRemove) {
                if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                    ShopServiceLog log = ShopCorruption.getShopServiceLog();
                    ShopScreen.purgeCard();
                    for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                        AbstractDungeon.player.masterDeck.removeCard(card);
                        AbstractDungeon.transformCard(card, false, AbstractDungeon.miscRng);
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.transformedCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        log.cardsTransformed.add(card.getMetricID());
                        log.cardsGained.add(AbstractDungeon.transformedCard.getMetricID());
                    }

                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    AbstractDungeon.shopScreen.purgeAvailable = false;
                }
                return SpireReturn.Return();
            }

            if (corruptionType == ShopCorruptionType.Service) {
                ShopServiceType currentService = ShopScreenServiceInfoField.serviceInfo.get(__instance).currentService;
                if (currentService == null) {
                    return SpireReturn.Continue();
                }

                ShopCorruption.performService(__instance, currentService);
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "updatePurgeCard")
    public static class UpdateServicesUserInterfacePatch {
        @SpirePostfixPatch
        public static void updateServicesUserInterface(ShopScreen __instance) {
            ShopCorruption.updateServicesUserInterface(__instance);
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "initCards")
    public static class GetPriceForCorruptedCardsPatch {
        public static class GetPriceForCorruptedCardsExprEditor extends ExprEditor {
            int hits = 0;
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(AbstractCard.class.getName()) && methodCall.getMethodName().equals("getPrice")) {
                    methodCall.replace(String.format("{ $_ = this.%3$s.get(i) instanceof %1$s ? %2$s.getCorruptedCardPrice((%1$s)this.%3$s.get(i)) : $proceed($$); }", AbstractCorruptedCard.class.getName(), ShopCorruption.class.getName(), hits == 0 ? "coloredCards" : "colorlessCards"));
                    hits++;
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor getPriceForCorruptedCards() {
            return new GetPriceForCorruptedCardsExprEditor();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "applyDiscount")
    public static class ServiceApplyDiscountPatch {
        @SpirePostfixPatch
        public static void serviceApplyDiscountPatch(ShopScreen __instance, float multiplier, boolean affectPurge) {
            ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                    ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                    : null;
            if (corruptionType == ShopCorruptionType.Service && affectPurge) {
                ShopScreenServiceInfo screenInfo = ShopScreenServiceInfoField.serviceInfo.get(__instance);
                screenInfo.serviceMultiplier *= multiplier;
            }
        }
    }

    @SpirePatch(cls = "expansioncontent.patches.ShopBossPatch", method = "Postfix", optional = true)
    public static class DownfallPreventShopBossPatchExprEditor extends ExprEditor {
        private static int counter = 0;
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            // With shop corruptions, the colorless cards that Downfall expects to exist may no longer be there
            if (methodCall.getClassName().equals(Random.class.getName()) && methodCall.getMethodName().equals("randomBoolean")) {
                methodCall.replace("{ $_ = $proceed($$) && colorlessCards.size() > " + counter + "; }");
                counter++;
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor preventCrash() {
            return new DownfallPreventShopBossPatchExprEditor();
        }
    }

    @SpirePatch(cls = "expansioncontent.patches.ShopBossPatch", method = "getReplacement", optional = true)
    public static class DownfallFixShopBossPatchRarityPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void DownfallFixShopBossPatchRarity(@ByRef AbstractCard.CardRarity[] rarity) {
            if (rarity[0] == AbstractCard.CardRarity.SPECIAL) {
                rarity[0] = AbstractCard.CardRarity.RARE;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(ArrayList.class);
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
