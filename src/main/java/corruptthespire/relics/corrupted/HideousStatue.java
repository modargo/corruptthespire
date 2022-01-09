package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;
import corruptthespire.potions.PotionUtil;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class HideousStatue extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:HideousStatue";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    public static final int FIGHTS_PER_POTION = 3;

    public HideousStatue() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    public static void handleRewards() {
        if (Cor.getActNum() >= 3 && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            return;
        }

        HideousStatue r = (HideousStatue)AbstractDungeon.player.getRelic(HideousStatue.ID);
        if (r.counter + 1 >= FIGHTS_PER_POTION) {
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(PotionUtil.getRandomCorruptedPotion(Cor.rng)));
            r.counter = 0;
        }
        else {
            r.counter++;
        }
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.actNum < 3;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], FIGHTS_PER_POTION);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HideousStatue();
    }
}
