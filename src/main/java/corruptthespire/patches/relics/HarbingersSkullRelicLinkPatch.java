package corruptthespire.patches.relics;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.relics.chaotic.HarbingersClaw;
import corruptthespire.relics.chaotic.HarbingersSkull;
import javassist.CtBehavior;

import java.util.ArrayList;

public class HarbingersSkullRelicLinkPatch {
    @SpirePatch(clz = RewardItem.class, method = "render")
    public static class RenderRelicLinkPatch {
        @SpirePostfixPatch
        public static void renderRelicLink(RewardItem __instance, SpriteBatch sb) {
            if (__instance.type == RewardItem.RewardType.RELIC && __instance.relicLink != null && __instance.relic.relicId.equals(HarbingersSkull.ID)) {
                ReflectionHacks.privateMethod(RewardItem.class, "renderRelicLink", SpriteBatch.class).invoke(__instance, sb);
            }
        }
    }

    @SpirePatch(clz = RewardItem.class, method = "render")
    public static class RelicTextPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tips"})
        public static void addRelicLinkTip(RewardItem __instance, SpriteBatch sb, ArrayList<PowerTip> tips) {
            if (__instance.type == RewardItem.RewardType.RELIC
                    && __instance.hb.hovered
                    && __instance.relicLink != null
                    && __instance.relicLink.type == RewardItem.RewardType.RELIC
                    && (__instance.relic.relicId.equals(HarbingersClaw.ID) || __instance.relic.relicId.equals(HarbingersSkull.ID))) {
                tips.add(new PowerTip(RewardItem.TEXT[7], RewardItem.TEXT[8] + FontHelper.colorString(__instance.relicLink.relic.name + RewardItem.TEXT[9], "y")));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TipHelper.class, "queuePowerTips");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "addRelicToRewards", paramtypez = { AbstractRelic.class })
    public static class AddRelicToRewardsPatch {
        @SpirePostfixPatch
        public static void setRelicLink(AbstractRoom __instance, AbstractRelic relic) {
            if (relic.relicId.equals(HarbingersSkull.ID)) {
                RewardItem reward = __instance.rewards.get(__instance.rewards.size() - 1);
                reward.relicLink = __instance.rewards.get(__instance.rewards.size() - 2);
                reward.relicLink.relicLink = reward;
            }
        }
    }
}
