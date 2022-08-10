package corruptthespire.corruptions.shop;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;
import corruptthespire.cards.CardUtil;
import corruptthespire.cards.corrupted.CorruptedCardColor;
import corruptthespire.cards.corrupted.CorruptedCardUtil;
import corruptthespire.patches.core.CorruptedField;
import corruptthespire.patches.relics.BottledPrismPatch;
import corruptthespire.patches.shop.ShopCorruptionTypeField;
import corruptthespire.patches.shop.ShopScreenServiceInfoField;
import corruptthespire.relics.FragmentOfCorruption;
import corruptthespire.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class ShopCorruption {
    private static final Logger logger = LogManager.getLogger(ShopCorruption.class.getName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:ShopCorruption");
    private static final UIStrings serviceUiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:ShopService");
    private static final String[] TEXT = uiStrings.TEXT;
    private static final String[] STEXT = serviceUiStrings.TEXT;

    private static final Texture shopTransformImage = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:ShopTransform"));
    private static final Texture shopUpgradeImage = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:ShopUpgrade"));
    private static final Texture shopDuplicateImage = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:ShopDuplicate"));
    private static final Texture shopCorruptImage = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:ShopCorrupt"));

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
            if (Cor.rng.randomBoolean()) {
                colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.UNCOMMON).makeCopy());
                colorlessCards.add(CorruptedCardUtil.getRandomCorruptedCard());
            }
            else {
                colorlessCards.add(CorruptedCardUtil.getRandomCorruptedCard());
                colorlessCards.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());
            }
        }

        if (corruptionType == ShopCorruptionType.Service) {
            colorlessCards.clear();
        }
    }

    public static boolean handleRelics(ShopScreen shopScreen) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType != null) {
            ArrayList<StoreRelic> relics = ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "relics");
            relics.clear();

            int numRelics = corruptionType == ShopCorruptionType.Service ? 0
                : corruptionType == ShopCorruptionType.CorruptedRelicsReplacePotions || Loader.isModLoaded("spicyShops") ? 6
                : 3;
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

                boolean error = false;
                try {
                    tempRelic.getPrice();
                }
                catch (Exception e) {
                    logger.error("Error getting price for relic: " + tempRelic.relicId, e);
                    error = true;
                }

                if (!error) {
                    StoreRelic relic = new StoreRelic(tempRelic, i, shopScreen);
                    if (!Settings.isDailyRun) {
                        relic.price = MathUtils.round((float)relic.price * AbstractDungeon.merchantRng.random(0.95F, 1.05F));
                    }
                    relics.add(relic);
                }
            }
            return true;
        }

        return false;
    }

    public static boolean handlePotions(ShopScreen shopScreen) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;

        if (corruptionType == ShopCorruptionType.CorruptedRelicsReplacePotions || corruptionType == ShopCorruptionType.CorruptedCards || corruptionType == ShopCorruptionType.Service) {
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

    public static void renderServices(ShopScreen shopScreen, SpriteBatch sb) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;
        if (corruptionType != ShopCorruptionType.Service) {
            return;
        }

        ShopScreenServiceInfo screenInfo = ShopScreenServiceInfoField.serviceInfo.get(shopScreen);
        float purgeCardY = getPurgeCardY(shopScreen);

        for (ShopServiceInfo info : getShopServiceInfos()) {
            float scale = screenInfo.serviceScales.containsKey(info.type) ? screenInfo.serviceScales.get(info.type) : getBaseServiceScale();
            int cost = MathUtils.round(info.cost * screenInfo.serviceMultiplier);
            renderService(shopScreen, sb, info.img, info.x, purgeCardY, scale, cost, !screenInfo.usedServices.contains(info.type));
        }
    }

    private static void renderService(ShopScreen shopScreen, SpriteBatch sb, Texture img, float x, float y, float scale, int cost, boolean available) {
        float GOLD_IMG_OFFSET_X = -50.0F * Settings.scale;
        float GOLD_IMG_OFFSET_Y = -215.0F * Settings.scale;
        float PRICE_TEXT_OFFSET_X = 16.0F * Settings.scale;
        float PRICE_TEXT_OFFSET_Y = -180.0F * Settings.scale;
        float GOLD_IMG_SIZE = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;
        sb.setColor(Settings.QUARTER_TRANSPARENT_BLACK_COLOR);
        TextureAtlas.AtlasRegion tmpImg = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
        sb.draw(tmpImg, x + 18.0F * Settings.scale + tmpImg.offsetX - (float)tmpImg.originalWidth / 2.0F, y - 14.0F * Settings.scale + tmpImg.offsetY - (float)tmpImg.originalHeight / 2.0F, (float)tmpImg.originalWidth / 2.0F - tmpImg.offsetX, (float)tmpImg.originalHeight / 2.0F - tmpImg.offsetY, (float)tmpImg.packedWidth, (float)tmpImg.packedHeight, scale, scale, 0.0F);
        sb.setColor(Color.WHITE);
        if (available) {
            sb.draw(img, x - 256.0F, y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, scale, scale, 0.0F, 0, 0, 512, 512, false, false);
            sb.draw(ImageMaster.UI_GOLD, x + GOLD_IMG_OFFSET_X, y + GOLD_IMG_OFFSET_Y - (scale / Settings.scale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_SIZE, GOLD_IMG_SIZE);
            Color color = Color.WHITE;
            if (cost > AbstractDungeon.player.gold) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(cost), x + PRICE_TEXT_OFFSET_X, y + PRICE_TEXT_OFFSET_Y - (scale / Settings.scale - 0.75F) * 200.0F * Settings.scale, color);
        } else {
            sb.draw(ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "soldOutImg"), x - 256.0F, y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, scale, scale, 0.0F, 0, 0, 512, 512, false, false);
        }
    }

    public static void updateServicesUserInterface(ShopScreen shopScreen) {
        ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                : null;
        if (corruptionType != ShopCorruptionType.Service) {
            return;
        }

        ShopScreenServiceInfo screenInfo = ShopScreenServiceInfoField.serviceInfo.get(shopScreen);
        float purgeCardY = getPurgeCardY(shopScreen);
        boolean hovered = false;
        boolean touched = false;
        for (ShopServiceInfo info : getShopServiceInfos()) {
            if (!screenInfo.usedServices.contains(info.type)) {
                float CARD_W = 110.0F * Settings.scale;
                float CARD_H = 150.0F * Settings.scale;
                if ((float) InputHelper.mX > info.x - CARD_W && (float)InputHelper.mX < info.x + CARD_W && (float)InputHelper.mY > purgeCardY - CARD_H && (float)InputHelper.mY < purgeCardY + CARD_H) {
                    screenInfo.hoveredService = info.type;
                    hovered = true;
                    shopScreen.moveHand(info.x - AbstractCard.IMG_WIDTH / 2.0F, purgeCardY);
                    ReflectionHacks.setPrivate(shopScreen, ShopScreen.class, "somethingHovered", true);
                    screenInfo.serviceScales.put(info.type, Settings.scale);
                }

                if (info.type != screenInfo.hoveredService) {
                    float currentScale = screenInfo.serviceScales.containsKey(info.type) ? screenInfo.serviceScales.get(info.type) : getBaseServiceScale();
                    screenInfo.serviceScales.put(info.type, MathHelper.cardScaleLerpSnap(currentScale, getBaseServiceScale()));
                } else {
                    if (InputHelper.justReleasedClickLeft || CInputActionSet.select.isJustPressed()) {
                        if (!Settings.isTouchScreen) {
                            CInputActionSet.select.unpress();
                            purchaseService(shopScreen, info);
                        } else if (info.type != screenInfo.touchedService) {
                            if (AbstractDungeon.player.gold < info.cost) {
                                shopScreen.playCantBuySfx();
                                shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
                            } else {
                                shopScreen.confirmButton.hideInstantly();
                                shopScreen.confirmButton.show();
                                shopScreen.confirmButton.hb.clickStarted = false;
                                shopScreen.confirmButton.isDisabled = false;
                                screenInfo.touchedService = info.type;
                                touched = true;
                            }
                        }
                    }

                    TipHelper.renderGenericTip((float)InputHelper.mX - 360.0F * Settings.scale, (float)InputHelper.mY - 70.0F * Settings.scale, getServiceLabel(info.type), getServiceText(info.type));
                }
            } else {
                float currentScale = screenInfo.serviceScales.containsKey(info.type) ? screenInfo.serviceScales.get(info.type) : getBaseServiceScale();
                screenInfo.serviceScales.put(info.type, MathHelper.cardScaleLerpSnap(currentScale, getBaseServiceScale()));
            }
        }

        if (!hovered) {
            screenInfo.hoveredService = null;
        }

        if (!touched) {
            screenInfo.touchedService = null;
        }
    }

    private static void purchaseService(ShopScreen shopScreen, ShopServiceInfo info) {
        ShopScreenServiceInfo screenInfo = ShopScreenServiceInfoField.serviceInfo.get(shopScreen);
        screenInfo.hoveredService = null;
        if (AbstractDungeon.player.gold >= info.cost) {
            logger.info("Purchasing service: " + info.type.name());
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
            CardGroup group;
            switch (info.type) {
                case Transform:
                case Corrupt:
                    group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards());
                    break;
                case Upgrade:
                    group = AbstractDungeon.player.masterDeck.getUpgradableCards();
                    break;
                case Duplicate:
                    group = AbstractDungeon.player.masterDeck;
                    break;
                default:
                    throw new RuntimeException("Unrecognized shop service type " + info.type.name());
            }
            AbstractDungeon.gridSelectScreen.open(group, 1, getServiceSelectScreenText(info.type), info.type == ShopServiceType.Upgrade, info.type == ShopServiceType.Transform, true, false);
            screenInfo.currentService = info.type;
        } else {
            shopScreen.playCantBuySfx();
            shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
        }
    }

    public static void performService(ShopScreen shopScreen, ShopServiceType type) {
        ShopScreenServiceInfo screenInfo = ShopScreenServiceInfoField.serviceInfo.get(shopScreen);
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            logger.info("Performing service: " + type.name());
            AbstractDungeon.player.loseGold(getShopServiceInfo(type).cost);
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                switch (type) {
                    case Transform:
                        CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
                        AbstractDungeon.player.masterDeck.removeCard(card);
                        AbstractDungeon.transformCard(card, false, AbstractDungeon.miscRng);
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.transformedCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case Upgrade:
                        card.upgrade();
                        AbstractDungeon.player.bottledCardUpgradeCheck(card);
                        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
                        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case Duplicate:
                        AbstractCard newCard = card.makeStatEquivalentCopy();
                        newCard.inBottleFlame = false;
                        newCard.inBottleLightning = false;
                        newCard.inBottleTornado = false;
                        BottledPrismPatch.InBottledPrismField.inBottlePrism.set(newCard, false);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(newCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    case Corrupt:
                        CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
                        AbstractDungeon.player.masterDeck.removeCard(card);
                        AbstractCard corruptedCard = CorruptedCardUtil.getRandomCorruptedCard();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(corruptedCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        break;
                    default:
                        throw new RuntimeException("Unrecognized shop service type " + type.name());
                }
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            screenInfo.usedServices.add(type);
        }
    }

    private static List<ShopServiceInfo> getShopServiceInfos() {
        float DRAW_START_X = (float)Settings.WIDTH * 0.16F;
        int tmp = (int)((float)Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
        float padX = (float)((int)((float)tmp + AbstractCard.IMG_WIDTH_S)) + 10.0F * Settings.scale;
        float purgeCardX = 1554.0F * Settings.xScale;
        ShopServiceInfo transformInfo = new ShopServiceInfo(ShopServiceType.Transform, 100, shopTransformImage);
        ShopServiceInfo upgradeInfo = new ShopServiceInfo(ShopServiceType.Upgrade, 100, shopUpgradeImage);
        ShopServiceInfo duplicateInfo = new ShopServiceInfo(ShopServiceType.Duplicate, 200, shopDuplicateImage);
        ShopServiceInfo corruptInfo = new ShopServiceInfo(ShopServiceType.Corrupt, 150, shopCorruptImage);
        List<ShopServiceInfo> infos = Arrays.asList(transformInfo, upgradeInfo, duplicateInfo, corruptInfo);
        for (int i = 0; i < infos.size(); i++) {
            infos.get(i).x = purgeCardX - (i + 1) * padX;
        }
        return infos;
    }

    private static ShopServiceInfo getShopServiceInfo(ShopServiceType type) {
        return getShopServiceInfos().stream().filter(i -> i.type == type).collect(Collectors.toList()).get(0);
    }

    private static float getPurgeCardY(ShopScreen shopScreen) {
        float BOTTOM_ROW_Y = 337.0F * Settings.yScale;
        return (float)ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "rugY") + BOTTOM_ROW_Y;
    }

    private static float getBaseServiceScale() {
        return 0.75F * Settings.scale;
    }

    private static String getServiceLabel(ShopServiceType type) {
        switch (type) {
            case Transform:
                return STEXT[0];
            case Upgrade:
                return STEXT[3];
            case Duplicate:
                return STEXT[6];
            case Corrupt:
                return STEXT[9];
            default:
                throw new RuntimeException("Unrecognized shop service type " + type.name());
        }
    }

    private static String getServiceText(ShopServiceType type) {
        switch (type) {
            case Transform:
                return STEXT[1];
            case Upgrade:
                return STEXT[4];
            case Duplicate:
                return STEXT[7];
            case Corrupt:
                return STEXT[10];
            default:
                throw new RuntimeException("Unrecognized shop service type " + type.name());
        }
    }

    private static String getServiceSelectScreenText(ShopServiceType type) {
        switch (type) {
            case Transform:
                return STEXT[2];
            case Upgrade:
                return STEXT[5];
            case Duplicate:
                return STEXT[8];
            case Corrupt:
                return STEXT[11];
            default:
                throw new RuntimeException("Unrecognized shop service type " + type.name());
        }
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
