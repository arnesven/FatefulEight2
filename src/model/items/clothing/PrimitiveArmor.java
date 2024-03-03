package model.items.clothing;

public class PrimitiveArmor extends FurArmor {
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
