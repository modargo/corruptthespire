package corruptthespire.relics.chaotic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.CorruptTheSpire;
import corruptthespire.powers.PowerUtil;
import corruptthespire.util.TextureLoader;

import java.text.MessageFormat;

public class TranscendentPet extends CustomRelic {
    public static final String ID = "CorruptTheSpire:TranscendentPet";
    private static final Texture IMG = TextureLoader.getTexture(CorruptTheSpire.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(CorruptTheSpire.relicOutlineImage(ID));

    private static final int VULNERABLE = 1;
    private static final int ABYSSTOUCHED = 5;

    public TranscendentPet() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter == 1) {
            this.flash();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, VULNERABLE, false)));
                this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, PowerUtil.abysstouched(m, ABYSSTOUCHED)));
            }
            this.counter = -1;
            this.grayscale = true;
        }
        else if (!this.grayscale) {
            this.counter++;
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], VULNERABLE, ABYSSTOUCHED);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TranscendentPet();
    }
}
