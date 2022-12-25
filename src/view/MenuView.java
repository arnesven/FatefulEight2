package view;

import jdk.jshell.spi.ExecutionControl;
import model.Model;
import util.MyPair;
import view.party.PartyView;
import view.sprites.ArrowSprites;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.awt.event.KeyEvent;
import java.util.List;

public class MenuView extends GameView {

    public static final int MENU_WIDTH = 15;
    public static final int MENU_HEIGHT = 14;
    public static final int Y_START = 14;
    public static final int X_START = (DrawingArea.WINDOW_COLUMNS - MENU_WIDTH) / 2;

    private GameView nextView;
    private final GameView previousView;
    private int cursorPos = 0;

    private List<MyPair<String, GameView>> options;
    private static final Sprite FILLED_BLOCK = CharSprite.make((char)0xFF, MyColors.BLUE);

    public MenuView(GameView previous) {
        super(true);
        this.previousView = previous;
        this.nextView = previous;
        options = List.of(
                new MyPair<>("Party", new PartyView(previous)),
                new MyPair<>("Map", new FullMapView(previous)),
                new MyPair<>("Inventory", new InventoryView(previous)),
                new MyPair<>("Skills", new NotImplementedView(previous)),
                new MyPair<>("Spells", new NotImplementedView(previous)),
                new MyPair<>("Exit Game", null));
    }

    @Override
    public void transitionedTo(Model model) {
        model.getScreenHandler().clearForeground(X_START, X_START+MENU_WIDTH, Y_START-2, Y_START + MENU_HEIGHT);
        BorderFrame.drawMenuFrame(model.getScreenHandler());
        setTimeToTransition(false);
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void internalUpdate(Model model) {
        int row = Y_START+2;
        for (MyPair<String, GameView> p : options) {
            MyColors fg = MyColors.WHITE;
            if (p.second != null && !p.second.isValid(model)) {
                fg = MyColors.GRAY;
            }
            BorderFrame.drawString(model.getScreenHandler(), p.first, X_START+3, row, fg, MyColors.BLUE);
            row += 2;
        }

        model.getScreenHandler().fillSpace(X_START+1, X_START+2, Y_START + 2, row, FILLED_BLOCK);
        Sprite arrow = ArrowSprites.RIGHT;
        arrow.setColor3(MyColors.BLUE);
        model.getScreenHandler().put(X_START+1, Y_START + 2 + cursorPos*2, arrow);
    }

    @Override
    public GameView getNextView(Model model) {
        return nextView;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            cursorPos = cursorPos - 1;
            if (cursorPos == -1) {
                cursorPos = options.size() - 1;
            }
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            cursorPos = (cursorPos + 1) % options.size();
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            GameView nextView = options.get(cursorPos).second;
            if (nextView != null) {
                if (nextView.isValid(model)){
                    this.nextView = nextView;
                    setTimeToTransition(true);
                }
            } else {
                model.setExitGame(true);
            }
        }
    }
}
