package corruptthespire.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;

public class CardUtil {
    public static AbstractCard upgradeRandomCard() {
        ArrayList<AbstractCard> upgrade = upgradeRandomCards(1);
        return upgrade.isEmpty() ? null : upgrade.get(0);
    }

    public static ArrayList<AbstractCard> upgradeRandomCards(int n) {
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }

        int cardsToUpgrade = Math.min(n, upgradableCards.size());
        ArrayList<AbstractCard> upgradedCards = new ArrayList<>();
        if (cardsToUpgrade > 0) {
            Collections.shuffle(upgradableCards, AbstractDungeon.miscRng.random);
        }

        for (int i = 0; i < cardsToUpgrade; i++) {
            float x = (float)Settings.WIDTH * (float)(i + 1) / (float)(cardsToUpgrade + 1);
            float y = (float)Settings.HEIGHT / 2.0F;
            AbstractCard card = upgradableCards.get(i);
            card.upgrade();
            upgradedCards.add(card);
            AbstractDungeon.player.bottledCardUpgradeCheck(card);
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy(), x, y));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
        }

        return upgradedCards;
    }

    public static int getNumCardsForRewards() {
        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            numCards = r.changeNumberOfCardsInReward(numCards);
        }
        if (ModHelper.isModEnabled("Binary")) {
            --numCards;
        }
        return numCards;
    }

    public static AbstractCard getOtherColorCard(AbstractCard.CardRarity rarity) {
        return getOtherColorCard(rarity, new ArrayList<>(), null);
    }

    public static AbstractCard getOtherColorCard(AbstractCard.CardRarity rarity, List<AbstractCard.CardColor> excludedColors) {
        return getOtherColorCard(rarity, excludedColors, null);
    }

    public static AbstractCard getOtherColorCard(AbstractCard.CardRarity rarity, List<AbstractCard.CardColor> excludedColors, AbstractCard.CardType type) {
        CardGroup anyCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (AbstractCard c : CardLibrary.cards.values()) {
            if (c.type != AbstractCard.CardType.CURSE
                && c.type != AbstractCard.CardType.STATUS
                && (!UnlockTracker.isCardLocked(c.cardID) || Settings.treatEverythingAsUnlocked())
                && (type == null || c.type == type)
                && !excludedColors.contains(c.color)
            ) {
                anyCard.addToBottom(c);
            }
        }

        if (type == AbstractCard.CardType.POWER && rarity == AbstractCard.CardRarity.COMMON && anyCard.group.stream().noneMatch(c -> c.rarity == AbstractCard.CardRarity.COMMON)) {
            rarity = AbstractCard.CardRarity.UNCOMMON;
        }

        return anyCard.getRandomCard(true, rarity).makeCopy();
    }

    public static AbstractCard returnTrulyRandomCardInCombat(AbstractCard.CardRarity rarity) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        if (rarity == AbstractCard.CardRarity.COMMON) {
            for (AbstractCard c : srcCommonCardPool.group) {
                if (!c.hasTag(AbstractCard.CardTags.HEALING))
                    list.add(c);
            }
        }
        if (rarity == AbstractCard.CardRarity.UNCOMMON) {
            for (AbstractCard c : srcUncommonCardPool.group) {
                if (!c.hasTag(AbstractCard.CardTags.HEALING))
                    list.add(c);
            }
        }
        if (rarity == AbstractCard.CardRarity.RARE) {
            for (AbstractCard c : srcRareCardPool.group) {
                if (!c.hasTag(AbstractCard.CardTags.HEALING))
                    list.add(c);
            }
        }
        return list.get(cardRandomRng.random(list.size() - 1));
    }

    public static AbstractCard getRandomCardByTag(String tagName) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            for (AbstractCard.CardTags tag : c.tags) {
                if (tag != null && tag.name().equals(tagName)) {
                    list.add(c);
                }
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(list, AbstractDungeon.miscRng.random);
            return list.get(0);
        }
    }
}
