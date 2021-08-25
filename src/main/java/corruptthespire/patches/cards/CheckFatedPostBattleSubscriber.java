package corruptthespire.patches.cards;

import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import corruptthespire.cards.Fated;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CheckFatedPostBattleSubscriber implements PostBattleSubscriber {
    public static final Logger logger = LogManager.getLogger(CheckFatedPostBattleSubscriber.class.getName());

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        logger.info("Checking if the combat was an victorious combat");
        boolean isVictorious = abstractRoom.monsters != null
               && abstractRoom.monsters.monsters != null
               && abstractRoom.monsters.areMonstersDead(); //No credit if you smoke bomb out
        if (isVictorious) {
            logger.info("Checking for Fated cards to remove");
            ArrayList<AbstractCard> fatedCards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.cardID.equals(Fated.ID)) {
                    logger.info("Found Fated with " + c.misc + " combat(s) left");
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