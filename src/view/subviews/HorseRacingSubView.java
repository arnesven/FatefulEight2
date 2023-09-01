package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.states.horserace.HorseRaceTrack;
import model.states.horserace.HorseRacer;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class HorseRacingSubView extends SubView implements Animation {

    private Sprite BANNER_ICON = new BannerSprite();
    private Sprite PATH_ICON = new Sprite32x32("path", "riding.png",
            0x61, MyColors.DARK_GREEN, MyColors.GREEN, MyColors.BROWN);
    private static final long ANIMATION_DELAY = 3;
    public static final int HORSE_VERTICAL_POSITION = 2;
    private final Horse horse;
    private HorseRaceTrack horseRaceTrack = new HorseRaceTrack();
    private long internalStep = 0;

    private HorseRacer player;
    private boolean animationStarted = false;

    public HorseRacingSubView(GameCharacter rider, Horse horse) {
        this.horse = horse;
        this.player = new HorseRacer(3, rider, horse, horseRaceTrack);
        AnimationManager.registerPausable(this);
    }

    @Override
    protected void drawArea(Model model) {
        horseRaceTrack.drawYourself(model, this, player);
        player.drawHorse(model, HORSE_VERTICAL_POSITION);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+4, blackBlock, 20);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_MAX-4, Y_MAX, blackBlock, 20);
        drawBanner(model);
    }

    private void drawBanner(Model model) {
        for (int i = 0; i < 8; ++i) {
            model.getScreenHandler().register(PATH_ICON.getName(),
                    new Point(X_OFFSET + i*4, Y_OFFSET), PATH_ICON, 21, 0, 5);
        }
        model.getScreenHandler().register(BANNER_ICON.getName(),
                new Point(X_OFFSET + (X_MAX - X_OFFSET)/2, Y_OFFSET), BANNER_ICON, 23);
        Sprite symbol = CombatSubView.getInitiativeSymbol(player.getCharacter(), model);
        int halfPos = (player.getPosition().y + HorseRaceTrack.TRACK_LENGTH / 2) % HorseRaceTrack.TRACK_LENGTH;
        int xPos = (halfPos * (X_MAX - X_OFFSET)) / HorseRaceTrack.TRACK_LENGTH;
        model.getScreenHandler().register(symbol.getName(), new Point(X_OFFSET + xPos, Y_OFFSET+2), symbol, 22);
    }


    public static Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col*4 + 2,
                Y_OFFSET + row * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Speed: " + player.getCurrentSpeed() + ", Position: " + player.getPosition().y;
    }

    @Override
    protected String getTitleText(Model model) {
        return "HORSE RACE";
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (!animationStarted) {
            return;
        }
        internalStep++;
        if (internalStep % ANIMATION_DELAY == 0) {
            player.updateYourself(model);
        }
    }

    @Override
    public void synch() {   }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            player.possiblyMoveLeft();
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.possiblyMoveRight();
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            player.possiblyJump();
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public void startRace() {
        animationStarted = true;
    }

    public boolean raceIsOver() {
        return false;
    }

    private static class BannerSprite extends Sprite {
        public BannerSprite() {
            super("racebanner", "riding.png", 7, 5, 16, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.PINK);
        }
    }
}
