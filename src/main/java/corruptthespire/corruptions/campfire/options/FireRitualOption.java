package corruptthespire.corruptions.campfire.options;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import corruptthespire.Cor;

import java.text.MessageFormat;

public class FireRitualOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:FireRitualOption");
    public static final String[] TEXT = uiStrings.TEXT;
    private static final int CORRUPTION_PER_FRAGMENT = 4;
    private static final int MAX_HEALTH_PER_FRAGMENT = 2;
    private final int fragmentCost;
    private final int corruptionReduction;
    private final int maxHealthCost;

    public FireRitualOption() {
        this.fragmentCost = Cor.getFragmentCount();
        this.corruptionReduction = this.fragmentCost * CORRUPTION_PER_FRAGMENT;
        this.maxHealthCost = this.fragmentCost * MAX_HEALTH_PER_FRAGMENT;
        this.usable = Cor.getFragmentCount() > 0;
        this.label = TEXT[0];
        this.description = MessageFormat.format(TEXT[1], this.corruptionReduction, this.maxHealthCost, this.fragmentCost);
        //TODO different image
        this.img = ImageMaster.CAMPFIRE_DIG_BUTTON;
    }

    @Override
    public void useOption() {
        //TODO wrap this in an effect so it looks less jarring
        Cor.reduceFragments(this.fragmentCost);
        Cor.addCorruption(-this.corruptionReduction);
        AbstractDungeon.player.decreaseMaxHealth(this.maxHealthCost);
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
    }
}
