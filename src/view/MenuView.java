package view;

import model.Model;
import view.party.PartyView;

import java.util.List;

public class MenuView extends ArrowMenuGameView {

    private static List<String> labels = List.of("Party", "Map", "Inventory", "Skills", "Spells", "Exit Game");
    public static final int MENU_WIDTH = 15;
    public static final int MENU_HEIGHT = 14;
    public static final int Y_START = 14;
    public static final int X_START = (DrawingArea.WINDOW_COLUMNS - MENU_WIDTH) / 2;
    private GameView nextView;
    private List<GameView> options;

    public MenuView(GameView previous) {
        super(true, X_START, Y_START, MENU_WIDTH, MENU_HEIGHT, labels);
        this.nextView = previous;
        options = List.of(new PartyView(previous), new FullMapView(previous), new InventoryView(previous),
                new SkillsView(previous), new SpellsView(previous), new DummyView());
    }

    @Override
    protected void enterPressed(Model model, int cursorPos) {
        if (options.get(cursorPos) instanceof DummyView) {
            model.transitionToDialog(new YesNoMessageView(this, "Are you sure you want to quit the game? Any unsaved progress will be lost.") {
                @Override
                protected void doAction(Model model) {
                    model.setExitGame(true);
                }
            });
            madeChanges();
        } else {
            GameView nextView = options.get(cursorPos);
            if (nextView.isValid(model)){
                this.nextView = nextView;
                setTimeToTransition(true);
            }
        }
    }

    @Override
    protected boolean optionEnabled(Model model, int i) {
        return options.get(i).isValid(model);
    }

    @Override
    public GameView getNextView(Model model) {
        return nextView;
    }

}
