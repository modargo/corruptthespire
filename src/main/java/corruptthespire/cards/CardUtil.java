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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CardUtil {
    public static AbstractCard upgradeRandomCard() {
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }

        if (!upgradableCards.isEmpty()) {
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractCard card = upgradableCards.get(AbstractDungeon.miscRng.random(upgradableCards.size() - 1));
            card.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(card);
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
            return card;
        }
        return null;
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
}
