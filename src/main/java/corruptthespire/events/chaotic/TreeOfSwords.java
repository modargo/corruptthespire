package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.relics.chaotic.BurningRing;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TreeOfSwords extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:TreeOfSwords";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int TRANSFORM_CARDS = 2;
    private static final int GOLD = 100;
    public static final int DAMAGE = 8;
    public static final int A15_DAMAGE = 10;

    private final AbstractRelic relic;
    private final AbstractRelic startingRelic;
    private final int damage;

    private int screenNum = 0;

    public TreeOfSwords() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relic = new BurningRing();
        ArrayList<AbstractRelic> starterRelics = AbstractDungeon.player.relics.stream().filter(r -> r.tier == AbstractRelic.RelicTier.STARTER).collect(Collectors.toCollection(ArrayList::new));
        if (starterRelics.isEmpty()) {
            throw new RuntimeException("Tree of Swords event requires at least one starter relic");
        }
        this.startingRelic = starterRelics.get(0);
        this.damage = AbstractDungeon.ascensionLevel >= 15 ? A15_DAMAGE : DAMAGE;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], TRANSFORM_CARDS));
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.relic.name, this.startingRelic.name), this.relic);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], GOLD, DAMAGE));
    }

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() == TRANSFORM_CARDS) {
            List<String> transformedCards = new ArrayList<>();
            List<String> obtainedCards = new ArrayList<>();
            List<AbstractCard> possibleCards = null;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.untip();
                c.unhover();
                AbstractDungeon.player.masterDeck.removeCard(c);
                transformedCards.add(c.cardID);

                AbstractCard newCard;
                if (c.type == AbstractCard.CardType.CURSE) {
                    newCard = CardLibrary.getCurse(c, AbstractDungeon.miscRng).makeCopy();
                }
                else {
                    if (possibleCards == null) {
                        possibleCards = CardLibrary.cards.values().stream()
                            .filter(card -> card.type != AbstractCard.CardType.CURSE)
                            .filter(card -> card.type != AbstractCard.CardType.STATUS)
                            .filter(card -> card.rarity == AbstractCard.CardRarity.COMMON || card.rarity == AbstractCard.CardRarity.UNCOMMON || card.rarity == AbstractCard.CardRarity.RARE)
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toCollection(ArrayList::new));
                    }
                    newCard = possibleCards.get(AbstractDungeon.miscRng.random(possibleCards.size() - 1)).makeCopy();
                }
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(newCard.makeCopy(), (float)Settings.WIDTH * (float)(obtainedCards.size() + 1) / 3.0F, (float)Settings.HEIGHT / 2.0F, false));
                obtainedCards.add(newCard.cardID);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            logMetricTransformCards(ID, "Equipment", transformedCards, obtainedCards);
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Equipment
                        if (AbstractDungeon.isScreenUp) {
                            AbstractDungeon.dynamicBanner.hide();
                            AbstractDungeon.previousScreen = AbstractDungeon.screen;
                        }
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), TRANSFORM_CARDS, OPTIONS[4], false, false, false, false);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Relic
                        AbstractDungeon.player.loseRelic(this.startingRelic.relicId);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic);
                        logMetricRelicSwap(ID, "Relic", this.relic, this.startingRelic);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Coins
                        AbstractDungeon.player.gainGold(GOLD);
                        AbstractDungeon.player.damage(new DamageInfo(null, this.damage));
                        logMetricGainGoldAndDamage(ID, "Coins", GOLD, this.damage);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }
}
