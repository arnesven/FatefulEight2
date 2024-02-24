package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.weapons.Dagger;
import util.Arithmetics;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ChooseStartingItemView extends SelectableListMenu {
    private static final int ITEMS_Y_OFFSET = 6;
    private static final int VIEW_HEIGHT = 24;
    private final List<Item> items;
    private int selectedIndex = 0;
    private boolean canceled = false;

    public ChooseStartingItemView(Model model, GameCharacter gc) {
        super(model.getView(), calculateWidth(gc), VIEW_HEIGHT);
        this.items = new ArrayList<>(gc.getCharClass().getStartingItems());
    }

    private static int calculateWidth(GameCharacter gc) {
        return 10 + 7 * gc.getCharClass().getStartingItems().size();
    }

    public Item getSelectedItem() {
        return items.get(selectedIndex);
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
                        "Select one of the following", y + 3,
                        MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(),
                        "as your starting item.", y + 4,
                        MyColors.WHITE, MyColors.BLUE);

                for (int i = 0; i < items.size(); ++i) {
                    items.get(i).drawYourself(model.getScreenHandler(), xStart + i*7 + 6, y + ITEMS_Y_OFFSET);
                }
                Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
                model.getScreenHandler().register(cursor.getName(),
                        new Point(xStart + selectedIndex*7 + 6, y + ITEMS_Y_OFFSET - 3), cursor);

                Item it = items.get(selectedIndex);
                String text = it.getName() + ", " + it.getShoppingDetails() +
                        ", Value: " + it.getCost();
                String[] parts = text.split(", ");
                int row = y + it.getSpriteSize() + ITEMS_Y_OFFSET + 1;
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
        return List.of(makeOkButton(model, xStart + getWidth()/2 - 1, yStart + getHeight()-3, this),
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
        }
    }

    public boolean didCancel() {
        return canceled;
    }
}
