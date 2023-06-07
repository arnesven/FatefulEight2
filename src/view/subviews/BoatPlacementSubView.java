package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.states.events.BoatsEvent;
import sprites.CombatCursorSprite;
import view.sprites.BoatSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

public class BoatPlacementSubView extends SubView {
    private static final Sprite[] BOAT_SPRITES = new BoatSprite[]{new BoatSprite(0x94),
            new BoatSprite(0xA4), new BoatSprite(0xB4)};
    private final SteppingMatrix<GameCharacter> matrix;
    private final BoatsEvent state;
    private final ArrayList<BoatsEvent.Boat> boats;

    public BoatPlacementSubView(BoatsEvent state, SteppingMatrix<GameCharacter> matrix, ArrayList<BoatsEvent.Boat> boats) {
        this.matrix = matrix;
        this.state = state;
        this.boats = boats;
    }

    @Override
    protected void drawArea(Model model) {
        drawBackground(model);
        drawMatrix(model);
        drawCursor(model);
    }

    private void drawBackground(Model model) {
        for (BoatsEvent.Boat boat : boats) {
            for (int y = 0; y < boat.getLength(); ++y) {
                Point p = convertToScreen(boat.getXPos(), y+3);
                int index = y == 0 ? 0 : y == boat.getLength()-1 ? 2 : 1;
                model.getScreenHandler().put(p.x, p.y, BOAT_SPRITES[index]);
            }
        }
    }

    private void drawMatrix(Model model) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (matrix.getElementAt(x, y) != null) {
                    Point p = matrixPosToScreen(x, y);
                    GameCharacter ch = matrix.getElementAt(x, y);
                    model.getScreenHandler().register(ch.getAvatarSprite().getName(), p, ch.getAvatarSprite());
                    if (matrix.getSelectedElement() != ch) {
                        ch.getAvatarSprite().synch();
                    }
                }
            }
        }
    }

    private void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = new Point(matrix.getSelectedPoint());
        p = matrixPosToScreen(p.x, p.y);
        p.y -= 4;
        model.getScreenHandler().register("boatscursor", p, cursor, 2);
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + x*4, Y_OFFSET + y*4);
    }

    private Point matrixPosToScreen(int x, int y) {
        return convertToScreen(x, y == 0 ? 1 : y + 2);
    }


    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return "RIVER CROSSING - BOATS";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            state.shiftCharacter(model);
        }
        return matrix.handleKeyEvent(keyEvent);
    }

}
