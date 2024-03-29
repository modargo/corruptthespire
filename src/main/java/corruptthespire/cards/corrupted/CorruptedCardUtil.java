package corruptthespire.cards.corrupted;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.daily.mods.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import corruptthespire.Cor;
import corruptthespire.cards.corrupted.attacks.*;
import corruptthespire.cards.corrupted.powers.*;
import corruptthespire.cards.corrupted.skills.*;
import corruptthespire.util.CollectionsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class CorruptedCardUtil {
    private static final float CORRUPTED_RARE_CHANCE = 0.2F;
    public static final int CORRUPTED_COMMON_PRICE = 50;
    public static final int CORRUPTED_RARE_PRICE = 125;

    public static Map<String, CorruptedCardInfo> getAllCorruptedCardInfos() {
        return getAllCorruptedCardInfos(false);
    }

    public static Map<String, CorruptedCardInfo> getAllCorruptedCardInfos(boolean ignoreClass) {
        ArrayList<CorruptedCardInfo> corruptedCardInfos = new ArrayList<>();

        //Attacks
        corruptedCardInfos.add(new CorruptedCardInfo(new AnnihilationRay(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new BlazeFromBeyond(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new Condemn(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new DeathTouch(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new EssenceRip(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new Retribution(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new ShadowAndFlame(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new AstralGust(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new DrainLife(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new EldritchFire(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new Fragmentize(), AbstractCard.CardRarity.RARE));

        //Skills
        corruptedCardInfos.add(new CorruptedCardInfo(new BlasphemousHymn(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new EldritchInsight(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new HiddenPotential(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new Irradiate(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new OccultMachine(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new ProfaneShield(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new Unearth(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new VoidArmor(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new WickedWard(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new DarkLore(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new DevilsBargain(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new MaskOfShards(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new NightsWhisper(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new Secundus(), AbstractCard.CardRarity.RARE));

        //Powers
        corruptedCardInfos.add(new CorruptedCardInfo(new BlackOmen(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new InfernalBargain(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new InnerFlame(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new JourneyBeyond(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new ManiacalRage(), AbstractCard.CardRarity.COMMON));
        corruptedCardInfos.add(new CorruptedCardInfo(new CorruptedForm(), AbstractCard.CardRarity.RARE));
        corruptedCardInfos.add(new CorruptedCardInfo(new LordOfNightmare(), AbstractCard.CardRarity.RARE));

        //Class-specific
        if (ignoreClass || AbstractDungeon.player.hasRelic(PrismaticShard.ID) || AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.RED || ModHelper.isModEnabled(RedCards.ID) || ModHelper.isModEnabled(Diverse.ID)) {
            corruptedCardInfos.add(new CorruptedCardInfo(new LightningBlood(), AbstractCard.CardRarity.COMMON));
        }
        if (ignoreClass || AbstractDungeon.player.hasRelic(PrismaticShard.ID) || AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.GREEN || ModHelper.isModEnabled(GreenCards.ID) || ModHelper.isModEnabled(Diverse.ID)) {
            corruptedCardInfos.add(new CorruptedCardInfo(new UnnaturalOrder(), AbstractCard.CardRarity.COMMON));
        }
        if (ignoreClass || AbstractDungeon.player.hasRelic(PrismaticShard.ID) || AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.BLUE || ModHelper.isModEnabled(BlueCards.ID) || ModHelper.isModEnabled(Diverse.ID)) {
            corruptedCardInfos.add(new CorruptedCardInfo(new GuidingStar(), AbstractCard.CardRarity.COMMON));
        }
        if (ignoreClass || AbstractDungeon.player.hasRelic(PrismaticShard.ID) || AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.PURPLE || ModHelper.isModEnabled(PurpleCards.ID) || ModHelper.isModEnabled(Diverse.ID)) {
            corruptedCardInfos.add(new CorruptedCardInfo(new DivineStrike(), AbstractCard.CardRarity.COMMON));
        }
        if (Loader.isModLoaded("Shaman")) {
            if (ignoreClass || AbstractDungeon.player.hasRelic(PrismaticShard.ID) || AbstractDungeon.player.getCardColor().name().equals("SHAMAN_COLOR") || ModHelper.isModEnabled(Diverse.ID)) {
                corruptedCardInfos.add(new CorruptedCardInfo(new ElementalBarrier(), AbstractCard.CardRarity.COMMON));
            }
        }
        if (Loader.isModLoaded("Channeler")) {
            if (ignoreClass || AbstractDungeon.player.hasRelic(PrismaticShard.ID) || AbstractDungeon.player.getCardColor().name().equals("CHANNELER_COLOR") || ModHelper.isModEnabled(Diverse.ID)) {
                corruptedCardInfos.add(new CorruptedCardInfo(new DiabolicForce(), AbstractCard.CardRarity.COMMON));
            }
        }

        if (Loader.isModLoaded("anniv5")) {
            if (ignoreClass || AbstractDungeon.player.hasRelic(PrismaticShard.ID) || AbstractDungeon.player.getCardColor().name().equals("PACKMASTER_RAINBOW") || ModHelper.isModEnabled(Diverse.ID)) {
                corruptedCardInfos.add(new CorruptedCardInfo(new OtherworldlyPack(), AbstractCard.CardRarity.COMMON));
            }
        }

        return corruptedCardInfos.stream()
            .collect(Collectors.toMap(cci -> cci.card.cardID, cci -> cci));
    }

    public static RewardItem getCorruptedCardReward() {
        RewardItem reward = new RewardItem();
        reward.cards = getRandomCorruptedCards(reward.cards.size());
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            for (AbstractCard c : reward.cards) {
                r.onPreviewObtainCard(c);
            }
        }
        return reward;
    }

    //Following the pattern of the base game, the distinction is that in combat means:
    //(1) It doesn't respect card rarity
    //(2) It uses cardRandomRng instead of cardRng
    //(3) It cannot return healing cards
    public static ArrayList<AbstractCard> getRandomCorruptedCardsInCombat(int n) {
        ArrayList<CorruptedCardInfo> allCorruptedCards = new ArrayList<>(getAllCorruptedCardInfos().values());
        allCorruptedCards.sort(Comparator.comparing(o -> o.card.cardID));
        CollectionsUtil.shuffle(allCorruptedCards, AbstractDungeon.cardRandomRng);
        return allCorruptedCards.stream()
                .filter(cci -> !cci.card.hasTag(AbstractCard.CardTags.HEALING))
                .map(cci -> cci.card.makeCopy())
                .limit(n)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //Following the pattern of the base game, the distinction is that for transform:
    //(1) It doesn't respect card rarity
    //(2) It uses the provided RNG
    //(3) It always proceeds one-by-one
    //Keeping to this pattern also helps ensure we only get a single random int, to avoid RNG instability
    public static AbstractCard getRandomCorruptedCardForTransform(Random rng) {
        ArrayList<CorruptedCardInfo> allCorruptedCards = new ArrayList<>(getAllCorruptedCardInfos().values());
        allCorruptedCards.sort(Comparator.comparing(o -> o.card.cardID));
        return allCorruptedCards.get(rng.random(allCorruptedCards.size() - 1)).card.makeCopy();
    }

    public static AbstractCard getRandomCorruptedCard() {
        return getRandomCorruptedCards(1).get(0);
    }

    public static ArrayList<AbstractCard> getRandomCorruptedCards(int n) {
        return getRandomCorruptedCards(n, null);
    }

    public static ArrayList<AbstractCard> getRandomCorruptedCards(int n, AbstractCard.CardType type) {
        return getRandomCorruptedCards(n, type, Cor.rewardRng);
    }

    public static ArrayList<AbstractCard> getRandomCorruptedCards(int n, AbstractCard.CardType type, Random rng) {
        ArrayList<AbstractCard.CardRarity> rarities = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rarities.add(rollCorruptedRarity(rng));
        }

        ArrayList<AbstractCard> cards = new ArrayList<>();
        ArrayList<AbstractCard> corruptedCommons = null;
        ArrayList<AbstractCard> corruptedRares = null;
        for (AbstractCard.CardRarity rarity : rarities) {
            AbstractCard card = null;
            if (rarity == AbstractCard.CardRarity.COMMON) {
                if (corruptedCommons == null) {
                    corruptedCommons = getAllCorruptedCardInfos().values().stream()
                            .filter(cci -> cci.rarity == AbstractCard.CardRarity.COMMON)
                            .filter(cci -> type == null || cci.card.type == type)
                            .map(cci -> cci.card)
                            .collect(Collectors.toCollection(ArrayList::new));
                    CollectionsUtil.shuffle(corruptedCommons, rng);
                }
                if (!corruptedCommons.isEmpty()) {
                    card = corruptedCommons.remove(0);
                }
            }
            else {
                if (corruptedRares == null) {
                    corruptedRares = getAllCorruptedCardInfos().values().stream()
                            .filter(cci -> cci.rarity == AbstractCard.CardRarity.RARE)
                            .filter(cci -> type == null || cci.card.type == type)
                            .map(cci -> cci.card)
                            .collect(Collectors.toCollection(ArrayList::new));
                    CollectionsUtil.shuffle(corruptedRares, rng);
                }
                if (!corruptedRares.isEmpty()) {
                    card = corruptedRares.remove(0);
                }
            }
            if (card != null) {
                cards.add(card.makeCopy());
            }
        }

        return cards;
    }

    private static AbstractCard.CardRarity rollCorruptedRarity(Random rng) {
        return rng.randomBoolean(CORRUPTED_RARE_CHANCE) ? AbstractCard.CardRarity.RARE : AbstractCard.CardRarity.COMMON;
    }
}
