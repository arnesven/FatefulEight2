package model.states.dailyaction;

import model.Model;
import model.items.Item;
import model.items.Lockpick;
import model.items.Prevalence;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrandEmporium extends GeneralShopNode {

    private static final Sprite SIGN = new SignSprite("emporiumsign", 0x26,
            MyColors.BLACK, MyColors.GOLD);

    public GrandEmporium(Model model, int x, int y) {
        super(model, x, y, "Grand Emporium");
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> result = new ArrayList<>();
        AccessoryShopNode asn = new AccessoryShopNode(model, 0, 0);
        result.addAll(asn.makeInventory(model));
        result.addAll(model.getItemDeck().draw(MyRandom.randInt(10, 20), Prevalence.unspecified));
        for (int i = MyRandom.randInt(0, 2); i > 0; --i) {
            result.add(new Lockpick());
        }
        Collections.sort(result);
        return result;
    }

    @Override
    protected int getShopSecurity() {
        return 10;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
    }
}
