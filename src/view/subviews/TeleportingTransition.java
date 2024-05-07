package view.subviews;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.Random;

public class TeleportingTransition extends TransitionView {


    protected static final Sprite background = new FilledBlockSprite(MyColors.LIGHT_BLUE);
    protected static final Sprite[] foregrounds = new Sprite[]{
            new FilledBlockSprite(MyColors.CYAN),
            new FilledBlockSprite(MyColors.LIGHT_PINK),
            new FilledBlockSprite(MyColors.LIGHT_YELLOW)
    };
    private static final int STEPS_START = (Y_MAX - Y_OFFSET) * 2;
    private final Point position;
    private final int randomSeed;
    private final boolean inCaves;
    private boolean flipped = false;

    public TeleportingTransition(SubView fromView, SubView toView, String title, int stepsStart, Point position, boolean inCaves) {
        super(fromView, toView, title, stepsStart);
        this.position = position;
        randomSeed = MyRandom.randInt(100000);
        this.inCaves = inCaves;
    }

    @Override
    protected void drawAnimation(Model model, int steps) {
        if (steps == 0) {
            model.getParty().setPosition(position);
            if (inCaves) {
                model.enterCaveSystem(false);
            } else {
                model.exitCaveSystem(false);
            }
            this.flipped = true;
        }

        Random random = new Random(randomSeed);
        if (flipped) {
            steps = -steps;
        }

        int width = X_MAX - X_OFFSET;
        for (int i = 150; i > 0; --i) {
            int x = random.nextInt(width/2) + random.nextInt(width/2) + X_OFFSET;
            int y = random.nextInt(STEPS_START * 3) + random.nextInt(STEPS_START * 3)
                    - STEPS_START * 3 + Y_OFFSET + steps * 4 - 15;
            Sprite foreground = foregrounds[random.nextInt(foregrounds.length)];
            model.getScreenHandler().fillForeground(x, x+1,
                    Math.max(y, Y_OFFSET),
                    Math.min(y+60, Y_MAX),
                    foreground, 33);
        }
    }

    @Override
    protected boolean stepsAreDone(int steps) {
        return steps >= STEPS_START;
    }

    public static void transition(Model model, SubView nextSubView, Point position, boolean inCaves) {
        TransitionView spiral = new TeleportingTransition(model.getSubView(), nextSubView,
                nextSubView.getTitleText(model), STEPS_START, position, inCaves);
        model.setSubView(spiral);
        spiral.waitToBeOver();
        model.setSubView(nextSubView);
    }

}
