package model.states.dailyaction.shops;

import model.Model;
import model.items.Item;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicSuperShop extends GeneralShopNode {

    private static final Sprite SIGN = new SignSprite("supermagicsign", 0x27,
            MyColors.BLACK, MyColors.GOLD);

    public MagicSuperShop(Model model, int x, int y) {
        super(model, x, y, "Magic Super Shop");
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> result = new ArrayList<>();
        MagicShopNode msn = new MagicShopNode(model, 0, 0);
        result.addAll(msn.makeInventory(model));
        result.addAll(msn.makeInventory(model));
        result.add(model.getItemDeck().getRandomItem());
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
