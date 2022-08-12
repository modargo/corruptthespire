package corruptthespire.savables.logs;

import basemod.abstracts.CustomSavable;

import java.util.ArrayList;
import java.util.List;

public class ShopServiceLog implements CustomSavable<List<ShopServiceLog>> {
    public static final String SaveKey = "ServiceShopLog";

    public static List<ShopServiceLog> shopServiceLog;

    public int floor;
    public List<String> cardsCorrupted = new ArrayList<>();
    public List<String> cardsDuplicated = new ArrayList<>();
    public List<String> cardsUpgraded = new ArrayList<>();
    public List<String> cardsTransformed = new ArrayList<>();
    public List<String> cardsGained = new ArrayList<>();

    @Override
    public List<ShopServiceLog> onSave() {
        return ShopServiceLog.shopServiceLog;
    }

    @Override
    public void onLoad(List<ShopServiceLog> shopServiceLog) {
        ShopServiceLog.shopServiceLog = shopServiceLog;
    }
}
