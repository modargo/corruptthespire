package corruptthespire.cards;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;

import java.text.MessageFormat;

public class Shadowblast extends AbstractCorruptedCard {
    public static final String ID = "CorruptTheSpire:Shadowblast";
    public static final String IMG = CorruptTheSpire.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int AMOUNT = 1;
    private static final int UPGRADE_AMOUNT = 1;
    private static final int CORRUPTION_THRESHOLD = 8;
    //TODO: Get corruption to be a keyword and an image
    //Check out other mods that do this -- I know that AllisonMoon's elemental types does, what else? Runesmith? Champ?

    public Shadowblast() {
        super(ID, NAME, IMG, COST, MessageFormat.format(DESCRIPTION, DAMAGE, CORRUPTION_THRESHOLD), CardType.ATTACK, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeMagicNumber(UPGRADE_AMOUNT);
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        this.addToBot(new VFXAction(p, new CleaveEffect(), 0.1F));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void applyPowers() {
        this.baseDamage = DAMAGE + (this.magicNumber * (Cor.corruption / CORRUPTION_THRESHOLD));

        super.applyPowers();

        this.rawDescription = MessageFormat.format(DESCRIPTION, DAMAGE, CORRUPTION_THRESHOLD) + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
}
