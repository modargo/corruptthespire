package corruptthespire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

public class FragmentOfCorruption extends CustomRelic {
    public static final String ID = "CorruptTheSpire:FragmentOfCorruption";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int PRICE = 40;

    public FragmentOfCorruption() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void increment() {
        if (this.counter < 0) {
            this.counter = 0;
        }
        this.counter++;
    }

    public void reduce(int amount) {
        this.counter -= amount;
        if (this.counter < 0) {
            this.counter = 0;
        }
    }

    @Override
    public void instantObtain() {
        if (AbstractDungeon.player.hasRelic(ID)) {
            FragmentOfCorruption fragment = (FragmentOfCorruption) AbstractDungeon.player.getRelic(ID);
            fragment.increment();
            fragment.flash();
        } else {
            super.instantObtain();
        }
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        if (AbstractDungeon.player.hasRelic(ID)) {
            FragmentOfCorruption fragment = (FragmentOfCorruption) AbstractDungeon.player.getRelic(ID);
            fragment.increment();
            fragment.flash();

            isDone = true;
            isObtained = true;
            discarded = true;
        } else {
            super.instantObtain(p, slot, callOnEquip);
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(ID)) {
            FragmentOfCorruption fragment = (FragmentOfCorruption) AbstractDungeon.player.getRelic(ID);
            fragment.increment();
            fragment.flash();
        } else {
            super.obtain();
        }
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FragmentOfCorruption();
    }
}
