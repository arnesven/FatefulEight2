package view;

import model.Model;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class IntroGameView extends GameView implements Animation {

    private static final long ANIMATION_START_MS = 2000;
    private static Sprite splashSprite = makeSprite();
    private static Sprite[] titleSprites = makeTitle();
    private int aniIndex = 0;
    private long lastUpdate = 0;
    private long animationTime = 0;
    private boolean runAnimation = false;
    private boolean fading = true;
    private double fadeLevel = 1.0;
    private long totalTime = 0;

    public IntroGameView() {
        super(true);
        AnimationManager.register(this);
    }

    @Override
    public void transitionedTo(Model model) {

    }

    @Override
    public void transitionedFrom(Model model) {
        model.getScreenHandler().clearAll();
        AnimationManager.unregister(this);
    }

    @Override
    public void internalUpdate(Model model) {
        model.getScreenHandler().put(0, 5, splashSprite);
        model.getScreenHandler().clearForeground();
        if (!fading) {
            model.getScreenHandler().register("titleani", new Point(0, 0), titleSprites[aniIndex]);
            BorderFrame.drawCentered(model.getScreenHandler(), "- Press any key -", 40, MyColors.WHITE);
        }
        model.getScreenHandler().setFade(fadeLevel, MyColors.BLACK);
    }

    @Override
    public GameView getNextView(Model model) {
        return new StartGameMenu();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        super.setTimeToTransition(true);
    }


    private static Sprite makeSprite() {
        Sprite sp = new Sprite("splash", "splash.png", 0, 0, 640, 400);
        MyColors.transformImage(sp);
        return sp;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        totalTime += elapsedTimeMs;
        if (fading && totalTime > 1000) {
            double newFade = fadeLevel - (double) elapsedTimeMs / 3000.0;
            fadeLevel = newFade;
            if (fadeLevel <= 0) {
                fadeLevel = 0.0;
                fading = false;
                model.getScreenHandler().setFade(1.0, MyColors.WHITE);
            }
            madeChanges();
        } else {
            animationTime += elapsedTimeMs;
            if (animationTime > ANIMATION_START_MS) {
                runAnimation = true;
            }

            if (runAnimation) {
                aniIndex = (int) ((animationTime - ANIMATION_START_MS) / 40);
                if (aniIndex >= titleSprites.length) {
                    aniIndex = 0;
                    runAnimation = false;
                    animationTime = -3 * ANIMATION_START_MS;
                }
                madeChanges();
            }
        }
    }


    private static Sprite[] makeTitle() {
        Sprite[] result = new Sprite[12];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite("title"+i, "title.png", 0, i, 640, 160);
            result[i].setColor1(MyColors.LIGHT_YELLOW);
            result[i].setColor2(MyColors.BLACK);
            result[i].setColor3(MyColors.WHITE);
            result[i].shiftUpPx(26);
        }
        return result;
    }

    @Override
    public void synch() {

    }
}
