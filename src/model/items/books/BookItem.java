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
    private final String author;
    private String title;
    private String textContent;

    public BookItem() {
        super("Test Book", 25);
        this.coverColor = MyColors.DARK_BLUE;
        this.sprite = makeBookSprite(coverColor);
        title = "How to Train Your Dragon";
        author = "Rastigan Veld";

        textContent =
                "Each combat round, characters take one combat action on their turn. Use the " +
                        "arrow keys to select targets during combat.\n\n" +
                        "Attack: The character uses their equipped weapon to attack the selected enemy. " +
                        "Only ranged weapons can attack from the back row.\n\n" +
                        "Item: Use an item from your inventory on the selected target.\n\n" +
                        "Spell: Cast a Combat Spell on the selected target.\n\n" +
                        "Flee: The party leader can announce a retreat from battle. If there is only " +
                        "one character in your party, this has a 60% chance of success. Otherwise the " +
                        "leader must succeed in a Leadership test where the difficulty is 3 + the number " +
                        "of party members.\n\n" +
                        "Delay: Postpone your action until later in the round.\n\n" +
                        "Ability: Use one of the character's combat abilities.\n\n" +
                        "Pass: Do nothing in combat.";
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

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTextContent() {
        return textContent;
    }
}
