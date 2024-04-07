package model.items.books;

import model.Model;
import model.characters.GameCharacter;
import model.items.Prevalence;
import model.items.ReadableItem;
import model.items.Item;
import view.GameView;
import view.InventoryView;
import view.MyColors;
import view.ReadBookView;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.HashMap;
import java.util.Map;

public abstract class BookItem extends ReadableItem {

    private final MyColors coverColor;
    private final Sprite sprite;
    private final String author;
    private final String title;
    private final String textContent;

    public BookItem(String itemName, int cost, MyColors coverColor, String title, String author, String content) {
        super(itemName, cost);
        this.coverColor = coverColor;
        this.sprite = makeBookSprite(coverColor);
        this.title = title;
        this.author = author;
        this.textContent = content;
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

    protected static Sprite makeBookSprite(MyColors coverColor) {
        MyColors bookMarkColor = MyColors.GOLD;
        if (coverColor == MyColors.GOLD) {
            bookMarkColor = MyColors.DARK_RED;
        }
        return new ItemSprite(7, 13, MyColors.DARK_GRAY, coverColor, bookMarkColor);
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

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTextContent() {
        return textContent;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    public Map<String, Sprite> getFigures() {
        return new HashMap<>();
    }
}
