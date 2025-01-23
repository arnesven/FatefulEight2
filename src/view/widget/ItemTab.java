package view.widget;

import model.Model;
import model.items.Item;
import model.items.potions.Potion;
import model.items.special.PearlItem;
import util.MyLists;

import java.util.*;

public abstract class ItemTab {
    private final String name;
    private Map<Item, Integer> cachedMap;

    public ItemTab(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<? extends Item> getItemsFromSource(Model model);

    public int getStackCount(Item it) {
        return cachedMap.get(it);
    }

    public List<? extends Item> getItems(Model model) {
        if (cachedMap == null) {
            cachedMap = makeItemMap(model);
        }
        List<? extends Item> result = new ArrayList<>(cachedMap.keySet());
        result.sort(Comparator.comparing(Item::getName));
        return result;
    }

    private Map<Item, Integer> makeItemMap(Model model) {
        Map<Item, Integer> result = new HashMap<>();
        for (Item it : getItemsFromSource(model)) {
            if (isStackable(it)) {
                Item other = MyLists.find(new ArrayList<>(result.keySet()), it2 -> it2.getName().equals(it.getName()));
                if (other != null) {
                    result.put(other, result.get(other) + 1);
                } else {
                    result.put(it, 1);
                }
            } else {
                result.put(it, 1);
            }
        }
        return result;
    }

    private boolean isStackable(Item it) {
        return it instanceof Potion || it instanceof PearlItem; // TODO: Move to item classes.
    }

    public void invalidate() {
        cachedMap = null;
    }
}
