package view.party;

import model.Model;
import view.GameView;

public abstract class FixedPositionSelectableListMenu extends SelectableListMenu {
    private final int x;
    private final int y;

    public FixedPositionSelectableListMenu(GameView partyView, int width, int height, int x, int y) {
        super(partyView, width, height);
        this.x = x;
        this.y = y;
    }

    @Override
    protected int getXStart() {
        return x + 5;
    }

    @Override
    protected int getYStart() {
        return y;
    }

    @Override
    public void transitionedFrom(Model model) {
    }
}
