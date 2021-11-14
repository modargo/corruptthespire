package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class HarbingersSkull extends CustomRelic {
    public static final String ID = "CorruptTheSpire:HarbingersSkull";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int BLOCK = 4;
    private static final int CARDS = 1;

    private boolean firstTurn = true;

    public HarbingersSkull() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
        this.firstTurn = true;
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.counter == 0) {
            this.addToBot(new GainBlockAction(AbstractDungeon.player, BLOCK));
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        if (this.counter == 0 && !this.firstTurn) {
            this.addToBot(new DrawCardAction(AbstractDungeon.player, CARDS));
        } else {
            this.firstTurn = false;
        }

        this.counter = 0;
        this.beginLongPulse();
    }


    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.POWER) {
            this.counter++;
            this.stopPulse();
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], BLOCK, CARDS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HarbingersSkull();
    }
}
