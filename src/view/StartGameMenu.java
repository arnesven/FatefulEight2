package view;

import model.Model;
import sound.SoundEffects;
import view.sprites.ArrowSprites;

import java.awt.event.KeyEvent;

public class StartGameMenu extends GameView {
    private static final int X_START = 6;
    private static final int Y_START = 6;

    private int cursorPos = 0;
    private static String[] options = new String[]{
            "New Game", "Load Game", "Hall of Fame",
            "Credits", "Sprite Previewer", "Jukebox",
            "Minigames", "Quit"};

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

        int controlsRow = 43;
        int xOffset = 64;
        BorderFrame.drawString(model.getScreenHandler(), "Controls", xOffset + 2, controlsRow++,
                MyColors.WHITE, MyColors.BLACK);
        controlsRow++;
        char[] arrows = new char[]{0xB1, 0xB0, 0xB2, 0xB4};
        BorderFrame.drawString(model.getScreenHandler(),
                "" + arrows[3], xOffset + 2, controlsRow++,
                MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(),
                " " + arrows[0] + "" + arrows[2] + "" + arrows[1] + " + Enter", xOffset, controlsRow++,
                MyColors.WHITE, MyColors.BLACK);
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
            } else if (cursorPos == 4) {
                model.showSpritePreviewer();
            } else if (cursorPos == 5) {
                showJukebox(model);
            } else if (cursorPos == 6) {
                model.transitionToDialog(new SelectMinigameDialog(model.getView()));
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

    private void showJukebox(Model model) {
        model.transitionToDialog(new JukeboxDialog(model.getView()));
    }
}
