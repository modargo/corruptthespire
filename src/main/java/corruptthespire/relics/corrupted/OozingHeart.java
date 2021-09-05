package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class OozingHeart extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:OozingHeart";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int HEAL = 22;
    private static final int HEALTH_LOSS = 1;

    public OozingHeart() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room instanceof MonsterRoomElite || room.eliteTrigger) {
            this.flash();
            this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL, 0.0F));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (AbstractDungeon.player.currentHealth > HEALTH_LOSS) {
            this.flash();
            AbstractDungeon.player.damage(new DamageInfo(null, HEALTH_LOSS));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], HEAL, HEALTH_LOSS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OozingHeart();
    }
}
