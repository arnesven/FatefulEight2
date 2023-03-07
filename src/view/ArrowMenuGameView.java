package view;

import model.Model;
import sound.SoundEffects;
import view.sprites.ArrowSprites;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public abstract class ArrowMenuGameView extends GameView {

    private static final Sprite FILLED_BLOCK = CharSprite.make((char)0xFF, MyColors.BLUE);
    private final int xStart;
    private final int yStart;
    private final int width;
    private final int height;
    private final List<String> labels;
    private int cursorPos;

    public ArrowMenuGameView(boolean doesPauseGame, int xStart, int yStart, int width, int height, List<String> labels) {
        super(doesPauseGame);
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
        this.labels = labels;
        this.cursorPos = 0;
    }

    protected abstract void enterPressed(Model model, int cursorPos);
    protected abstract boolean optionEnabled(Model model, int i);

    @Override
    public void transitionedTo(Model model) {
        model.getScreenHandler().clearForeground(xStart, xStart+width, yStart-2, yStart + height);
        BorderFrame.drawFrame(model.getScreenHandler(), xStart, yStart, width, height,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        setTimeToTransition(false);
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected void internalUpdate(Model model) {
        int row = yStart+2;
        for (int i = 0; i < labels.size(); ++i) {
            MyColors fg = MyColors.WHITE;
            if (!optionEnabled(model, i)) {
                fg = MyColors.GRAY;
            }
            BorderFrame.drawString(model.getScreenHandler(), labels.get(i), xStart+3, row, fg, MyColors.BLUE);
            row += 2;
        }

        model.getScreenHandler().fillSpace(xStart+1, xStart+2, yStart + 2, row, FILLED_BLOCK);
        Sprite arrow = ArrowSprites.RIGHT;
        arrow.setColor3(MyColors.BLUE);
        model.getScreenHandler().put(xStart+1, yStart + 2 + cursorPos*2, arrow);
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
            SoundEffects.menuQuit();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            cursorPos = cursorPos - 1;
            if (cursorPos == -1) {
                cursorPos = labels.size() - 1;
            }
            madeChanges();
            SoundEffects.menuUp();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            cursorPos = (cursorPos + 1) % labels.size();
            madeChanges();
            SoundEffects.menuDown();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed(model, cursorPos);
            SoundEffects.menuSelect();
        }
    }

}
