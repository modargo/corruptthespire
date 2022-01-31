package corruptthespire.events.special;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.MaskedBandits;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import corruptthespire.Cor;
import corruptthespire.monsters.Encounters;
import corruptthespire.relics.chaotic.TranscendentEye;
import corruptthespire.relics.chaotic.TranscendentPet;

// We extend the MaskedBandits event because ProceedButton.java specifically checks if an event is an instance of this type
// (or a few other types) in the logic for what happens when you click proceed. This is easier than a patch.
public class DevourerEvent extends MaskedBandits {
    public static final String ID = "CorruptTheSpire:Devourer";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private int screen = 0;

    public DevourerEvent() {
        this.roomEventText.clear();
        this.body = DESCRIPTIONS[0];
        this.roomEventText.addDialogOption(OPTIONS[0]);
        this.roomEventText.addDialogOption(OPTIONS[1]);
        this.hasDialog = true;
        this.hasFocus = true;
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(Encounters.DEVOURER);
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

                        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                        AbstractDungeon.getCurrRoom().addRelicToRewards(new TranscendentEye());
                        AbstractDungeon.getCurrRoom().addRelicToRewards(new TranscendentPet());

                        AbstractDungeon.getCurrRoom().eliteTrigger = true;
                        this.enterCombat();
                        AbstractDungeon.lastCombatMetricKey = Encounters.DEVOURER;
                        break;
                    case 1:
                        logMetricIgnored(ID);
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