package corruptthespire.subscribers;

import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import corruptthespire.cards.Fated;

import java.util.ArrayList;

public class CheckFatedPostBattleSubscriber implements PostBattleSubscriber {
    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        boolean isVictorious = abstractRoom.monsters != null
               && abstractRoom.monsters.monsters != null
               && abstractRoom.monsters.areMonstersDead(); //No credit if you smoke bomb out
        if (isVictorious) {
            ArrayList<AbstractCard> fatedCards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.cardID.equals(Fated.ID)) {
                    if (c.misc <= 1) {
                        fatedCards.add(c);
                    }
                    else {
                        ((Fated)c).decrementMisc();
                    }
                }
            }
            for (AbstractCard c : fatedCards) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                AbstractDungeon.player.masterDeck.removeCard(c);
            }
        }
    }
}