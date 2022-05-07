package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.Fated;

import java.text.MessageFormat;

public class FutureSight extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:FutureSight";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int CORRUPTION = 12;
    private static final int A15_CORRUPTION = 15;

    private final AbstractCard curse;
    private final int corruption;

    private int screenNum = 0;

    public FutureSight() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.curse = new Fated();
        this.corruption = AbstractDungeon.ascensionLevel >= 15 ? A15_CORRUPTION : CORRUPTION;

        if (AbstractDungeon.bossRelicPool.size() < 3) {
            throw new RuntimeException("Future Sight event requires at least 3 boss relics left in the pool");
        }

        this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.curse.name), this.curse);
        this.imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Concentrate
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.curse, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));

                        String bossRelic1 = RelicLibrary.getRelic(AbstractDungeon.bossRelicPool.get(0)).name;
                        String bossRelic2 = RelicLibrary.getRelic(AbstractDungeon.bossRelicPool.get(1)).name;
                        String bossRelic3 = RelicLibrary.getRelic(AbstractDungeon.bossRelicPool.get(2)).name;
                        String description = DESCRIPTIONS[1].replace("{0}", FontHelper.colorString(bossRelic1, "b")).replace("{1}", FontHelper.colorString(bossRelic2, "b")).replace("{2}", FontHelper.colorString(bossRelic3, "b"));
                        this.imageEventText.updateBodyText(description);
                        this.screenNum = 1;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.corruption));
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        break;
                    case 1: // Close your eyes
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Change
                        AbstractDungeon.bossRelicPool.remove(0);
                        AbstractDungeon.bossRelicPool.remove(0);
                        AbstractDungeon.bossRelicPool.remove(0);
                        Cor.addCorruption(this.corruption);
                        logMetricObtainCard(ID, "Change", this.curse);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Accept
                        logMetricObtainCard(ID, "Accept", this.curse);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
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