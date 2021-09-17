package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.CardUtil;
import corruptthespire.cards.CustomTags;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class TheChoice extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:TheChoice";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static final int CORRUPTION_GAIN = 8;
    public static final int A15_CORRUPTION_GAIN = 10;
    public static final int UPGRADES = 2;
    public static final int CORRUPTION_REDUCTION = 10;
    public static final int A15_CORRUPTION_REDUCTION = 8;

    private final int corruptionGain;
    private final int corruptionReduction;
    private final AbstractCard corruptedCard;

    private int screenNum = 0;

    public TheChoice() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.corruptionGain = AbstractDungeon.ascensionLevel >= 15 ? A15_CORRUPTION_GAIN : CORRUPTION_GAIN;
        this.corruptionReduction = AbstractDungeon.ascensionLevel >= 15 ? A15_CORRUPTION_REDUCTION : CORRUPTION_REDUCTION;
        this.corruptedCard = CardUtil.getRandomCardByTag(CustomTags.CORRUPTED.name());
        String cardName;
        if (this.corruptedCard == null) {
            throw new RuntimeException("TheChoice event requires at least one corrupted card.");
        }
        else {
            cardName = this.corruptedCard.name;
        }

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], UPGRADES, this.corruptionGain));
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.corruptionReduction, cardName), this.corruptedCard.makeCopy());
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Embrace
                        Cor.addCorruption(this.corruptionGain);
                        ArrayList<AbstractCard> upgradedCards = CardUtil.upgradeRandomCards(UPGRADES);
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        logMetric(ID, "Embrace", null, null, null, upgradedCards.stream().map(c -> c.cardID).collect(Collectors.toCollection(ArrayList::new)), Collections.singletonList(relic.relicId), null, null, 0, 0, 0, 0, 0, 0);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Reject
                        Cor.addCorruption(-this.corruptionReduction);
                        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(this.corruptedCard, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                        AbstractDungeon.player.masterDeck.removeCard(this.corruptedCard);
                        logMetricCardRemoval(ID, "Reject", this.corruptedCard);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
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
