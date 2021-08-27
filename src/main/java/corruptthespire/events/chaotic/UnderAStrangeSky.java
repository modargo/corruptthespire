package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.ShimmeringShield;
import corruptthespire.relics.FragmentOfCorruption;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;

public class UnderAStrangeSky extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:UnderAStrangeSky";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int MAX_HP = 9;
    private static final int A15_MAX_HP = 7;
    private static final int FRAGMENTS = 2;
    private static final float HEAL_PERCENT = 0.33F;
    private static final float A15_HEAL_PERCENT = 0.20F;
    private static final int MAX_HP_LOSS = 4;
    private static final int A15_MAX_HP_LOSS = 6;

    private final AbstractCard card;
    private final int maxHp;
    private final int maxHpLoss;
    private final int heal;

    private int screenNum = 0;

    public UnderAStrangeSky() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.card = new ShimmeringShield();
        this.maxHp = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HP : MAX_HP;
        this.maxHpLoss = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HP_LOSS : MAX_HP_LOSS;
        this.heal = (int)(AbstractDungeon.player.maxHealth * (AbstractDungeon.ascensionLevel >= 15 ? A15_HEAL_PERCENT : HEAL_PERCENT));

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.card.name, this.maxHp), this.card);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], FRAGMENTS, new FragmentOfCorruption().name, this.heal), new FragmentOfCorruption());
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.maxHpLoss));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Shield
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.card.makeCopy(), (float)Settings.WIDTH  / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.player.increaseMaxHp(this.maxHp, true);
                        logMetric(ID, "Shield", Collections.singletonList(this.card.cardID), null, null, null, null, null, null, 0, 0, 0, this.maxHp, 0, 0);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Campfire
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new FragmentOfCorruption());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new FragmentOfCorruption());
                        AbstractDungeon.player.heal(this.heal);
                        logMetric(ID, "Campfire", null, null, null, null, Arrays.asList(FragmentOfCorruption.ID, FragmentOfCorruption.ID), null, null, 0, this.heal, 0, 0, 0, 0);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Depths
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHpLoss);
                        logMetricObtainRelicAndLoseMaxHP(ID, "Depths", relic, this.maxHpLoss);

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
