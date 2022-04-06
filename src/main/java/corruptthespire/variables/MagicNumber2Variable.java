package corruptthespire.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import corruptthespire.cards.AbstractModCard;

public class MagicNumber2Variable extends DynamicVariable {
    @Override
    public String key() {
        return "corruptthespire:M2";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractModCard)card).isMagicNumber2Modified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractModCard)card).magicNumber2;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractModCard)card).baseMagicNumber2;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractModCard)card).upgradedMagicNumber2;
    }
}