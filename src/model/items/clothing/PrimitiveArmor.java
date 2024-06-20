package model.items.clothing;

import model.items.StartingItem;

public class PrimitiveArmor extends FurArmor implements StartingItem {
    @Override
    public String getName() {
        return "Primitive Armor";
    }

    @Override
    public int getAP() {
        return 2;
    }

    @Override
    public int getCost() {
        return 20;
    }
}
