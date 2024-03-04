package view;

import model.Model;
import sound.SoundEffects;
import view.party.SelectableListMenu;
import view.sprites.ArrowSprites;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.awt.event.KeyEvent;
import java.util.List;

public abstract class ArrowMenuGameView extends GameView {

    private static final Sprite FILLED_BLOCK = CharSprite.make((char)0xFF, MyColors.BLUE);
    public static final int MAX_LABELS = 16;
    private final int xStart;
    private final int yStart;
    private final int width;
    private final int height;
    private final List<String> labels;
    private int cursorPos;
    private boolean quitSoundEnabled = true;
    private int scrollshift = 0;

    public ArrowMenuGameView(boolean doesPauseGame, int xStart, int yStart, int width, int height, List<String> labels) {
        super(doesPauseGame);
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
        this.labels = labels;
        this.cursorPos = 0;
        SoundEffects.arrowMenu();
    }

    protected abstract void enterPressed(Model model, int cursorPos);
    protected abstract boolean optionEnabled(Model model, int i);

    @Override
    public void transitionedTo(Model model) {
        model.getScreenHandler().clearForeground(xStart, xStart+width, yStart, yStart + height);
        BorderFrame.drawFrame(model.getScreenHandler(), xStart, yStart, width, height,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        setTimeToTransition(false);
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected void internalUpdate(Model model) {
        int row = yStart+2;
        for (int i = scrollshift; i < labels.size() && i < MAX_LABELS + scrollshift; ++i) {
            MyColors fg = MyColors.WHITE;
            if (!optionEnabled(model, i)) {
                fg = MyColors.GRAY;
            }
            BorderFrame.drawString(model.getScreenHandler(), labels.get(i), xStart+3, row, fg, MyColors.BLUE);
            row += 2;
        }

        model.getScreenHandler().fillSpace(xStart+1, xStart+2, yStart + 2, row-1, FILLED_BLOCK);
        Sprite arrow = ArrowSprites.RIGHT;
        arrow.setColor3(MyColors.BLUE);
        model.getScreenHandler().put(xStart+1, yStart + 2 + cursorPos*2, arrow);
        if (labels.size() > height/2 - 1) {
            model.getScreenHandler().put(xStart + width - 1, yStart + 1, SelectableListMenu.upScroll);
            model.getScreenHandler().put(xStart + width - 1, yStart + height - 1, SelectableListMenu.downScroll);
        }
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        int maxToUse = Math.min(labels.size(), MAX_LABELS);

        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
            if (quitSoundEnabled) {
                SoundEffects.menuQuit();
            }
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            if (cursorPos == 0 && scrollshift > 0) {
                scrollshift--;
            } else {
                cursorPos = cursorPos - 1;
                if (cursorPos == -1) {
                    cursorPos = maxToUse - 1;
                    scrollshift = Math.max(labels.size() - MAX_LABELS, 0);
                }
            }
            madeChanges();
            SoundEffects.menuUp();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            if (cursorPos == MAX_LABELS-1 && scrollshift < labels.size() - MAX_LABELS) {
                scrollshift++;
            } else {
                if (cursorPos == maxToUse - 1) {
                    scrollshift = 0;
                }
                cursorPos = (cursorPos + 1) % maxToUse;
            }
            madeChanges();
            SoundEffects.menuDown();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed(model, cursorPos + scrollshift);
            SoundEffects.menuSelect();
        }
    }

    public void setQuitSoundEnabled(boolean b) {
        this.quitSoundEnabled = b;
    }
}
