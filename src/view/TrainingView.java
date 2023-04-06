package view;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.states.TrainingState;
import sprites.CombatCursorSprite;
import view.sprites.Sprite;
import view.subviews.SubView;

import java.awt.*;

public class TrainingView extends SubView {
    private final TrainingState state;
    private final SteppingMatrix<GameCharacter> matrix;

    public TrainingView(TrainingState state, SteppingMatrix<GameCharacter> matrix) {
        this.state = state;
        this.matrix = matrix;
    }

    @Override
    protected void drawArea(Model model) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (matrix.getElementAt(x, y) != null) {
                    Point p = convertToScreen(x, y);
                    GameCharacter ch = matrix.getElementAt(x, y);
                    model.getScreenHandler().register(ch.getAvatarSprite().getName(), p, ch.getAvatarSprite());
                }
            }
        }

        drawCursor(model);
    }

    private void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = new Point(matrix.getSelectedPoint());
        p = convertToScreen(p.x, p.y);
        p.y -= 4;
        model.getScreenHandler().register("trainingcursor", p, cursor, 2);
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + x*4, Y_OFFSET + y*4);
    }

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return "TEMPLE - TRAINING";
    }
}
