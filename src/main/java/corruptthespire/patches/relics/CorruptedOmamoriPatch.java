package corruptthespire.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.relics.corrupted.CorruptedOmamori;

public class CorruptedOmamoriPatch {
    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
    public static class ShowCardAndObtainEffectPatch {
        @SpirePrefixPatch
        public static void checkCorruptedOmamori(ShowCardAndObtainEffect __instance, AbstractCard card, float x, float y, boolean convergeCards) {
            AbstractRelic corruptedOmamori = AbstractDungeon.player.getRelic(CorruptedOmamori.ID);
            if (corruptedOmamori != null && ((CorruptedOmamori)corruptedOmamori).tryTrigger(card)) {
                __instance.duration = 0.0F;
                __instance.isDone = true;
                ReflectionHacks.setPrivate(__instance, ShowCardAndObtainEffect.class, "converge", convergeCards);
            }
        }
    }

    @SpirePatch(clz = FastCardObtainEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    public static class FastCardObtainEffectPatch {
        @SpirePrefixPatch
        public static void checkCorruptedOmamori(FastCardObtainEffect __instance, AbstractCard card, float x, float y) {
            AbstractRelic corruptedOmamori = AbstractDungeon.player.getRelic(CorruptedOmamori.ID);
            if (corruptedOmamori != null && ((CorruptedOmamori)corruptedOmamori).tryTrigger(card)) {
                __instance.duration = 0.0F;
                __instance.isDone = true;
            }
        }
    }
}
