package model.items.books;

import model.Model;
import model.characters.GameCharacter;
import model.items.ReadableItem;
import model.items.Item;
import view.GameView;
import view.InventoryView;
import view.MyColors;
import view.ReadBookView;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BookItem extends ReadableItem {

    private final MyColors coverColor;
    private final Sprite sprite;

    public BookItem() {
        super("Test Book", 25);
        this.coverColor = MyColors.DARK_BLUE;
        this.sprite = makeBookSprite(coverColor);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public String getShoppingDetails() {
        return "A book about ...";
    }

    @Override
    public Item copy() {
        return new BookItem();
    }

    protected static Sprite makeBookSprite(MyColors coverColor) {
        return new ItemSprite(7, 13, MyColors.GRAY, coverColor, MyColors.GOLD);
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        return gc.getFirstName() + " reads the book.";
    }

    @Override
    public boolean removeAfterUse() {
        return false;
    }

    @Override
    public boolean opensViewFromInventoryMenu() {
        return true;
    }

    @Override
    public GameView getViewFromInventoryMenu(Model model, InventoryView inventoryView, Item itemToEquip) {
        return new ReadBookView(model, inventoryView, (BookItem)itemToEquip);
    }

    public MyColors getCoverColor() {
        return coverColor;
    }
}
