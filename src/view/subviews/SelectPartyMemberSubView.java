package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import view.DrawingArea;
import view.sprites.CharacterSelectCursor;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SelectPartyMemberSubView extends SubView {

    private final SubView previous;
    private SteppingMatrix<GameCharacter> matrix;
    private boolean orientation;

    public SelectPartyMemberSubView(Model model, GameCharacter preselected) {
        this.previous = model.getSubView();
        makeMatrix(model, preselected);
    }

    private void makeMatrix(Model model, GameCharacter preselected) {
        this.matrix = new SteppingMatrix<>(DrawingArea.WINDOW_COLUMNS, DrawingArea.WINDOW_ROWS);
        int count = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            Point p = model.getParty().getLocationForPartyMember(count++);
            p.x += 4;
            p.y -= 1;
            matrix.addElement(p.x, p.y, gc);
        }
        if (preselected != null) {
            matrix.setSelectedElement(preselected);
        }
        this.orientation = model.getParty().isDrawVertically();
    }

    @Override
    protected void drawArea(Model model) {
        if (orientation != model.getParty().isDrawVertically()) {
            makeMatrix(model, model.getParty().getPartyMember(0));
        }
        previous.drawArea(model);
        drawCursor(model);
    }

    private void drawCursor(Model model) {
        Point p = new Point(matrix.getSelectedPoint());
        Sprite sp = CharacterSelectCursor.LEFT_ARROW;
        model.getScreenHandler().register("memberSelectCursor", p, sp, 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getFullName();
    }

    @Override
    protected String getTitleText(Model model) {
        return "SELECT CHARACTER";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    public GameCharacter getSelectedCharacter() {
        return matrix.getSelectedElement();
    }
}
