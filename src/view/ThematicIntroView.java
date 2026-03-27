package view;

import model.Model;
import util.Arithmetics;
import view.sprites.Animation;
import view.sprites.AnimationManager;

import java.awt.event.KeyEvent;

public class ThematicIntroView extends GameView implements Animation {

    private static final int PRINT_DELAY = 3;
    private String[] text = new String[]{
            "Deep in the forest, the adventurers finally found the lost temple.",
            "But it turned out not to be nearly as easy to plunder as the ",
            "mysterious man who had sold them the treasure map had claimed.",
            "",
            "\"Gold and glory, huh?\" grumbled Bungo Darkwood, the group's",
            "professional thief.",
            "\"Well, a few aggressive lizardmen are to be expected when you're",
            "hunting treasure,\" replied the leader of the group, the paladin",
            "Denise Boyd.",
            "",
            "The marksman Rolf Fryt slowly drew an arrow from his quiver.",
            "\"I prefer lizardmen over frogmen. At least they understand our",
            "language. Maybe we won't have to resort to violence this time.\"",
            "",
            "But his words were drowned out by a series of booming thunderclaps",
            "as the wizard Melethain Gauth unleashed his signature spell:",
            "Chain Lightning. Denise gave Melethain a weary look, only to be",
            "met with a childish grin beneath his bushy mustache.",
            "",
            "Rolf let the arrow slip back into the quiver and said, ",
            "\"Well. Diplomacy lasted all of three seconds this time. A new",
            "record.\"",
            "",
            "The temple's guardians had been easily defeated, and the adventurers",
            "were free to continue deeper into the temple - but inside awaited",
            "dangers they could scarcely have imagined, even in their worst",
            "nightmares."};

    private int currentRow = 0;
    private int charCount = 0;
    private int stepCount = 1;
    private boolean printAll = false;
    private boolean swapBack;
    private double fadeLevel = 0.0;
    private boolean timeOut = false;

    public ThematicIntroView() {
        super(true);
        AnimationManager.register(this);
    }

    @Override
    public void transitionedTo(Model model) {
    }

    @Override
    public void transitionedFrom(Model model) {
        AnimationManager.unregister(this);
        model.getScreenHandler().clearAll();
    }

    @Override
    protected void internalUpdate(Model model) {
        if (fadeLevel > 0.1) {
            return;
        }
        model.getScreenHandler().clearAll();
        int row = Math.max(6, 20 - currentRow);
        int col = 4;
        MyColors textColor = LogView.DEFAULT_TEXT_COLOR;
        swapBack = false;
        for (int i = 0; i <= currentRow; i++) {
            String substr;
            if (i < currentRow || printAll) {
                substr = text[i];
            } else {
                substr = text[i].substring(0, charCount);
            }
            for (int j = 0; j < substr.length(); ++j) {
                char charToPrint = substr.charAt(j);
                if (charToPrint == '\"') {
                    if (textColor == LogView.DEFAULT_TEXT_COLOR) {
                        textColor = MyColors.YELLOW;
                    } else {
                        swapBack = true;
                    }
                }
                BorderFrame.drawString(model.getScreenHandler(), charToPrint + "", col+j, row + i, textColor);
                if (swapBack) {
                    swapBack = false;
                    textColor = LogView.DEFAULT_TEXT_COLOR;
                }
            }
        }
    }

    @Override
    public GameView getNextView(Model model) {
        if (timeOut) {
            return new IntroGameView();
        }
        return new StartGameMenu();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        setTimeToTransition(true);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        stepCount = Arithmetics.incrementWithWrap(stepCount, PRINT_DELAY);
        if (stepCount == 0) {
            if (currentRow < text.length) {
                charCount++;
            }
            if (charCount >= text[currentRow].length()) {
                currentRow++;
                if (currentRow == text.length) {
                    currentRow--;
                    printAll = true;
                    model.getScreenHandler().setFade(fadeLevel, MyColors.BLACK);
                    fadeLevel += 0.1;
                    if (fadeLevel > 1.0) {
                        this.timeOut = true;
                        setTimeToTransition(true);
                    }
                }
                charCount = 0;
            }
            madeChanges();
        }
    }

    @Override
    public void synch() {

    }
}
