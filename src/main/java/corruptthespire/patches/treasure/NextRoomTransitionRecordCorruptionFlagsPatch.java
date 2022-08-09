package corruptthespire.patches.treasure;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import corruptthespire.Cor;
import corruptthespire.corruptions.shop.ShopCorruptionType;
import corruptthespire.events.special.TreasureWardensEventRoom;
import corruptthespire.patches.core.CorruptedField;
import corruptthespire.patches.shop.ShopCorruptionTypeField;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
public class NextRoomTransitionRecordCorruptionFlagsPatch {
    @SpirePrefixPatch
    public static void recordCorruptionFlags(AbstractDungeon __instance, SaveFile saveFile) {
        boolean isLoadingPostCombatSave = CardCrawlGame.loadingSave && saveFile != null && saveFile.post_combat;
        if (!isLoadingPostCombatSave && AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null) {
            if (AbstractDungeon.getCurrRoom() instanceof TreasureWardensEventRoom) {
                //This is here so that it has to happen, but needs to happen after the player is entirely done with the room
                //If we set this when the room was entered or when combat was finished, saving/reloading afterwards would
                //result in a different outcome, because the flag will already be in the save file and Treasure Wardens
                //will be filtered out as an option
                Cor.flags.seenTreasureWardens = true;
            }
            else {
                //This is here because there wasn't a natural place to put it in the shop corruption logic and this is
                //a surefire way to do it
                ShopCorruptionType corruptionType = CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode()) && AbstractDungeon.getCurrRoom() instanceof ShopRoom
                        ? ShopCorruptionTypeField.corruptionType.get(AbstractDungeon.getCurrRoom())
                        : null;
                if (corruptionType == ShopCorruptionType.Service) {
                    Cor.flags.seenServiceShop = true;
                }
            }
        }
    }
}
