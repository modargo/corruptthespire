package corruptthespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
    private final int block;
    private final int draw;
    private final int artifact;
    private final int abysstouched;

    public HiddenPotentialAction(int block, int draw, int artifact, int abysstouched) {
        this.block = block;
        this.draw = draw;
        this.artifact = artifact;
        this.abysstouched = abysstouched;
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
            if (c.color == CorruptedCardColor.CORRUPTTHESPIRE_CORRUPTED) {
                this.addToTop(new DrawCardAction(this.draw));
            }
            if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) {
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, this.artifact)));
            }
            else if (c.type == AbstractCard.CardType.ATTACK) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDead && !m.isDying) {
                        this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, PowerUtil.abysstouched(m, this.abysstouched)));
                    }
                }
            }
            else if (c.type == AbstractCard.CardType.SKILL || c.type == AbstractCard.CardType.POWER) {
                this.addToTop(new GainBlockAction(AbstractDungeon.player, this.block));
            }
            this.addToTop(new DiscardSpecificCardAction(c));
        }
        this.isDone = true;
    }
}
