package corruptthespire.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import corruptthespire.util.CollectionsUtil;

import java.util.ArrayList;
import java.util.Collections;

public class EldritchInsightAction extends AbstractGameAction {
    private static final int CARD_OPTIONS = 3;
    private boolean retrieveCard = false;

    public EldritchInsightAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        ArrayList<AbstractCard> generatedCards = this.generateCardChoices();
        if (!AbstractDungeon.player.drawPile.isEmpty()) {
            if (this.duration == Settings.ACTION_DUR_FAST) {
                AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], false);
            } else {
                if (!this.retrieveCard) {
                    if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                        AbstractCard card = AbstractDungeon.cardRewardScreen.discoveryCard;
                        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                            AbstractDungeon.player.drawPile.moveToHand(card, AbstractDungeon.player.drawPile);
                        } else {
                            AbstractDungeon.player.drawPile.moveToDiscardPile(card);
                            AbstractDungeon.player.createHandIsFullDialog();
                        }

                        AbstractDungeon.cardRewardScreen.discoveryCard = null;
                    }

                    this.retrieveCard = true;
                }
            }
        }
        this.tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> cards = new ArrayList<>(AbstractDungeon.player.drawPile.group);

        CollectionsUtil.shuffle(cards, AbstractDungeon.cardRandomRng);

        return new ArrayList<>(cards.subList(0, Math.min(CARD_OPTIONS, cards.size())));
    }
}
