package corruptthespire.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import corruptthespire.cards.AbstractModCard;

public class MagicNumber3Variable extends DynamicVariable {
    @Override
    public String key() {
        return "corruptthespire:M3";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractModCard)card).isMagicNumber3Modified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractModCard)card).magicNumber3;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractModCard)card).baseMagicNumber3;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractModCard)card).upgradedMagicNumber3;
    }
}