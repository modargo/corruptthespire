package corruptthespire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.vfx.FlameAnimationEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.map.CorruptMap;
import corruptthespire.util.TextureLoader;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ShowCorruptNodesOnMapPatch {
    private static final Logger logger = LogManager.getLogger(CorruptMap.class.getName());
    private static final Texture IMAGE = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:Corruption"));
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final float GLOW_CYCLE = 2.0F;
    private static final float ALPHA_RANGE = 0.20F;

    @SpirePatch(clz = MapRoomNode.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class RenderPatch {
        @SpireInsertPatch(locator = RenderPatch.Locator.class)
        public static void renderCorruptedVfx(MapRoomNode __instance, SpriteBatch sb, float ___flameVfxTimer, ArrayList<FlameAnimationEffect> ___fEffects, float ___scale) {
            if (CorruptedField.corrupted.get(__instance)) {
                int imgWidth = ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "IMG_WIDTH");
                float scale = ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "scale");
                float offsetX = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "OFFSET_X");
                float offsetY = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "OFFSET_Y");
                float spacingX = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "SPACING_X");

                sb.setColor(Color.WHITE);
                if (!Settings.isMobile) {
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale, scale * Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
                } else {
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale * 2.0F, scale * Settings.scale * 2.0F, 0.0F, 0, 0, 128, 128, false, false);
                }

                //exp10In interpolation with input range from 0.0 to 0.5 results in output range from 0.0 to 0.12
                //Future experiment can be to linearly map between those numbers instead
                //The value of 0.12F for the maximum additional scale here was reached by empirically examining the
                //interpolation that pulsing relics using, Interpolation.exp10In with an input range from 0.0 to 0.5,
                //and observing that it gives an output range of 0.0 to 0.12. This was then tweaked based on how things
                //looked with the various smoother interpolations (linear, sine).
                float maxAdditionalScale = 0.09F;
                float p = Math.abs((GLOW_CYCLE / 2.0F) - ___flameVfxTimer) / (GLOW_CYCLE / 2.0F);
                //float tmp = Interpolation.exp10In.apply(0.0F, 4.0F, p / 2.0F);
                //float tmp = p * maxAdditionalScale;
                float tmp = (1.0F - MathUtils.sin(___flameVfxTimer / GLOW_CYCLE * MathUtils.PI)) * maxAdditionalScale;
                //logger.info("tmp: " + tmp);
                sb.setBlendFunction(770, 1);
                float alpha = p * ALPHA_RANGE;
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, alpha));
                if (!Settings.isMobile) {
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale + tmp, scale * Settings.scale + tmp, 0.0F, 0, 0, 128, 128, false, false);
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale + tmp * 0.66F, scale * Settings.scale + tmp * 0.66F, 0.0F, 0, 0, 128, 128, false, false);
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale + tmp * 0.33F, scale * Settings.scale + tmp * 0.33F, 0.0F, 0, 0, 128, 128, false, false);
                } else {
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale * 2.0F + tmp, scale * Settings.scale * 2.0F + tmp, 0.0F, 0, 0, 128, 128, false, false);
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale * 2.0F + tmp * 0.66F, scale * Settings.scale * 2.0F + tmp * 0.66F, 0.0F, 0, 0, 128, 128, false, false);
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale * 2.0F + tmp * 0.33F, scale * Settings.scale * 2.0F + tmp * 0.33F, 0.0F, 0, 0, 128, 128, false, false);
                }
                sb.setBlendFunction(770, 771);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MapRoomNode.class, "renderEmeraldVfx");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = MapRoomNode.class, method = "update")
    public static class UpdatePatch {
        @SpireInsertPatch(locator = UpdatePatch.Locator.class)
        public static void updateCorruptedVfx(MapRoomNode __instance, @ByRef float[] ___flameVfxTimer, ArrayList<FlameAnimationEffect> ___fEffects, Hitbox ___hb) {
            //TODO: Make a separate field for the corrupted timer, because right now the node with the emerald key ends up flashing at a different rate
            if (CorruptedField.corrupted.get(__instance) && !__instance.hasEmeraldKey) {
                ___flameVfxTimer[0] -= Gdx.graphics.getDeltaTime();

                if ( ___flameVfxTimer[0] < 0.0F) {
                    ___flameVfxTimer[0] = GLOW_CYCLE;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MapRoomNode.class, "updateEmerald");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "renderBossIcon")
    public static class RenderBossIconPatch {
        @SpirePostfixPatch
        private static void renderCorruptedVfx(DungeonMap __instance, SpriteBatch sb) {
            if (DungeonMap.boss != null) {
                if (CorruptMap.isBossCorrupted()) {
                    float bossW = ReflectionHacks.getPrivateStatic(DungeonMap.class, "BOSS_W");
                    float bossH = (512.0F * 3/4) * Settings.scale;
                    float mapOffsetY = ReflectionHacks.getPrivateStatic(DungeonMap.class, "mapOffsetY");
                    float bossOffsetY = ReflectionHacks.getPrivateStatic(DungeonMap.class, "BOSS_OFFSET_Y");
                    Color baseMapColor = ReflectionHacks.getPrivate(__instance, DungeonMap.class, "baseMapColor");
                    //sb.draw(IMAGE, (float)Settings.WIDTH / 2.0F - bossW / 2.0F, DungeonMapScreen.offsetY + mapOffsetY + bossOffsetY, bossW, bossW);
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, baseMapColor.a));
                    sb.draw(IMAGE, (float)Settings.WIDTH / 2.0F + bossW  * 3.0F / 8.0F, DungeonMapScreen.offsetY + mapOffsetY + bossOffsetY + bossH, WIDTH, HEIGHT);
                }
            }
        }
    }
}
