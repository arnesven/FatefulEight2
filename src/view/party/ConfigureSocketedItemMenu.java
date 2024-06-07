package view.party;

import model.Model;
import model.items.Item;
import model.items.SocketedItem;
import model.items.accessories.*;
import util.MyLists;
import util.MyStrings;
import view.GameView;
import view.MyColors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ConfigureSocketedItemMenu extends SelectableListMenu {
    private final SocketedItem socketedItem;

    public ConfigureSocketedItemMenu(GameView innerView, SocketedItem socketedItem) {
        super(innerView, 36, 14);
        this.socketedItem = socketedItem;
    }

    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        String text = "Configure the " + socketedItem.getName() + " with items from your inventory.";
        String[] parts = MyStrings.partitionWithLineBreaks(text, getWidth() - 3);
        List<DrawableObject> result = new ArrayList<>();
        int row = 1;
        for (String part : parts) {
            result.add(new TextDecoration(part, xStart + 1, yStart + row++, MyColors.WHITE, MyColors.BLUE, true));
        }
        row++;
        result.add(new TextDecoration(socketedItem.getSocketLabels(),
                xStart + 1, yStart + row++, MyColors.WHITE, MyColors.BLUE, true));

        int xOffset = (4 - socketedItem.getNumberOfSockets()) * 4;
        int finalRow = row-1;
        result.add(new DrawableObject(xStart + 1, yStart+1) {
                       @Override
                       public void drawYourself(Model model, int x, int y) {
                           for (int col = 0; col < socketedItem.getNumberOfSockets(); ++col) {
                               Point finalPosition = new Point(xOffset + x + col * 8 + 1, y + finalRow);
                               if (socketedItem.getInnerItem(col) != null) {
                                   socketedItem.getInnerItem(col).drawYourself(model.getScreenHandler(), finalPosition.x, finalPosition.y);
                               } else {
                                   model.getScreenHandler().clearSpace(finalPosition.x, finalPosition.x + 4,
                                           finalPosition.y, finalPosition.y + 4);
                                   model.getScreenHandler().put(finalPosition.x, finalPosition.y, Item.EMPTY_ITEM_SPRITE);
                               }
                           }
                       }
                   }
        );

        return result;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        int xOffset = (4 - socketedItem.getNumberOfSockets()) * 4;
        List<ListContent> contents = new ArrayList<>();
        for (int col = 0; col < socketedItem.getNumberOfSockets(); ++col) {
            int finalCol = col;
            contents.add(new SelectableListContent(xOffset + xStart + 2 + finalCol * 8, yStart + 10, getLabel(socketedItem, finalCol)) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SetItemMenu(ConfigureSocketedItemMenu.this, socketedItem, finalCol) {
                        @Override
                        public List<Item> getAccessories() {
                            return socketedItem.getItemsForSlot(model, finalCol);
                        }
                    }, model);
                }
            });
        }

        contents.add(SelectableListMenu.makeOkButton(model, xStart + getWidth() / 2 - 1, yStart + getHeight() - 2, this));
        return contents;
    }

    private String getLabel(SocketedItem sash, int i) {
        Item innerItem = sash.getInnerItem(i);
        return innerItem == null
                ? "Not Set"
                : innerItem.getName().substring(0, Math.min(7, innerItem.getName().length()));
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            keyEvent.setKeyCode(KeyEvent.VK_UP);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            keyEvent.setKeyCode(KeyEvent.VK_DOWN);
        }
        super.handleKeyEvent(keyEvent, model);
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
    }

    @Override
    public void transitionedFrom(Model model) {
    }

    private static abstract class SetItemMenu extends SelectableListMenu {
        private final SocketedItem sash;
        private final int index;

        public SetItemMenu(GameView previous, SocketedItem sash, int index) {
            super(previous, 20, 10);
            this.sash = sash;
            this.index = index;
        }

        public abstract List<Item> getAccessories();

        @Override
        public void transitionedFrom(Model model) { }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return new ArrayList<>();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> result = new ArrayList<>();
            result.add(new SelectableListContent(xStart+1, ++yStart, "Unset") {
                @Override
                public void performAction(Model model, int x, int y) {
                    unset(model);
                    setTimeToTransition(true);
                }
            });
            for (Item it : getAccessories()) {
                result.add(new SelectableListContent(xStart+1, ++yStart, it.getName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        set(model, it);
                        setTimeToTransition(true);
                    }
                });
            }
            return result;
        }

        public void unset(Model model) {
            if (sash.getInnerItem(index) != null) {
                sash.getInnerItem(index).addYourself(model.getParty().getInventory());
                sash.setInnerItem(index, null);
            }
        }

        public void set(Model model, Item item) {
            if (sash.getInnerItem(index) != null) {
                unset(model);
            }
            model.getParty().getInventory().remove(item);
            sash.setInnerItem(index, item);
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
    }
}
