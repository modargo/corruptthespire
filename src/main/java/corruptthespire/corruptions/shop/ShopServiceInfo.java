package corruptthespire.corruptions.shop;

import com.badlogic.gdx.graphics.Texture;

public class ShopServiceInfo {
    public final ShopServiceType type;
    public final int cost;
    public final Texture img;
    public float x;

    public ShopServiceInfo(ShopServiceType type, int cost, Texture img) {
        this.type = type;
        this.cost = cost;
        this.img = img;
    }
}
