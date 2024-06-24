package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import view.sprites.CharacterSelectCursor;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SelectPartyMemberSubView extends SubView {

    private final SubView previous;
    private SteppingMatrix<GameCharacter> matrix;

    public SelectPartyMemberSubView(Model model, GameCharacter preselected) {
        this.previous = model.getSubView();
        this.matrix = new SteppingMatrix<>(2, 4);
        int col = 0;
        int row = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (model.getParty().getBench().contains(gc)) {
                col++;
            } else {
                matrix.addElement(col++, row, gc);
            }
            if (col == matrix.getColumns()) {
                row++;
                col = 0;
            }
        }
        if (preselected != null) {
            matrix.setSelectedElement(preselected);
        }
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        drawCursor(model);
    }

    private void drawCursor(Model model) {
        Point p = new Point(matrix.getSelectedPoint());
        Sprite sp = CharacterSelectCursor.LEFT_ARROW;
        if (p.x == 1) {
            sp = CharacterSelectCursor.RIGHT_ARROW;
        }
        p.x = p.x*50 + 6;
        p.y = p.y*11 + 5;
        model.getScreenHandler().register("memberSelectCursor", p, sp, 2);
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
