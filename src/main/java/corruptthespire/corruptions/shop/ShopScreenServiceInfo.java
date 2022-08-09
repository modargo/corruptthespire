package corruptthespire.corruptions.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopScreenServiceInfo {
    public ShopServiceType hoveredService = null;
    public ShopServiceType touchedService = null;
    public ShopServiceType currentService = null;
    public final List<ShopServiceType> usedServices = new ArrayList<>();
    public final Map<ShopServiceType, Float> serviceScales = new HashMap<>();
    public float serviceMultiplier = 1.0F;
}
