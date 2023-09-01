package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.states.horserace.HorseRaceTrack;
import model.states.horserace.HorseRacer;
import model.states.horserace.TrackTerrain;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HorseRacingSubView extends SubView implements Animation {

    private static final long ANIMATION_DELAY = 3;
    public static final int HORSE_VERTICAL_POSITION = 2;
    private final Horse horse;
    private HorseRaceTrack horseRaceTrack = new HorseRaceTrack();
    private long internalStep = 0;

    private HorseRacer player;

    public HorseRacingSubView(GameCharacter rider, Horse horse) {
        this.horse = horse;
        this.player = new HorseRacer(3, rider, horse, horseRaceTrack);
        AnimationManager.registerPausable(this);
    }

    @Override
    protected void drawArea(Model model) {
        horseRaceTrack.drawYourself(model, this, player);
        player.drawHorse(model, HORSE_VERTICAL_POSITION);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+4, blackBlock, 3);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_MAX-4, Y_MAX, blackBlock, 3);
    }



    public static Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col*4 + 2,
                Y_OFFSET + row * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Speed: " + player.getCurrentSpeed(); // + ", Strafe " + (laneChangeCooldown>0?"not ready":"READY") + ", " +
                //(jumpCounter>0?"JUMPING":"");
    }

    @Override
    protected String getTitleText(Model model) {
        return "HORSE RACE";
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
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

}
