package view.party;

import model.Model;
import sound.ClientSoundManager;
import sound.SoundEffects;
import view.*;
import view.sprites.AnimatedCharSprite;
import view.sprites.ArrowSprites;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public abstract class SelectableListMenu extends GameView {
    private GameView previous;
    private int selectedRow;
    private final int width;
    private final int height;
    private SelectableListMenu innerMenu;
    private int scrollShift = 0;
    private static Sprite downScroll = new AnimatedCharSprite((char)0xB2, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, 2);
    private static Sprite upScroll = new AnimatedCharSprite((char)0xB4, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, 2);


    public SelectableListMenu(GameView previous, int width, int height) {
        super(true);
        this.width = width;
        this.height = height;
        this.previous = previous;
        this.selectedRow = 0;
        innerMenu = null;
    }

    protected void setPrevious(GameView view) {
        this.previous = view;
    }

    public GameView getPrevious() {
        return previous;
    }

    protected int getYStart() {
        return (DrawingArea.WINDOW_ROWS - height) / 2;
    }

    protected int getXStart() {
        return (DrawingArea.WINDOW_COLUMNS - width) / 2;
    }

    protected int getWidth() { return width; }

    protected int getHeight() { return height; }

    public int getSelectedRow() { return selectedRow; }

    @Override
    public void transitionedTo(Model model) {
        SoundEffects.menuSelect();
        setTimeToTransition(false);
    }

    @Override
    protected void internalUpdate(Model model) {
        int xStart = getXStart();
        int yStart = getYStart();
        if (!(previous instanceof SelectableListMenu)) {
            previous.update(model);
        }
        clearPreviousForeground(model, xStart, yStart);
        BorderFrame.drawFrame(model.getScreenHandler(),
                xStart, yStart, width, height,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);

        drawContent(model, xStart, yStart, buildContent(model, xStart, yStart));
        drawDecorations(model, buildDecorations(model, xStart, yStart));
        drawNonScrollingParts(model, xStart, yStart);
        if (innerMenu != null) {
            innerMenu.internalUpdate(model);
        }
    }

    protected void clearPreviousForeground(Model model, int xStart, int yStart) {
        model.getScreenHandler().clearForeground(xStart, xStart + width, yStart-3, yStart + height);
    }

    protected void drawNonScrollingParts(Model model, int xStart, int yStart) {

    }


    private void drawContent(Model model, int xStart, int yStart, List<ListContent> content) {
        for (int index = 0; index < content.size(); ++index) {
            ListContent p = content.get(index);
            MyColors fg = p.getForegroundColor(model);
            MyColors bg = MyColors.BLUE;
            if (index == selectedRow) {
                bg = MyColors.LIGHT_YELLOW;
                fg = p.getSelectedForegroundColor(model);
            }
            if (isInScrollFrame(p.position.y)) {
                BorderFrame.drawString(model.getScreenHandler(), p.text, p.position.x, p.position.y - scrollShift, fg, bg);
                p.drawDecorations(model, p.position.x, p.position.y);
            }
        }
        if (content.size() >= height) {
            model.getScreenHandler().put(xStart+width-1, yStart+1, upScroll);
            model.getScreenHandler().put(xStart+width-1, yStart+height-1, downScroll);
        }
    }

    private void drawDecorations(Model model, List<DrawableObject> decorations) {
        for (DrawableObject decor : decorations) {
            if (isInScrollFrame(decor.position.y)) {
                decor.drawYourself(model, decor.position.x, decor.position.y+scrollShift);
            }
        }

    }

    private boolean isInScrollFrame(int y) {
        return getYStart() + scrollShift < y && y < getYStart() + scrollShift + height;
    }

    protected abstract List<DrawableObject> buildDecorations(Model model, int xStart, int yStart);

    protected abstract List<ListContent> buildContent(Model model, int xStart, int yStart);

    protected abstract void specificHandleEvent(KeyEvent keyEvent, Model model);

    @Override
    public GameView getNextView(Model model) {
        return previous;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (innerMenu != null) {
            innerMenu.handleKeyEvent(keyEvent, model);
            madeChanges();
            if (innerMenu.timeToTransition()) {
                innerMenu = null;
            }
            return;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE && escapeDisposesMenu()) {
            setTimeToTransition(true);
            SoundEffects.menuQuit();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            SoundEffects.menuDown();
            handleKeyDown(model);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            SoundEffects.menuUp();
            handleKeyUp(model);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            ListContent lc = buildContent(model, getXStart(), getYStart()).get(selectedRow);
            if (lc.isEnabled(model)) {
                lc.performAction(model, lc.position.x, lc.position.y);
                madeChanges();
            }
        }
        specificHandleEvent(keyEvent, model);
    }

    protected boolean escapeDisposesMenu() {
        return true;
    }

    private void handleKeyUp(Model model) {
        List<ListContent> content = buildContent(model, getXStart(), getYStart());
        int oldSelectedY =  content.get(selectedRow).position.y;
        selectedRow--;
        if (selectedRow < 0) {
            selectedRow = buildContent(model, getXStart(), getYStart()).size() - 1;
            scrollShift = 0;
            while (!isInScrollFrame(content.get(selectedRow).position.y)) {
                scrollShift++;
            }
        } else if (!isInScrollFrame(content.get(selectedRow).position.y)) {
            scrollShift -= oldSelectedY - content.get(selectedRow).position.y;
        }
    }

    private void handleKeyDown(Model model) {
        List<ListContent> content = buildContent(model, getXStart(), getYStart());
        int oldSelectedY =  content.get(selectedRow).position.y;
        selectedRow = (selectedRow + 1) % content.size();
        if (selectedRow == 0) {
            scrollShift = 0;
        } else if (!isInScrollFrame(content.get(selectedRow).position.y)) {
            scrollShift += content.get(selectedRow).position.y - oldSelectedY;
        }
    }

    protected void checkForSelectedRowReset(Model model) {
        List<ListContent> list = buildContent(model, getXStart(), getYStart());
        if (selectedRow >= list.size()) {
            selectedRow = list.size() - 1;
            scrollShift = 0;
        }
    }

    public void setInnerMenu(SelectableListMenu innerMenu, Model model) {
        this.innerMenu = innerMenu;
        innerMenu.transitionedTo(model);
    }

    protected static class ListContent {
        public Point position;
        public String text;
        public ListContent(int x, int y, String text) {
            this.position = new Point(x, y);
            this.text = text;
        }

        public void performAction(Model model, int x, int y) { }

        public MyColors getForegroundColor(Model model) {
            return MyColors.WHITE;
        }

        public boolean isEnabled(Model model) {
            return true;
        }

        public MyColors getSelectedForegroundColor(Model model) {
            return MyColors.BLACK;
        }

        public void setText(String text) { this.text = text; }

        public void drawDecorations(Model model, int x, int y) { }
    }

    protected static abstract class SelectableListContent extends ListContent {
        public SelectableListContent(int x, int y, String text) {
            super(x, y, text);
        }

        @Override
        public boolean isEnabled(Model model) {
            return !model.isInCombat();
        }

        @Override
        public abstract void performAction(Model model, int x, int y);

        public MyColors getForegroundColor(Model model) {
            if (!isEnabled(model)) {
                return MyColors.LIGHT_GRAY;
            }
            return MyColors.YELLOW;
        }

        public MyColors getSelectedForegroundColor(Model model) {
            if (!isEnabled(model)) {
                return MyColors.DARK_GRAY;
            }
            return MyColors.BLACK;
        }
    }

    protected abstract static class DrawableObject {
        public Point position;
        public DrawableObject(int x, int y) {
            this.position = new Point(x, y);
        }
        public abstract void drawYourself(Model model, int x, int y);
    }

    protected static class TextDecoration extends DrawableObject {

        private final String text;
        private final MyColors fg;
        private final MyColors bg;
        private final boolean centered;

        public TextDecoration(String text, int x, int y, MyColors fgColor, MyColors bgColor, boolean centered) {
            super(x, y);
            this.text = text;
            this.fg = fgColor;
            this.bg = bgColor;
            this.centered = centered;
        }

        @Override
        public void drawYourself(Model model, int x, int y) {
            if (centered) {
                BorderFrame.drawCentered(model.getScreenHandler(), text, y, fg, bg);
            } else {
                BorderFrame.drawString(model.getScreenHandler(), text, x, y, fg, bg);
            }
        }
    }

    protected abstract class CarouselListContent extends ListContent {
        private final int length;

        public CarouselListContent(int x, int y, String label) {
            super(x, y, label);
            this.length = label.length();
        }

        @Override
        public MyColors getForegroundColor(Model model) {
            return MyColors.YELLOW;
        }

        public abstract void turnLeft(Model model);

        public abstract void turnRight(Model model);

        @Override
        public void drawDecorations(Model model, int x, int y) {
            model.getScreenHandler().put(x-1, y, ArrowSprites.LEFT);
            model.getScreenHandler().put(x+length, y, ArrowSprites.RIGHT);
        }
    }

    protected static void print(ScreenHandler screenHandler, int x, int y, String text) {
        BorderFrame.drawString(screenHandler, text, x, y, MyColors.WHITE, MyColors.BLUE);
    }
}
