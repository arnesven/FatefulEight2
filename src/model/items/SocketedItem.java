package model.items;

import model.Model;

import java.util.List;

public interface SocketedItem {
    int getNumberOfSockets();

    Item getInnerItem(int index);

    void setInnerItem(int index, Item it);

    String getName();

    String getSocketLabels();

    List<Item> getItemsForSlot(Model model, int index);
}
