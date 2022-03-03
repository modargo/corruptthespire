package corruptthespire.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

@SpirePatch(clz = CardLibrary.class, method = "initialize")
public class UnlockCardsPatch {
    @SpirePostfixPatch
    public static void unlockCards() {
        for (String s : CardLibrary.cards.keySet()) {
            if (s.startsWith("CorruptTheSpire:")) {
                if (!CardLibrary.cards.get(s).isSeen) {
                    UnlockTracker.markCardAsSeen(s);
                }
            }
        }
    }
}
