package view.subviews;

import model.Model;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public abstract class AvatarSubView extends SubView {
    private MovementAnimation movementAnimation;

    @Override
    protected final void drawArea(Model model) {
        specificDrawArea(model);
        if (movementAnimation != null) {
            movementAnimation.drawYourself(model);
        }
    }

    protected abstract void specificDrawArea(Model model);

    public void addMovementAnimation(Sprite avatarSprite, Point fromPoint, Point toPoint) {
        movementAnimation = new MovementAnimation(fromPoint, toPoint, avatarSprite);
    }

    public void removeMovementAnimation() {
        movementAnimation = null;
    }

    protected boolean movementAnimationIsDone() {
        if (movementAnimation == null) {
            return true;
        }
        return movementAnimation.isDone();
    }

    private class MovementAnimation implements Animation {
        private double steps;
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
            steps = fromPoint.distance(toPoint)/1.5;
            this.diff = new Point2D.Double((to.x - from.x) / steps, (to.y - from.y) / steps);
        }

        @Override
        public synchronized void stepAnimation(long elapsedTimeMs, Model model) {
            if (from == null) {
                System.err.println("From was null!");
            }
            if (shift == null) {
                System.err.println("Shift was null!");
            }
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
            return this.done;
        }

        @Override
        public void synch() {

        }

        public void drawYourself(Model model) {
            model.getScreenHandler().register("movefrom", from, sprite, 2, (int)shift.x, (int)shift.y);
        }
    }
}
