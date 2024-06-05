package view.party;

import model.Model;
import model.items.Item;
import model.items.accessories.*;
import model.items.special.FashionableSash;
import util.MyLists;
import util.MyStrings;
import view.GameView;
import view.MyColors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ConfigureFashionableSashMenu extends SelectableListMenu {
    private final FashionableSash sash;

    public ConfigureFashionableSashMenu(GameView innerView, FashionableSash sash) {
        super(innerView, 36, 14);
        this.sash = sash;
    }

    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        String text = "Configure the Fashionable Sash with items from your inventory.";
        String[] parts = MyStrings.partitionWithLineBreaks(text, getWidth() - 3);
        List<DrawableObject> result = new ArrayList<>();
        int row = 1;
        for (String part : parts) {
            result.add(new TextDecoration(part, xStart + 1, yStart + row++, MyColors.WHITE, MyColors.BLUE, true));
        }
        row++;
        result.add(new TextDecoration("Feet    Head    Gloves  Jewelry", xStart + 1, yStart + row++, MyColors.WHITE, MyColors.BLUE, true));

        int finalRow = row-1;
        result.add(new DrawableObject(xStart + 1, yStart+1) {
                       @Override
                       public void drawYourself(Model model, int x, int y) {
                           for (int col = 0; col < 4; ++col) {
                               Point finalPosition = new Point(x + col * 8 + 1, y + finalRow);
                               if (sash.getInnerItem(col) != null) {
                                   sash.getInnerItem(col).drawYourself(model.getScreenHandler(), finalPosition.x, finalPosition.y);
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
        List<ListContent> contents = new ArrayList<>();
        for (int col = 0; col < 4; ++col) {
            int finalCol = col;
            contents.add(new SelectableListContent(xStart + 2 + finalCol * 8, yStart + 10, getLabel(sash, finalCol)) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SetItemMenu(ConfigureFashionableSashMenu.this, sash, finalCol) {
                        @Override
                        public List<Accessory> getAccessories() {
                            return getItemsForSlot(model.getParty().getInventory().getAccessories(), finalCol);
                        }
                    }, model);
                }
            });
        }

        contents.add(SelectableListMenu.makeOkButton(model, xStart + getWidth() / 2 - 1, yStart + getHeight() - 2, this));
        return contents;
    }

    private List<Accessory> getItemsForSlot(List<Accessory> accessories, int slotIndex) {
        switch (slotIndex) {
            case 0:
                return MyLists.filter(accessories, (Accessory acc) -> acc instanceof ShoesItem);
            case 1:
                return MyLists.filter(accessories, (Accessory acc) -> acc instanceof HeadGearItem);
            case 2:
                return MyLists.filter(accessories, (Accessory acc) -> acc instanceof GlovesItem);
            default:
                return MyLists.filter(accessories, (Accessory acc) -> acc instanceof JewelryItem);
        }
    }

    private String getLabel(FashionableSash sash, int i) {
        Accessory innerItem = sash.getInnerItem(i);
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
        private final FashionableSash sash;
        private final int index;

        public SetItemMenu(GameView previous, FashionableSash sash, int index) {
            super(previous, 20, 10);
            this.sash = sash;
            this.index = index;
        }

        public abstract List<Accessory> getAccessories();

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
            for (Accessory accessory : getAccessories()) {
                result.add(new SelectableListContent(xStart+1, ++yStart, accessory.getName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        set(model, accessory);
                        setTimeToTransition(true);
                    }
                });
            }
            return result;
        }

        public void unset(Model model) {
            sash.getInnerItem(index).addYourself(model.getParty().getInventory());
            sash.setInnerItem(index, null);
        }

        public void set(Model model, Accessory accessory) {
            if (sash.getInnerItem(index) != null) {
                unset(model);
            }
            model.getParty().getInventory().remove(accessory);
            sash.setInnerItem(index, accessory);
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
    }
}
