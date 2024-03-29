package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.MaskedBandits;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.Cor;
import corruptthespire.monsters.Encounters;

import java.text.MessageFormat;

// We extend the MaskedBandits event because ProceedButton.java specifically checks if an event is an instance of this type
// (or a few other types) in the logic for what happens when you click proceed. This is easier than a patch.
public class EpicenterOfCorruption extends MaskedBandits {
    public static final String ID = "CorruptTheSpire:EpicenterOfCorruption";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private int screen = 0;

    private static final int CORRUPTION = 1;
    private static final int A15_CORRUPTION = 2;

    private final int corruption;
    private final String encounterID;

    public EpicenterOfCorruption() {
        this.corruption = AbstractDungeon.ascensionLevel >= 15 ? A15_CORRUPTION : CORRUPTION;

        this.roomEventText.clear();
        this.body = DESCRIPTIONS[0];
        this.roomEventText.addDialogOption(OPTIONS[0]);
        this.roomEventText.addDialogOption(MessageFormat.format(OPTIONS[1], this.corruption));
        this.hasDialog = true;
        this.hasFocus = true;
        this.encounterID = Encounters.HORDE_ENCOUNTERS[Cor.rng.random(Encounters.HORDE_ENCOUNTERS.length - 1)];
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(this.encounterID);
    }

    @Override
    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        Cor.flags.seenHarbinger = true;
        switch(this.screen) {
            case 0:
                switch(buttonPressed) {
                    case 0:
                        logMetric(ID, "Fight");

                        if (Settings.isDailyRun) {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(30));
                        } else {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(25, 35));
                        }

                        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);

                        this.enterCombat();
                        AbstractDungeon.lastCombatMetricKey = this.encounterID;
                        break;
                    case 1:
                        Cor.addCorruption(this.corruption);
                        logMetric(ID, "Retreat");
                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.roomEventText.updateDialogOption(0, OPTIONS[2]);
                        this.roomEventText.clearRemainingOptions();
                        this.screen = 1;
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }
}