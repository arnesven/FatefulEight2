package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Item;
import model.items.weapons.Dagger;
import util.Arithmetics;
import util.MyStrings;
import view.party.CharacterCreationView;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseStartingItemView extends SelectableListMenu {
    private static final int ITEMS_Y_OFFSET = 6;
    private static final int VIEW_HEIGHT = 30;
    private static final int ITEMS_PER_ROW = 5;
    private static final int NO_OF_ITEMS_TO_PICK = 2;
    private final List<Item> items;
    private int selectedIndex = 0;
    private boolean canceled = false;
    private final List<Item> selectedItems = new ArrayList<>();


    public ChooseStartingItemView(Model model, GameCharacter gc) {
        super(model.getView(), 11 + ITEMS_PER_ROW * 5, calculateHeight(gc));
        this.items = getStartingItems(gc);
    }

    private static int calculateHeight(GameCharacter gc) {
        return ((getStartingItems(gc).size() - 1) / ITEMS_PER_ROW) * 5 + 26;
    }

    private static List<Item> getStartingItems(GameCharacter gc) {
        Map<String, Item> itemMap = new HashMap<>();
        for (int i = 0; i < gc.getClasses().length; ++i) {
            CharacterClass cls = gc.getClasses()[i];
            if (cls != Classes.None) {
                for (Item it : cls.getStartingItems()) {
                    itemMap.put(it.getName(), it);
                }
            }
        }
        return new ArrayList<>(itemMap.values());
    }

    public List<Item> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                BorderFrame.drawCentered(model.getScreenHandler(), "STARTING GEAR", y + 1,
                        MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(),
                        "Use Space to select " + MyStrings.numberWord(NO_OF_ITEMS_TO_PICK) + " of the", y + 3,
                        MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(),
                        "following as your starting items.", y + 4,
                        MyColors.WHITE, MyColors.BLUE);

                int row = y + ITEMS_Y_OFFSET - 5;
                for (int i = 0; i < items.size(); ++i) {
                    int itemX = i % ITEMS_PER_ROW;
                    if (i % ITEMS_PER_ROW == 0) {
                        row += 5;
                    }

                    Point position = new Point(xStart + itemX*6 + 5, row);

                    items.get(i).drawYourself(model.getScreenHandler(), position.x, position.y);
                    if (selectedItems.contains(items.get(i))) {
                        model.getScreenHandler().put(position.x+3, position.y+3, CharacterCreationView.CHECK_SPRITE);
                    }
                }
                Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
                int cursorX = selectedIndex % ITEMS_PER_ROW;
                int cursorY = selectedIndex / ITEMS_PER_ROW;
                model.getScreenHandler().register(cursor.getName(),
                        new Point(xStart + cursorX*6 + 5, y + ITEMS_Y_OFFSET - 3 + cursorY * 5), cursor);

                Item it = items.get(selectedIndex);
                String text = it.getName() + ", " + it.getShoppingDetails() +
                        ", Value: " + it.getCost();
                String[] parts = text.split(", ");
                row += 6;
                for (String s : parts) {
                    String[] innerParts = MyStrings.partition(s, 30);
                    for (String s2 : innerParts) {
                        BorderFrame.drawCentered(model.getScreenHandler(), s2, row++, MyColors.WHITE, MyColors.BLUE);
                    }
                }
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
                new SelectableListContent(xStart + getWidth()/2 - 1, yStart + getHeight()-3, "OK") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        ChooseStartingItemView.this.setTimeToTransition(true);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return selectedItems.size() == NO_OF_ITEMS_TO_PICK;
                    }
                },
                new SelectableListContent(xStart + getWidth()/2 - 3, yStart + getHeight()-2, "CANCEL") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        ChooseStartingItemView.this.canceled = true;
                        setTimeToTransition(true);
                    }
                });
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            selectedIndex = Arithmetics.incrementWithWrap(selectedIndex, items.size());
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            selectedIndex = Arithmetics.decrementWithWrap(selectedIndex, items.size());
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            Item currentItem = items.get(selectedIndex);
            if (selectedItems.contains(currentItem)) {
                selectedItems.remove(currentItem);
            } else {
                selectedItems.add(currentItem);
            }
            madeChanges();
        }
    }

    public boolean didCancel() {
        return canceled;
    }
}
