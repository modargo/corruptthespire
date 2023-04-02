package corruptthespire.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.corrupted.powers.OtherworldlyPack;
import corruptthespire.effects.combat.OtherworldlyPackEffect;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class OtherworldlyPackPower extends AbstractPower {
    public static final String POWER_ID = "CorruptTheSpire:OtherworldlyPack";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static HashMap<String, String> cardParentMap;
    private static HashMap<String, String> packNameMap;

    private HashSet<String> usedPacks = new HashSet<>();
    private boolean triggered = false;

    public OtherworldlyPackPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.priority = 50;
        this.type = PowerType.BUFF;
        CorruptTheSpire.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], OtherworldlyPack.THRESHOLD, this.amount)
                + " NL NL " + (this.triggered ? DESCRIPTIONS[3] : MessageFormat.format(DESCRIPTIONS[1], this.usedPacks.size() > 0 ? this.usedPacks.stream().map(this::getPackName).collect(Collectors.joining(" NL ")) : DESCRIPTIONS[2]));
    }

    @Override
    public void atStartOfTurn() {
        this.usedPacks.clear();
        this.triggered = false;
        this.updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (this.triggered) {
            return;
        }
        String packID = getPackID(card);
        if (packID != null && !this.usedPacks.contains(packID)) {
            this.usedPacks.add(packID);
            if (this.usedPacks.size() >= OtherworldlyPack.THRESHOLD) {
                this.addToBot(new GainBlockAction(this.owner, this.amount));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped()) {
                        this.addToBot(new VFXAction(new OtherworldlyPackEffect(this.owner.hb.cX, this.owner.hb.cY, m.hb.cX, m.hb.cY), 0.1F));
                        this.addToBot(new ApplyPowerAction(m, this.owner, PowerUtil.abysstouched(m, this.amount)));
                    }
                }
                this.triggered = true;
            }
            this.updateDescription();
        }
    }

    @SuppressWarnings("unchecked")
    private String getPackID(AbstractCard card) {
        if (cardParentMap == null) {
            try {
                Class<?> clz = Class.forName("thePackmaster.SpireAnniversary5Mod");
                Field field = clz.getField("cardParentMap");
                cardParentMap = (HashMap<String, String>)field.get(null);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }

        return cardParentMap.getOrDefault(card.cardID, null);
    }

    @SuppressWarnings("unchecked")
    private String getPackName(String packID) {
        if (packNameMap == null) {
            try {
                Class<?> clz = Class.forName("thePackmaster.SpireAnniversary5Mod");
                Field field = clz.getField("packsByID");
                HashMap<String, Object> packsByID = (HashMap<String, Object>)field.get(null);
                packNameMap = new HashMap<>();
                Class<?> packClz = Class.forName("thePackmaster.packs.AbstractCardPack");
                Field nameField = packClz.getField("name");
                for (Map.Entry<String, Object> entry : packsByID.entrySet()) {
                    packNameMap.put(entry.getKey(), (String)nameField.get(entry.getValue()));
                }
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }

        return packNameMap.get(packID);
    }
}
