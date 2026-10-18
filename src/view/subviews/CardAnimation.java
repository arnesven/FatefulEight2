package view.subviews;

import model.Model;
import model.states.cardgames.TableSeating;
import view.MyColors;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.awt.geom.Point2D;

class CardAnimation implements Animation {
    private static final Sprite[] PINK_SPRITES = makeSprites(MyColors.PINK);
    private static final Sprite[] WHITE_SPRITES = makeSprites(MyColors.WHITE);
    private final boolean reverse;
    private Point2D.Double dxdy;
    private final Point2D.Double currentPos;
    private final int animationSteps;
    private int step;

    public CardAnimation(TableSeating seating, boolean reverse) {
        Point cardPos = seating.getHandPosition();
        Point center = new Point(SubView.X_OFFSET + (SubView.X_MAX - SubView.X_OFFSET) / 2 - 2, SubView.Y_OFFSET + (SubView.Y_MAX - SubView.Y_OFFSET) / 2 - 2);
        double distance = cardPos.distance(center);
        this.animationSteps = (int) Math.round(distance);
        this.dxdy = new Point2D.Double((cardPos.x - center.x) / (double) animationSteps,
                (cardPos.y - center.y) / (double) animationSteps);
        this.reverse = reverse;
        if (reverse) {
            this.dxdy = new Point2D.Double(-dxdy.x, -dxdy.y);
            currentPos = new Point2D.Double(cardPos.x, cardPos.y);
        } else {
            currentPos = new Point2D.Double(center.x, center.y);
        }
        this.step = 0;
        AnimationManager.register(this);
    }

    public CardAnimation(TableSeating seating) {
        this(seating, false);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (step < animationSteps) {
            currentPos.x += dxdy.x;
            currentPos.y += dxdy.y;
            step++;
        }
    }

    @Override
    public void synch() {
    }

    public void drawYourself(Model model) {
        Point totalPosition = new Point((int) (Math.round(currentPos.x * 8)), (int) (Math.round(currentPos.y * 8)));
        Point wholePosition = new Point(totalPosition.x / 8, totalPosition.y / 8);
        Point shift = new Point(totalPosition.x % 8, totalPosition.y % 8);
        Sprite spriteToUse;
        if (reverse) {
            spriteToUse = WHITE_SPRITES[(step / 3) % 4];
        } else {
            spriteToUse = PINK_SPRITES[(step / 3) % 4];
        }
        model.getScreenHandler().register(spriteToUse.getName(), wholePosition, spriteToUse, 2, shift.x, shift.y);
    }

    public boolean checkForDone() {
        if (step == animationSteps) {
            AnimationManager.unregister(this);
        }
        return step >= animationSteps;
    }

    private static Sprite[] makeSprites(MyColors cardColor) {
        Sprite[] sprites = new Sprite[]{new Sprite32x32("dealcard1", "cardgame.png", 0x15,
                MyColors.BLACK, cardColor, cardColor, MyColors.CYAN),
                new Sprite32x32("dealcard2", "cardgame.png", 0x16,
                        MyColors.BLACK, cardColor, cardColor, MyColors.CYAN),
                new Sprite32x32("dealcard3", "cardgame.png", 0x15,
                        MyColors.BLACK, cardColor, cardColor, MyColors.CYAN),
                new Sprite32x32("dealcard4", "cardgame.png", 0x16,
                        MyColors.BLACK, cardColor, cardColor, MyColors.CYAN)};
        sprites[2].setRotation(90);
        sprites[3].setRotation(90);
        return sprites;
    }
}
