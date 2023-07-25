package view;

import model.Model;
import sound.SoundEffects;
import view.sprites.Animation;
import view.sprites.AnimationManager;

import java.awt.event.KeyEvent;

public class CreditsView extends GameView implements Animation {
    private double fadeLevel = 1.0;
    private long totalTime = 0;
    private static final MyColors[] textColors = new MyColors[]{MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_RED,
            MyColors.ORANGE, MyColors.LIGHT_YELLOW, MyColors.WHITE};
    private int colorStep = 0;
    private String[][] creditParts = new String[][]{
            new String[]{
                "Game Design and Programming",
                "Erik Nilsson",
            },
            new String[]{
                "Music",
                "Per Soderberg",
            },
            new String[]{
            "Play Testers",
            "Nathalie Bjallerhag",
            "Peter Komaromy"}
    };

    public CreditsView() {
        super(false);
        AnimationManager.register(this);
    }

    @Override
    public void transitionedTo(Model model) { }

    @Override
    public void transitionedFrom(Model model) {
        AnimationManager.unregister(this);
    }

    @Override
    protected void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        BorderFrame.drawCentered(model.getScreenHandler(), "- Credits -", 8, MyColors.WHITE);
        if (fadeLevel <= 0.001) {
            int y = 10;
            for (int i = 0; i < creditParts.length; ++i) {
                y += 4;
                for (int j = 0; j < creditParts[i].length; ++j) {
                    BorderFrame.drawCentered(model.getScreenHandler(), creditParts[i][j], y, getColor(j*2 + i * 16));
                    if (j == 0) {
                        y += 4;
                    } else {
                        y += 2;
                    }
                }
            }
        }
        model.getScreenHandler().setFade(fadeLevel, MyColors.BLACK);
    }

    private MyColors getColor(int i) {
        return textColors[Math.min(Math.max(colorStep-i, 0), textColors.length-1)];
    }

    @Override
    public GameView getNextView(Model model) {
        return new StartGameMenu();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        setTimeToTransition(true);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        totalTime += elapsedTimeMs;
        if (totalTime > 500) {
            double newFade = fadeLevel - (double) elapsedTimeMs / 3000.0;
            fadeLevel = Math.max(0.0, newFade);
            madeChanges();
        }

        long newTime = totalTime - 4000;
        colorStep = (int)Math.max(0, newTime / 100);

    }

    @Override
    public void synch() { }
}
