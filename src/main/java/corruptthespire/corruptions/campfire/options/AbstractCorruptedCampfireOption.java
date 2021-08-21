package corruptthespire.corruptions.campfire.options;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import corruptthespire.Cor;

public abstract class AbstractCorruptedCampfireOption extends AbstractCampfireOption {
    public AbstractCorruptedCampfireOption() {
        this.usable = this.getFragmentCost() <= Cor.getFragmentCount();
        this.label = this.getLabel();
        this.description = this.getDescription();
        this.img = this.getImage();
    }

    public abstract int getFragmentCost();
    public abstract String getLabel();
    public abstract String getDescription();
    public abstract Texture getImage();
}
