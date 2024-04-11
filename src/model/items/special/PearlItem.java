package model.items.special;

import model.items.Inventory;
import model.items.Item;
import model.items.WeightlessItem;
import view.MyColors;
import view.sprites.ItemSprite;

public abstract class PearlItem extends WeightlessItem {

    public PearlItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addSpecialItem(this);
    }

    @Override
    public String getShoppingDetails() {
        return "";
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    @Override
    public String getSound() {
        return "wood-small";
    }

    public static PearlItem makeFromColor(MyColors color) {
        if (color == MyColors.DARK_RED) {
            return new CrimsonPearl();
        }
        if (color == MyColors.PINK) {
            return new PinkPearl();
        }
        if (color == MyColors.BLUE) {
            return new BluePearl();
        }
        if (color == MyColors.GREEN) {
            return new GreenPearl();
        }
        if (color == MyColors.BLACK) {
            return new BlackPearl();
        }
        if (color == MyColors.ORANGE) {
            return new OrangePearl();
        }
        if (color == MyColors.PURPLE) {
            return new PurplePearl();
        }
        throw new IllegalArgumentException("Cannot make pearl item for that color " + color.name());
    }

    protected static class PearlSprite extends ItemSprite {
        public PearlSprite(MyColors color, MyColors shade, MyColors highLight) {
            super(15, 4);
            setColor2(color);
            setColor3(shade);
            setColor4(highLight);
        }
    }
}
