package model.items;

import view.sprites.Sprite;

public class LockpicksDummyItem extends InventoryDummyItem {
    private final int amount;

    public LockpicksDummyItem(int lockpicks) {
        super("Lockpicks (" + lockpicks + ")", 4);
        this.amount = lockpicks;
    }

    @Override
    protected Sprite getSprite() {
        return Lockpick.SPRITE;
    }

    @Override
    public int getWeight() {
        return amount * Inventory.WEIGHT_OF_LOCKPICKS;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addToLockpicks(amount);
    }

    @Override
    public String getShoppingDetails() {
        return Lockpick.DETAIL_STRING + ", Chance of breaking: " + Lockpick.BREAK_CHANCE_FAILURE +
                "% on failure, " + Lockpick.BREAK_CHANCE_SUCCESS + "% during succeess.";
    }

    @Override
    public Item copy() {
        return new LockpicksDummyItem(amount);
    }
}
