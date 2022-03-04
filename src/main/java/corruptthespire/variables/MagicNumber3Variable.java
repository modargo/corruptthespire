package corruptthespire.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import corruptthespire.cards.corrupted.AbstractCorruptedCard;

public class MagicNumber3Variable extends DynamicVariable {
    @Override
    public String key() {
        return "corruptthespire:M3";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractCorruptedCard)card).isMagicNumber3Modified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractCorruptedCard)card).magicNumber3;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractCorruptedCard)card).baseMagicNumber3;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractCorruptedCard)card).upgradedMagicNumber3;
    }
}