package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class GnosticBlessing extends CustomRelic {
    public static final String ID = "CorruptTheSpire:GnosticBlessing";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int DRAW = 1;
    private static final int METALLICIZE = 2;
    private static final int REWARDS = 1;

    public GnosticBlessing() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.masterHandSize += DRAW;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.masterHandSize -= DRAW;
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MetallicizePower(AbstractDungeon.player, METALLICIZE)));
    }

    @Override
    public void atTurnStart() {
        this.flash();
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards - REWARDS;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], DRAW, METALLICIZE, REWARDS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GnosticBlessing();
    }
}
