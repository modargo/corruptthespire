package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class ChthonicBlessing extends CustomRelic {
    public static final String ID = "CorruptTheSpire:ChthonicBlessing";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int REWARDS = 1;
    private static final int GOLD = 15;
    private static final int COMBATS = 7;

    public ChthonicBlessing() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
            this.counter++;
        }
    }

    public void addRewards() {
        AbstractDungeon.getCurrRoom().addGoldToRewards(GOLD);
        if (this.counter >= COMBATS) {
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
            this.counter -= COMBATS;
        }
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards - REWARDS;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], GOLD, COMBATS, REWARDS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ChthonicBlessing();
    }
}
