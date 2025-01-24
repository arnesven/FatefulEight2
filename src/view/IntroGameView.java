package view;

import control.FatefulEight;
import model.Model;
import sound.SoundEffects;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.CharClassIconSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class IntroGameView extends GameView implements Animation {

    private static final long ANIMATION_START_MS = 12000;
    private static final long THUNDER_START_MS = 3700;
    private static final long FANFARE_START_MS = 3000;
    private static final String START_SOUND = "Rise03";
    private static final String JINGLE_SOUND = "Rise06";
    private static final Sprite BLACK_SPRITE = new CharClassIconSprite(0x1F, MyColors.RED);
    private static Sprite splashSprite = makeSprite();
    private static Sprite[] titleSprites = makeTitle();
    private int aniIndex = 0;
    private long lastUpdate = 0;
    private long animationTime = 0;
    private boolean runAnimation = false;
    private boolean fading = true;
    private double fadeLevel = 1.0;
    private long totalTime = 0;
    private boolean thunder = false;
    private boolean fanfare = false;
    private boolean jingle = false;

    private boolean aniDone = false;

    public IntroGameView() {
        super(true);
        AnimationManager.register(this);
    }

    @Override
    public void transitionedTo(Model model) {
        SoundEffects.preload("thunder", false);
        SoundEffects.preload("intro_fanfare", false);
        SoundEffects.preload(JINGLE_SOUND, false);
        SoundEffects.preload(START_SOUND, false);
    }

    @Override
    public void transitionedFrom(Model model) {
        model.getScreenHandler().clearAll();
        AnimationManager.unregister(this);
    }

    @Override
    public void internalUpdate(Model model) {
        model.getScreenHandler().put(0, 5, splashSprite);
        for (int x = 0; x < 640/32; ++x) {
            model.getScreenHandler().put(x*4, 46, BLACK_SPRITE);
        }
        BorderFrame.drawCentered(model.getScreenHandler(), "Alt + Enter for fullscreen", 47, MyColors.WHITE);
        BorderFrame.drawCentered(model.getScreenHandler(), "Fateful Eight v " + FatefulEight.version + " - Written by Erik A. Nilsson - Copyright (C) 2025", 49, MyColors.CYAN);
        model.getScreenHandler().clearForeground();
        if (!fading) {
            model.getScreenHandler().register("titleani", new Point(0, 0), titleSprites[aniIndex]);
            if (aniDone) {
                BorderFrame.drawCentered(model.getScreenHandler(), "- Press any key -", 40, MyColors.WHITE);
            }
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
        SoundEffects.playSound(START_SOUND);
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
            if (totalTime > THUNDER_START_MS && !thunder) {
                SoundEffects.playSound("thunder");
                thunder = true;
            }
            if (fadeLevel <= 0) {
                fadeLevel = 0.0;
                fading = false;
                model.getScreenHandler().setFade(1.0, MyColors.WHITE);
            }
            madeChanges();
        } else {
            animationTime += elapsedTimeMs;
            if (animationTime > FANFARE_START_MS && !fanfare) {
                SoundEffects.playSound("intro_fanfare");
                fanfare = true;
            }
            
            if (animationTime > ANIMATION_START_MS) {
                runAnimation = true;
                if (!jingle) {
                    SoundEffects.playSound(JINGLE_SOUND);
                    jingle = true;
                }
            }

            if (runAnimation) {
                aniIndex = (int) ((animationTime - ANIMATION_START_MS) / 40);
                if (aniIndex >= titleSprites.length) {
                    aniIndex = 0;
                    runAnimation = false;
                    animationTime = 0;
                    aniDone = true;
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
