package view;

import model.CorruptSaveFileException;
import model.Model;
import sound.SoundEffects;
import view.sprites.ArrowSprites;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

public class StartGameMenu extends GameView {
    private static final int X_START = 6;
    private static final int Y_START = 6;

    private int cursorPos = 0;
    private static String[] options = new String[]{"New Game", "Load Game", "Hall of Fame", "Credits", "Quit"};

    public StartGameMenu() {
        super(true);
    }


    @Override
    public void transitionedTo(Model model) {
        update(model);
    }

    @Override
    public void transitionedFrom(Model model) {
        model.getScreenHandler().clearAll();
    }

    @Override
    public void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        int row = Y_START;
        for (String s : options) {
            BorderFrame.drawString(model.getScreenHandler(), s, X_START, row, MyColors.WHITE);
            row += 2;
        }

        model.getScreenHandler().clearSpace(X_START-2, X_START-1, Y_START, row);
        model.getScreenHandler().put(X_START - 2, Y_START + cursorPos*2, ArrowSprites.RIGHT_BLACK);
    }

    @Override
    public GameView getNextView(Model model) {
        return new StoryIntroView();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cursorPos == 0) {
                model.startGameNoLoad();
                setTimeToTransition(true);
            } else if (cursorPos == 1) {
                model.transitionToDialog(new SelectSaveSlotMenu(this, true));
            } else if (cursorPos == 2) {
                model.showHallOfFame();
            } else if (cursorPos == 3) {
                model.showCredits();
            } else {
                model.setGameStarted(true);
                model.setExitGame(true);
            }
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            SoundEffects.matrixSelect();
            cursorPos = cursorPos - 1;
            if (cursorPos == -1) {
                cursorPos = options.length - 1;
            }
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            SoundEffects.matrixSelect();
            cursorPos = (cursorPos + 1) % options.length;
            madeChanges();
        }
    }
}
