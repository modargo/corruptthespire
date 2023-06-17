package corruptthespire.ui;

import basemod.ModLabel;
import basemod.ModPanel;
import basemod.ModToggleButton;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import corruptthespire.Config;
import corruptthespire.cards.corrupted.powers.CorruptedForm;

public class CorruptTheSpireModPanel extends ModPanel {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:CorruptTheSpireModPanel");
    private static final String[] TEXT = uiStrings.TEXT;

    private static final float BX = 400.0F;
    private static final float LX = BX + 32.0F;

    public CorruptTheSpireModPanel() {
        float y1 = 700.0f;
        ModToggleButton toggleButton = new ModToggleButton(BX, this.adjustY(y1), Config.nerfCorruptedForm(), true, this, x -> Config.setNerfCorruptedForm(x.enabled));
        this.addUIElement(toggleButton);
        String text = TEXT[0].replace("{0}", CorruptedForm.PERCENT + "").replace("{1}", CorruptedForm.NERF_PERCENT + "");
        ModLabel label = new ModLabel(text, LX, y1, this, x -> {});
        this.addUIElement(label);
    }

    private float adjustY(float y) {
        return y - 6.0F;
    }
}
