package model.items;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class SellPotionDummyItem extends InventoryDummyItem {
    private final Item inner;
    private int count;
    private Sprite sprite;
    private Sprite overSprite;

    public SellPotionDummyItem(Item item, int count) {
        super(item.getName(), item.getCost());
        this.inner = item;
        this.count = count;
        this.sprite = inner.getSprite();
        makeSprite();
    }

    private void makeSprite() {
        char x = (char)('0' + count);
        if (count > 9) {
            x = '*';
        }
        overSprite = CharSprite.make(x, MyColors.LIGHT_GRAY);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return inner.getWeight();
    }

    @Override
    public void addYourself(Inventory inventory) {

    }

    @Override
    public String getShoppingDetails() {
        return " (" + count + ")" + inner.getShoppingDetails();
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("Should not be copied!");
    }

    public Item getInnerItem() {
        return inner;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        super.drawYourself(screenHandler, col, row);
        screenHandler.put(col + 3, row + 3, overSprite);
    }

    public void decrement() {
        count--;
        makeSprite();
    }
}
