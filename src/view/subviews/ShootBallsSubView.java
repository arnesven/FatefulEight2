package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.items.weapons.BowWeapon;
import model.states.ShootBallsState;
import util.Arithmetics;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.*;

import java.util.List;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ShootBallsSubView extends AimingSubView implements Animation {

    private static final Sprite BALL_SPRITE = new BallSprite();
    private static final int[] VERTICAL_SPEEDS = new int[]{12, 12, 10, 8};
    public static final int MAX_BALLS = 3;
    private final List<Ball> balls = new ArrayList<>();
    private final List<Ball> destroyedBalls = new ArrayList<>();
    private final ShootBallsState state;
    private final GameCharacter shooter;
    private final BowWeapon bowToUse;
    private boolean animationStarted = false;
    private int internalCount = 0;
    private int animationDelay = 6;
    private static final Rectangle SUBVIEW_BOUNDS = new Rectangle(X_OFFSET+1, Y_OFFSET+1, X_MAX - X_OFFSET-2, Y_MAX - Y_OFFSET - 2);
    private static final Rectangle OUTER_BOUNDS = new Rectangle(X_OFFSET+1, Y_OFFSET-19, X_MAX - X_OFFSET-2, Y_MAX - Y_OFFSET - 2 + 20);
    private final List<MyPair<Ball, RunOnceAnimationSprite>> destroyAnimations = new ArrayList<>();
    private int reloadCounter;

    public ShootBallsSubView(ShootBallsState shootBallsState, GameCharacter shooter, BowWeapon bowToUse) {
        this.state = shootBallsState;
        this.shooter = shooter;
        this.bowToUse = bowToUse;
        reloadCounter = 0;

        AnimationManager.registerPausable(this);
        for (int i = 0; i < MAX_BALLS; ++i) {
            balls.add(makeBall());
        }
    }

    private Ball makeBall() {
        int x = MyRandom.randInt(-3, 3);
        int y = VERTICAL_SPEEDS[Math.abs(x)];
        int xOff = MyRandom.randInt(4);
        if (x > 0) {
            xOff = -xOff;
        }
        int yOff = MyRandom.randInt(3);
        return new Ball(new Point(X_OFFSET+OFFSETS.x + xOff, Y_MAX+1+yOff),
                new Point(x, -y));
    }

    @Override
    protected String getUnderText(Model model) {
        String text = "Using " + bowToUse.getName() + ", ";
        if (shotReady()) {
            text += "SHOT READY.";
        } else {
            text += "next shot in " + reloadCounter + " seconds.";
        }
        return text;
    }

    @Override
    protected String getTitleText(Model model) {
        return "ARCHERY CONTEST - ROUND 2";
    }

    @Override
    protected void innerDrawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX-9,
                lightBlueBlock);
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_MAX-9, Y_MAX,
                greenBlock);
        for (Ball b : balls) {
            if (SUBVIEW_BOUNDS.contains(b.position)) {
                model.getScreenHandler().register(BALL_SPRITE.getName(), b.position, BALL_SPRITE, 0,
                        b.shift.x - 12, b.shift.y - 12);
            }
        }
        for (MyPair<Ball, RunOnceAnimationSprite> ani : destroyAnimations) {
            model.getScreenHandler().register(ani.second.getName(), ani.first.position, ani.second, 1,
                    ani.first.shift.x - 12, ani.first.shift.y - 12);
        }
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return false;
    }

    public void startAnimation() {
        for (Ball b : destroyedBalls) {
            balls.remove(b);
            destroyAnimations.add(new MyPair<>(b, new StrikeEffectSprite(MyColors.RED)));
        }
        destroyedBalls.clear();
        animationStarted = true;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (animationStarted) {
            internalCount++;
            if (internalCount % animationDelay == 0) {
                for (Ball b : balls) {
                    int x = b.position.x*8 + b.shift.x + b.velocity.x;
                    int y = b.position.y*8 + b.shift.y + b.velocity.y;
                    b.position.x = x / 8;
                    b.position.y = y / 8;
                    b.shift.x = x % 8;
                    b.shift.y = y % 8;
                    if (internalCount % (animationDelay*4) == 0) {
                        b.velocity.y += 1;
                    }
                }
                if (internalCount % (animationDelay*4) == 0) {
                    reloadCounter = Math.max(reloadCounter - 1, 0);
                }
            }
        }
    }

    @Override
    public void synch() {

    }

    public void stopAnimation() {
        animationStarted = false;
    }

    public boolean checkForHit(Point finalResult) {
        for (ShootBallsSubView.Ball b : balls) {
            System.out.println("Ball at (" + b.position.x + "," + b.position.y + ")");
            Rectangle hitBox = new Rectangle(b.position.x - X_OFFSET - OFFSETS.x - 1,
                    b.position.y - Y_OFFSET - OFFSETS.y - 1, 3, 3);
            System.out.println("Hitbox is " + hitBox.x + "," + hitBox.y);
            if (hitBox.contains(finalResult)) {
                destroyedBalls.add(b);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (animationStarted) {
            return super.handleKeyEvent(keyEvent, model);
        }
        return false;
    }

    public boolean stillBallsToShoot() {
        for (Ball b : balls) {
            if (OUTER_BOUNDS.contains(b.position)) {
                return true;
            }
        }
        return false;
    }

    public int getScore() {
        return MAX_BALLS - balls.size();
    }

    public boolean shootArrow(Point finalResult, Sprite arrowSprite) {
        addArrow(finalResult, arrowSprite);
        reloadCounter = bowToUse.getReloadSpeed();
        return checkForHit(finalResult);
    }

    public boolean shotReady() {
        return reloadCounter == 0;
    }

    private static class Ball {
        public Point position;
        public Point shift;
        public Point velocity;
        public Ball(Point p, Point v) {
            position = p;
            shift = new Point(0, 0);
            velocity = v;
        }
    }

    private static class BallSprite extends Sprite {
        public BallSprite() {
            super("shootballball", "arrows.png", 0, 2, 24, 24);
            setColor2(MyColors.RED);
            setColor3(MyColors.DARK_RED);
            setColor4(MyColors.WHITE);
        }
    }
}
