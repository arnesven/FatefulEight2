package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.map.World;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MapSubView extends AvatarSubView {
    public static final int MAP_WIDTH_HEXES = 8;
    public static final int MAP_HEIGHT_HEXES = 10;
    public static final String TITLE_TEXT = "TRAVEL";
    private final List<Point> directions;
    private SteppingMatrix<Point> matrix;
    private boolean avatarEnabled = true;

    public MapSubView(Model model) {
        matrix = new SteppingMatrix<>(3, 3);
        directions = World.getDirectionsForPosition(model.getParty().getPosition());
        matrix.addElement(1, 1, new Point(0, 0));

        addIfOkToMoveTo(model,2, 2, directions.get(0));
        addIfOkToMoveTo(model, 1, 2, directions.get(1));
        addIfOkToMoveTo(model, 0, 2, directions.get(2));
        addIfOkToMoveTo(model, 0, 0, directions.get(3));
        addIfOkToMoveTo(model, 1, 0, directions.get(4));
        addIfOkToMoveTo(model, 2, 0, directions.get(5));

    }

    private void addIfOkToMoveTo(Model model, int col, int row, Point p) {
        if (canMoveToHex(model, p)) {
            matrix.addElement(col, row, p);
        }
    }

    private boolean canMoveToHex(Model model, Point point) {
        Point p = new Point(model.getParty().getPosition());
        p.x = p.x + point.x;
        p.y = p.y + point.y;
        return model.getWorld().canTravelTo(model, p);
    }

    @Override
    public void specificDrawArea(Model model) {
        Point cursorPos = getSelectedDestination(model);
        model.getWorld().drawYourself(model, model.getParty().getPosition(), model.getParty().getPosition(),
                MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, cursorPos, avatarEnabled);
    }

    private Point getSelectedDestination(Model model) {
        Point sel = new Point(matrix.getSelectedElement());
        Point p = new Point(model.getParty().getPosition().x + sel.x,
                model.getParty().getPosition().y + sel.y);
        return p;
    }

    @Override
    protected String getUnderText(Model model) {
        return model.getHexInfo(getSelectedDestination(model));
    }

    @Override
    protected String getTitleText(Model model) {
        return TITLE_TEXT;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    public Point getSelectedDirection(Model model) {
        return matrix.getSelectedElement();
    }

    public void drawAvatarEnabled(boolean b) {
        this.avatarEnabled = b;
    }

    public String getNameForDirection(Point direction) {
        java.util.List<String> shorts = List.of("SE", "S", "SW", "NW", "N", "NE");
        return shorts.get(directions.indexOf(direction));
    }

    public String getSelectedDirectionName() {
        return getNameForDirection(matrix.getSelectedElement());
    }

    public List<Point> getDirections(Model model) {
        return directions;
    }
}
