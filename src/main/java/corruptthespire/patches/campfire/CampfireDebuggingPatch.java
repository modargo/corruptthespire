package corruptthespire.patches.campfire;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CampfireDebuggingPatch {
    private static final Logger logger = LogManager.getLogger(CampfireDebuggingPatch.class.getName());

    @SpirePatch(clz = CampfireSmithEffect.class, method = "update")
    public static class CampfireSmithEffectPatch {
        @SpirePostfixPatch
        public static void campfireSmithEffectUpdateLog(CampfireSmithEffect __instance) {
            logger.info("AbstractDungeon.isScreenUp: " + AbstractDungeon.isScreenUp);
            logger.info("AbstractDungeon.screen: " + AbstractDungeon.screen);
            logger.info("AbstractDungeon.previousScreen: " + AbstractDungeon.previousScreen);
            logger.info("__instance.duration: " + __instance.duration);
            logger.info("AbstractDungeon.effectsQueue.size(): " + AbstractDungeon.effectsQueue.size());
            logger.info("AbstractDungeon.getCurrRoom().phase: " + AbstractDungeon.getCurrRoom().phase);
            logger.info("AbstractDungeon.getCurrRoom().rewardTime: " + AbstractDungeon.getCurrRoom().rewardTime);
        }
    }
}
