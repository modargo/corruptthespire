package corruptthespire.powers;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import corruptthespire.CorruptTheSpire;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PowerUtil {
    public static final String AbysstouchedPowerId = "Abyss:Abysstouched";
    private static Constructor<?> abysstouchedConstructor = null;
    private static Constructor<?> handOfTheAbyssConstructor = null;

    public static AbstractPower abysstouched(AbstractCreature owner, int amount) {
        if (Loader.isModLoaded(CorruptTheSpire.AbyssModId)) {
            try {
                if (abysstouchedConstructor == null) {
                    abysstouchedConstructor = Class.forName("abyss.powers.AbysstouchedPower").getConstructor(AbstractCreature.class, int.class);
                }
                return (AbstractPower)abysstouchedConstructor.newInstance(owner, amount);
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create AbysstouchedPower");
            }
        }
        return new AbysstouchedPower(owner, amount);
    }

    public static AbstractPower handOfTheAbyss(AbstractCreature owner, int amount) {
        if (Loader.isModLoaded(CorruptTheSpire.AbyssModId)) {
            try {
                if (handOfTheAbyssConstructor == null) {
                    handOfTheAbyssConstructor = Class.forName("abyss.powers.HandOfTheAbyssPower").getConstructor(AbstractCreature.class, int.class);
                }
                return (AbstractPower)handOfTheAbyssConstructor.newInstance(owner, amount);
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create HandOfTheAbyssPower");
            }
        }
        return new HandOfTheAbyssPower(owner, amount);
    }

    public static AbstractPower gainStrengthBuff(AbstractCreature owner, int amount) {
        AbstractPower gainStrengthPower = new GainStrengthPower(owner, amount);
        gainStrengthPower.type = AbstractPower.PowerType.BUFF;
        return gainStrengthPower;
    }
}
