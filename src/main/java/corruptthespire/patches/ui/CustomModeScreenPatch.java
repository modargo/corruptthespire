package corruptthespire.patches.ui;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.screens.custom.CustomModeScreen.PositionCharacterButtons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import corruptthespire.Config;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class CustomModeScreenPatch {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:CharacterSelectScreen").TEXT;
    private static final float CHECKBOX_Y = 400.0F;
    private static final String EXTRA_SPACE = "60.0F";

    private static float getCheckboxY(CustomModeScreen screen) {
        int rows = screen.options.size() / (int)ReflectionHacks.getPrivateStatic(PositionCharacterButtons.class, "MAX_BUTTONS_PER_ROW");
        return CHECKBOX_Y - rows * 100;
    }

    @SpirePatch(clz = CustomModeScreen.class, method = SpirePatch.CLASS)
    public static class HitboxField {
        public static final SpireField<Hitbox> hitbox = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "open")
    public static class InitializeHitboxPatch {
        @SpirePostfixPatch
        public static void initializeHitbox(CustomModeScreen __instance, float ___scrollY) {
            Hitbox hitbox = new Hitbox(80.0F * Settings.scale, 80.0F * Settings.scale);
            hitbox.move(CustomModeScreen.screenX + 130.0F * Settings.xScale, ___scrollY + getCheckboxY(__instance) * Settings.scale);
            CustomModeScreenPatch.HitboxField.hitbox.set(__instance, hitbox);
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "renderAscension")
    public static class RenderCheckboxPatch {
        @SpirePostfixPatch
        public static void renderCheckbox(CustomModeScreen __instance, SpriteBatch sb, float ___imageScale, float ___scrollY) {
            Hitbox hb = HitboxField.hitbox.get(__instance);
            sb.setColor(Color.WHITE);
            if (hb.hovered) {
                sb.draw(ImageMaster.CHECKBOX, hb.cX - 32.0F, hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, ___imageScale * 1.2F, ___imageScale * 1.2F, 0.0F, 0, 0, 64, 64, false, false);
                sb.setColor(Color.GOLD);
                sb.setBlendFunction(770, 1);
                sb.draw(ImageMaster.CHECKBOX, hb.cX - 32.0F, hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, ___imageScale * 1.2F, ___imageScale * 1.2F, 0.0F, 0, 0, 64, 64, false, false);
                sb.setBlendFunction(770, 771);
            } else {
                sb.draw(ImageMaster.CHECKBOX, hb.cX - 32.0F, hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, ___imageScale, ___imageScale, 0.0F, 0, 0, 64, 64, false, false);
            }

            FontHelper.renderFontCentered(sb, FontHelper.charDescFont, TEXT[0], CustomModeScreen.screenX + 310.0F * Settings.scale, ___scrollY + getCheckboxY(__instance) * Settings.scale, hb.hovered ? Settings.PURPLE_COLOR : Color.PURPLE);

            if (Config.active()) {
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.TICK, hb.cX - 32.0F, hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, ___imageScale, ___imageScale, 0.0F, 0, 0, 64, 64, false, false);
            }

            hb.render(sb);
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "updateAscension")
    public static class UpdateCheckboxPatch {
        @SpirePostfixPatch
        private static void updateCheckbox(CustomModeScreen __instance, float ___scrollY) {
            Hitbox hb = HitboxField.hitbox.get(__instance);
            hb.moveY(___scrollY + CHECKBOX_Y * Settings.scale); // Doesn't need to use getCheckboxY because we're inside a block that's already affected by BaseMod's FixEverythingPosition patch
            hb.update();
            if (hb.justHovered) {
                ReflectionHacks.privateMethod(CustomModeScreen.class, "playHoverSound").invoke(__instance);
            }

            if (hb.hovered && InputHelper.justClickedLeft) {
                ReflectionHacks.privateMethod(CustomModeScreen.class, "playClickStartSound").invoke(__instance);
                hb.clickStarted = true;
            }

            if (hb.clicked || CInputActionSet.topPanel.isJustPressed()) {
                CInputActionSet.topPanel.unpress();
                ReflectionHacks.privateMethod(CustomModeScreen.class, "playClickFinishSound").invoke(__instance);
                hb.clicked = false;
                Config.setActive(!Config.active());
            }
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "renderScreen")
    public static class RenderScreenPatch {
        public static class UpdateSeedPatchExprEditor extends ExprEditor {
            private int callCount = 0;

            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(CustomModeScreen.class.getName()) && methodCall.getMethodName().equals("renderHeader")) {
                    if (callCount > 1) {
                        methodCall.replace("{ $0.renderHeader($1, $2, $3 - " + EXTRA_SPACE + "); }");
                    }
                    callCount++;
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor updateSeedPatch() {
            return new UpdateSeedPatchExprEditor();
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "updateSeed")
    public static class UpdateSeedPatch {
        public static class UpdateSeedPatchExprEditor extends ExprEditor {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(Hitbox.class.getName()) && methodCall.getMethodName().equals("move")) {
                    methodCall.replace("{ $0.move($1, $2 - " + EXTRA_SPACE + "); }");
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor updateSeedPatch() {
            return new UpdateSeedPatchExprEditor();
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "updateMods")
    public static class UpdateModsPatch {
        public static class UpdateSeedPatchExprEditor extends ExprEditor {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(CustomMod.class.getName()) && methodCall.getMethodName().equals("update")) {
                    methodCall.replace("{ $0.update($1 - " + EXTRA_SPACE + "); }");
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor updateModsPatch() {
            return new UpdateSeedPatchExprEditor();
        }
    }
}
