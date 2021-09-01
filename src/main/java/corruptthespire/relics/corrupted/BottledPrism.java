package corruptthespire.relics.corrupted;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.CorruptTheSpire;
import corruptthespire.patches.relics.BottledPrismPatch;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.function.Predicate;


public class BottledPrism extends AbstractCorruptedRelic implements CustomBottleRelic, CustomSavable<Integer> {
    public static final String ID = "CorruptTheSpire:BottledPrism";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private boolean cardSelected = true;
    public AbstractCard card = null;

    public BottledPrism() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return BottledPrismPatch.InBottledPrismField.inBottlePrism::get;
    }

    @Override
    public Integer onSave() {
        if (this.card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(this.card);
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            this.card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (this.card != null) {
                BottledPrismPatch.InBottledPrismField.inBottlePrism.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }

    @Override
    public void onEquip() {
        CardGroup differentColorCards = AbstractDungeon.player.masterDeck.getPurgeableCards();
        differentColorCards.group.removeIf(c -> c.color == AbstractDungeon.player.getCardColor() || c.type == AbstractCard.CardType.CURSE);
        if (differentColorCards.size() > 0) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(differentColorCards, 1, String.format(DESCRIPTIONS[1], this.name), false, false, false, false);
        }
    }

    @Override
    public void onUnequip() {
        if (this.card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            if (cardInDeck != null) {
                BottledPrismPatch.InBottledPrismField.inBottlePrism.set(card, false);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            BottledPrismPatch.InBottledPrismField.inBottlePrism.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.description = MessageFormat.format(this.DESCRIPTIONS[2], FontHelper.colorString(this.card.name, "y"));
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }

    }

    public void setDescriptionAfterLoading() {
        this.description = MessageFormat.format(this.DESCRIPTIONS[2], FontHelper.colorString(this.card.name, "y"));
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public boolean canSpawn() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.color != AbstractDungeon.player.getCardColor() && c.type != AbstractCard.CardType.CURSE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractCard getCard() {
        return this.card.makeCopy();
    }
}