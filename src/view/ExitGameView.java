package view;

import model.Model;

public class ExitGameView extends YesNoMessageView {
    public ExitGameView(GameView menuView) {
        super(menuView,
                "Are you sure you want to quit the game? Any unsaved progress will be lost.");
    }

    protected void doAction(Model model) {
        model.setExitGame(true);
    }
}
