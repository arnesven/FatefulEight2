package model.headquarters;

import model.items.Item;
import model.items.books.BookItem;
import view.MyColors;

public class HeadquartersLogBook extends BookItem {
    public HeadquartersLogBook() {
        super("Log Book", 0, MyColors.DARK_RED, "Headquarters' Log", "", "");
    }

    @Override
    public Item copy() {
        return new HeadquartersLogBook();
    }

    public boolean isEmpty() {
        return getTextContent().equals("");
    }

    public void makeDayEntry(int day, String text) {
        String finalEntry = "DAY " + day + "\n" + text;
        if (isEmpty()) {
            setTextContent(finalEntry);
        } else {
            setTextContent(getTextContent() + "\n\n" + finalEntry);
        }
    }
}
