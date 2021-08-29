package corruptthespire.powers;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.CorruptTheSpire;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PowerUtil {
    private static Constructor<?> abysstouchedConstructor = null;

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
}
