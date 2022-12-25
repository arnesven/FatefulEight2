package view.subviews;

import model.Model;
import model.SteppingMatrix;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.List;

public class MapSubView extends SubView {
    public static final int MAP_WIDTH_HEXES = 8;
    public static final int MAP_HEIGHT_HEXES = 10;
    public static final String TITLE_TEXT = "WORLD MAP";
    private final boolean isEven;
    private SteppingMatrix<Point> matrix;
    private MovementAnimation movementAnimation;
    private boolean avatarEnabled = true;

    public MapSubView(Model model) {
        matrix = new SteppingMatrix<>(3, 3);
        isEven = model.getParty().getPosition().x % 2 == 0;
        List<Point> directions;
        if (isEven) {
            directions = java.util.List.of(new Point(1, 1), new Point(0, 1), new Point(-1, 1), new Point(-1, 0), new Point(0, -1), new Point(1, 0));
        } else {
            directions = java.util.List.of(new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(-1, -1), new Point(0, -1), new Point(1, -1));
        }
        java.util.List<String> shorts = List.of("SE", "S", "SW", "NW", "N", "NE");
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
    public void drawArea(Model model) {
        Point cursorPos = getSelectedDestination(model);
        model.getWorld().drawYourself(model, model.getParty().getPosition(), model.getParty().getPosition(),
                MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, cursorPos, avatarEnabled);
        if (movementAnimation != null) {
            movementAnimation.drawYourself(model);
        }

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

    public void addMovementAnimation(Sprite avatarSprite, Point fromPoint, Point toPoint) {
        movementAnimation = new MovementAnimation(fromPoint, toPoint, avatarSprite);
    }

    public void drawAvatarEnabled(boolean b) {
        this.avatarEnabled = b;
    }

    public void removeMovementAnimation() {
        movementAnimation = null;
    }

    private class MovementAnimation implements Animation {
        private static final int STEPS = 5;
        private final Point from;
        private final Point to;
        private final Sprite sprite;
        private final Point2D.Double shift;
        private final Point2D.Double diff;
        private boolean done = false;

        public MovementAnimation(Point fromPoint, Point toPoint, Sprite avatarSprite) {
            this.from = fromPoint;
            this.to = toPoint;
            this.sprite = avatarSprite;
            AnimationManager.registerPausable(this);
            this.shift = new Point2D.Double(0.0, 0.0);
            this.diff = new Point2D.Double((to.x - from.x) / (double) STEPS, (to.y - from.y) / (double) STEPS);
        }

        @Override
        public void stepAnimation(long elapsedTimeMs, Model model) {
            double calcX = 8 * from.x + shift.x;
            double calcY = 8 * from.y + shift.y;
            if (new Point2D.Double(8*to.x, 8*to.y).distance(calcX, calcY) > 0.5) {
                shift.x = shift.x + diff.x;
                shift.y = shift.y + diff.y;
            } else {
                this.done = true;
            }
        }

        public boolean isDone() {
            return false;
        }

        @Override
        public void synch() {

        }

        public void drawYourself(Model model) {
            model.getScreenHandler().register("movefrom", from, sprite, 2, (int)shift.x, (int)shift.y);
        }
    }
}
