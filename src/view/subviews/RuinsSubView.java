package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.ruins.objects.DungeonObject;
import model.states.ExploreRuinsState;
import view.BorderFrame;
import view.MyColors;

import java.awt.event.KeyEvent;

public class RuinsSubView extends AvatarSubView {
    private final ExploreRuinsState state;
    private final SteppingMatrix<DungeonObject> matrix;
    private final String dungeonType;

    public RuinsSubView(ExploreRuinsState exploreRuinsState, SteppingMatrix<DungeonObject> matrix, String dungeonType) {
        this.state = exploreRuinsState;
        this.matrix = matrix;
        this.dungeonType = dungeonType;
    }

    @Override
    protected void specificDrawArea(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), state.getCurrentRoomInfo(),
                X_OFFSET + 2, Y_OFFSET + 2, MyColors.WHITE);
        state.getDungeon().drawYourself(model, state.getPartyPosition(), state.getCurrentLevel(), matrix);
        if (state.isMapView()) {
            state.getDungeon().getMap().drawYourself(model, state.getPartyPosition(), state.getCurrentLevel());
        }
    }

    @Override
    protected String getUnderText(Model model) {
        if (matrix.getSelectedElement() == null) {
            return "";
        }
        return matrix.getSelectedElement().getDescription();
    }

    @Override
    protected String getTitleText(Model model) {
        return dungeonType.toUpperCase();
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (state.isMapView()) {
            state.setMapView(false);
            return true;
        }
        return matrix.handleKeyEvent(keyEvent);
    }
}
