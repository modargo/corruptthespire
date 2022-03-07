package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import corruptthespire.cards.corrupted.CorruptedCardColor;
import corruptthespire.powers.PowerUtil;

public class HiddenPotentialAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private final int draw;
    private final int artifact;

    public HiddenPotentialAction(int draw, int artifact) {
        this.draw = draw;
        this.artifact = artifact;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DISCARD;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.player.hand.size() == 0) {
                this.fire(null);
            }
            else if (AbstractDungeon.player.hand.size() == 1) {
                AbstractCard c = AbstractDungeon.player.hand.getTopCard();
                this.fire(c);
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
            }
        }
        else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                AbstractCard c = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                this.fire(c);
            }
        }
        tickDuration();
    }

    private void fire(AbstractCard c) {
        if (c != null) {
            if (c.color == CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED || c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) {
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, this.artifact)));
                this.addToTop(new DrawCardAction(this.draw));
            }
            AbstractDungeon.player.hand.moveToDiscardPile(c);
            c.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);
        }
        this.isDone = true;
    }
}
