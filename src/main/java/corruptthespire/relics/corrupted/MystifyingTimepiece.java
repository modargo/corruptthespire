package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class MystifyingTimepiece extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:MystifyingTimepiece";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int COST = 0;
    private static final int CARDS = 2;

    private boolean firstTurn = true;

    public MystifyingTimepiece() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
        this.firstTurn = true;
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
        if (card.costForTurn == COST) {
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
        return MessageFormat.format(DESCRIPTIONS[0], COST, CARDS);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MystifyingTimepiece();
    }
}
