package view.widget;

import model.Model;
import model.items.Item;

import java.util.List;

public abstract class ItemTab {
    private final String name;

    public ItemTab(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<? extends Item> getItems(Model model);
}
