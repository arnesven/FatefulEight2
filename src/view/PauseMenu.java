package view;

import model.Model;

import java.awt.event.KeyEvent;

public class PauseMenu extends GameView {
    private static final int WIDTH = 10;
    private static final int X_START = DrawingArea.WINDOW_COLUMNS - WIDTH;
    private static final int HEIGHT = 2;
    private final GameView previous;

    public PauseMenu(GameView prev) {
        super(true);
        this.previous = prev;
    }

    @Override
    public void transitionedTo(Model model) {
        model.getScreenHandler().clearForeground(X_START, DrawingArea.WINDOW_COLUMNS-1, 0, HEIGHT);
        BorderFrame.drawFrame(model.getScreenHandler(), X_START, 0, WIDTH-1, HEIGHT,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        BorderFrame.drawString(model.getScreenHandler(), "PAUSED", X_START+2, 1, MyColors.WHITE, MyColors.BLUE);
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void internalUpdate(Model model) {

    }

    @Override
    public GameView getNextView(Model model) {
        return previous;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE || keyEvent.getKeyCode() == KeyEvent.VK_INSERT) {
            setTimeToTransition(true);
        }
    }
}
