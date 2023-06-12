package view;

import model.Model;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class TwoPaneSelectableListMenu extends SelectableListMenu {
    private final int rightPaneWidth;
    private final int xOffset;
    private int index;

    public TwoPaneSelectableListMenu(GameView previous, int width, int height, int rightPaneWidth) {
        super(previous, width, height);
        this.rightPaneWidth = rightPaneWidth;
        this.xOffset = width - 1 - rightPaneWidth;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new TextDecoration(getHeading(), xStart + 1, yStart + 1, MyColors.WHITE, MyColors.BLUE, false),
                new DrawableObject(xStart + 1, yStart + 1) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        BorderFrame.drawFrame(model.getScreenHandler(), x + xOffset, y - 1, rightPaneWidth, getHeight(),
                                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, false);
                        drawContent(model, index, x+xOffset, y-1);
                    }
                },
                new DrawableObject(xStart + 1, yStart + index + 2) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        print(model.getScreenHandler(), x, y, (char)0x10+"");
                    }
                });
    }

    protected abstract void drawContent(Model model, int index, int x, int y);

    protected abstract String getHeading();

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        for (int i = 0; i < getNumberOfEntries(); ++i) {
            int finalI = i;
            content.add(new SelectableListContent(xStart+2, yStart+finalI+2, getEntryName(i)) {
                @Override
                public void performAction(Model model, int x, int y) {
                    index = finalI;
                    TwoPaneSelectableListMenu.this.madeChanges();
                }

                @Override
                public boolean isEnabled(Model model) {
                    return true;
                }
            });
        }
        return content;
    }

    protected abstract String getEntryName(int index);

    protected abstract int getNumberOfEntries();

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    @Override
    public void transitionedFrom(Model model) {

    }
}
