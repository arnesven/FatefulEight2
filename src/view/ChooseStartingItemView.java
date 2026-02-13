package view;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Item;
import model.items.special.TentUpgradeItem;
import model.items.weapons.Dagger;
import model.items.weapons.Dirk;
import util.Arithmetics;
import util.MyStrings;
import view.party.CharacterCreationView;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
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
    private final SteppingMatrix<Item> matrix;
    private boolean canceled = false;
    private final List<Item> selectedItems = new ArrayList<>();
    private boolean inItemMatrix = true;


    public ChooseStartingItemView(Model model, GameCharacter gc) {
        super(model.getView(), 8 + ITEMS_PER_ROW * 5, calculateHeight(gc));
        this.items = getStartingItems(gc);
        this.matrix = new SteppingMatrix<>(ITEMS_PER_ROW,
                (int)Math.ceil(((double)items.size()) / ITEMS_PER_ROW));
        matrix.addElements(items);
        matrix.setSelectedPoint(new Point(0, 0));
        setSelectedRow(-1);
    }

    private static int calculateHeight(GameCharacter gc) {
        return ((getStartingItems(gc).size() - 1) / ITEMS_PER_ROW) * 5 + 26;
    }

    private static List<Item> getStartingItems(GameCharacter gc) {
        Map<String, Item> itemMap = new HashMap<>();
        for (Item it : gc.getCharClass().getStartingItems()) {
            itemMap.put(it.getName(), it);
        } // TODO: Add some more default starting items?
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
                        "Select " + MyStrings.numberWord(NO_OF_ITEMS_TO_PICK) + " of the following", y + 3,
                        MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(),
                        "as your starting items.", y + 4,
                        MyColors.WHITE, MyColors.BLUE);

                int row = y + ITEMS_Y_OFFSET - 5;

                for (int matY = 0; matY < matrix.getRows(); ++matY) {
                    row += 5;
                    for (int matX = 0; matX < matrix.getColumns(); ++matX) {
                        Item currentItem = matrix.getElementAt(matX, matY);
                        if (currentItem != null) {
                            Point position = new Point(xStart + matX*6 + 3, row);
                            currentItem.drawYourself(model.getScreenHandler(), position.x, position.y);
                            if (selectedItems.contains(currentItem)) {
                                model.getScreenHandler().put(position.x+3, position.y+3, CharacterCreationView.CHECK_SPRITE);
                            }
                        }
                    }
                }

                if (inItemMatrix) {
                    Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
                    Point cursorPos = matrix.getSelectedPoint();
                    model.getScreenHandler().register(cursor.getName(),
                            new Point(xStart + cursorPos.x * 6 + 3, y + ITEMS_Y_OFFSET - 3 + cursorPos.y * 5), cursor);
                }

                Item it = matrix.getSelectedElement();
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

    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (inItemMatrix) {
            if (matrix.getSelectedPoint().y == matrix.getRows()-1 && keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                inItemMatrix = false;
                super.setSelectedRow(0);
            } else if (matrix.getSelectedPoint().y == matrix.getMinimumRow() && keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                inItemMatrix = false;
                super.setSelectedRow(1);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                Item currentItem = matrix.getSelectedElement();
                if (selectedItems.contains(currentItem)) {
                    selectedItems.remove(currentItem);
                } else {
                    selectedItems.add(currentItem);
                    if (selectedItems.size() > NO_OF_ITEMS_TO_PICK) {
                        selectedItems.remove(0);
                    }
                }
            } else {
                matrix.handleKeyEvent(keyEvent);
            }
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP && getSelectedRow() == 0) {
            inItemMatrix = true;
            super.setSelectedRow(-1);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN && getSelectedRow() == 1) {
            super.setSelectedRow(-1);
            inItemMatrix = true;
            matrix.setSelectedPoint(new Point(0, 0));
            madeChanges();
        } else {
            super.handleKeyEvent(keyEvent, model);
        }
    }

    public boolean didCancel() {
        return canceled;
    }
}
