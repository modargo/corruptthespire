package corruptthespire.relics.corrupted;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class BottomlessMeadHorn extends AbstractCorruptedRelic {
    public static final String ID = "CorruptTheSpire:BottomlessMeadHorn";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));
    private static final int THRESHOLD_1 = 20;
    private static final int THRESHOLD_2 = 30;
    private static final int THRESHOLD_3 = 40;
    private static final int STATS = 1;
    private static final int EXHAUST = 1;
    private static final int DRAW = 1;

    public BottomlessMeadHorn() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStartPreDraw() {
        int deckSize = AbstractDungeon.player.masterDeck.size();
        if (deckSize >= THRESHOLD_2) {
            List<AbstractCard> strikes = new ArrayList<>();
            List<AbstractCard> unupgradedStrikes = new ArrayList<>();
            List<AbstractCard> defends = new ArrayList<>();
            List<AbstractCard> unupgradedDefends = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                    strikes.add(c);
                    if (!c.upgraded) {
                        unupgradedStrikes.add(c);
                    }
                }
                if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                    defends.add(c);
                    if (!c.upgraded) {
                        unupgradedDefends.add(c);
                    }
                }
            }

            if (unupgradedStrikes.size() > 0) {
                this.exhaustRandom(unupgradedStrikes);
            }
            else if (strikes.size() > 0) {
                this.exhaustRandom(strikes);
            }
            if (unupgradedDefends.size() > 0) {
                this.exhaustRandom(unupgradedDefends);
            }
            else if (defends.size() > 0) {
                this.exhaustRandom(defends);
            }
        }

        if (deckSize >= THRESHOLD_3) {
            AbstractDungeon.player.gameHandSize += DRAW;
        }
    }

    private void exhaustRandom(List<AbstractCard> cards) {
        AbstractCard c = cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
        this.addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.player.masterDeck.size() >= THRESHOLD_1) {
            this.flash();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STATS)));
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, STATS)));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], THRESHOLD_1, STATS, THRESHOLD_2, EXHAUST, THRESHOLD_3, DRAW);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BottomlessMeadHorn();
    }
}
