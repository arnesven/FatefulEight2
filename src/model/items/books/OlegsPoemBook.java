package model.items.books;

import model.items.Item;
import view.MyColors;

public class OlegsPoemBook extends BookItem {
    private static final String TEXT = "Roses red.\nOgre big.\nYou smell nice.\nWant pig?";

    public OlegsPoemBook() {
        super("Oleg's Poem", 1, MyColors.BROWN, "Oleg's Poem", "Oleg", TEXT);
    }

    @Override
    public Item copy() {
        return new OlegsPoemBook();
    }
}
