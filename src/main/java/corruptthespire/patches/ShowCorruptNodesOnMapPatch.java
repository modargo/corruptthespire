package corruptthespire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import corruptthespire.CorruptTheSpire;
import corruptthespire.map.CorruptMap;
import corruptthespire.util.TextureLoader;
import javassist.CtBehavior;

public class ShowCorruptNodesOnMapPatch {
    private static final Texture IMAGE = TextureLoader.getTexture(CorruptTheSpire.uiImage("CorruptTheSpire:Corruption"));
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;

    @SpirePatch(clz = MapRoomNode.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class RenderPatch {
        @SpireInsertPatch(locator = RenderPatch.Locator.class)
        public static void RenderCorruptedVfx(MapRoomNode __instance, SpriteBatch sb) {
            if (CorruptedField.corrupted.get(__instance)) {
                int imgWidth = ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "IMG_WIDTH");
                float scale = ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "scale");
                float offsetX = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "OFFSET_X");
                float offsetY = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "OFFSET_Y");
                float spacingX = ReflectionHacks.getPrivateStatic(MapRoomNode.class, "SPACING_X");
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                if (!Settings.isMobile) {
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale, scale * Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
                } else {
                    sb.draw(IMAGE, (float)__instance.x * spacingX + offsetX - 64.0F + __instance.offsetX + imgWidth * scale, (float)__instance.y * Settings.MAP_DST_Y + offsetY + DungeonMapScreen.offsetY - 64.0F + __instance.offsetY, 64.0F, 64.0F, 128.0F, 128.0F, scale * Settings.scale * 2.0F, scale * Settings.scale * 2.0F, 0.0F, 0, 0, 128, 128, false, false);
                }
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
        public static void UpdateCorruptedVfx(MapRoomNode __instance) {
            if (CorruptedField.corrupted.get(__instance)) {
                // TODO: IF I want an animation for corrupted nodes, will need to update it here
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
        private static void RenderCorruptedVfx(DungeonMap __instance, SpriteBatch sb) {
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
