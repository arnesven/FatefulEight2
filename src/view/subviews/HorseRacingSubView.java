package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.states.HorseRaceTrack;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HorseRacingSubView extends SubView implements Animation {

    public static final int HORSE_VERTICAL_POSITION = 2;
    private final Horse horse;
    private final RidingSprite gallopSprite;
    private final RidingSprite trotSprite;
    private HorseRaceTrack horseRaceTrack = new HorseRaceTrack();
    private Point position = new Point(3, 0);
    private int positionShift = 0;
    private int currentSpeed = 1;
    private long internalStep = 0;
    private long delay = 3;

    public HorseRacingSubView(GameCharacter rider, Horse horse) {
        this.horse = horse;
        trotSprite = new RidingSprite(rider, horse, 0);
        gallopSprite = new RidingSprite(rider, horse, 1);
        gallopSprite.setDelay(8);
        AnimationManager.registerPausable(this);
    }

    @Override
    protected void drawArea(Model model) {
        horseRaceTrack.drawYourself(model, this);
        RidingSprite spriteToUse = trotSprite;
        if (currentSpeed > 2) {
            spriteToUse = gallopSprite;
        }

        if (currentSpeed == 0) {
            spriteToUse.synch();
        } else if (currentSpeed == 2 || currentSpeed == 4) {
            spriteToUse.setDelay(12);
        } else if (currentSpeed > 4) {
            spriteToUse.setDelay(20 - currentSpeed*2);
        }

        model.getScreenHandler().register(gallopSprite.getName(), convertToScreen(position.x, HORSE_VERTICAL_POSITION),
                spriteToUse, 1);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+4, blackBlock, 1);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_MAX-4, Y_MAX, blackBlock, 1);
    }

    public Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col*4 + 2,
                Y_OFFSET + row * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Speed: " + currentSpeed;
    }

    @Override
    protected String getTitleText(Model model) {
        return "HORSE RACE";
    }

    public Point getPosition() {
        return position;
    }

    public int getYShift() {
        return positionShift;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        internalStep++;
        if (internalStep % delay == 0) {
            positionShift += currentSpeed;
            while (positionShift > 32) {
                positionShift -= 32;
                position.y += 1;
            }
        }
    }

    @Override
    public void synch() {   }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            position.x = Math.max(0, position.x - 1);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            position.x = Math.min(HorseRaceTrack.TRACK_WIDTH-1, position.x + 1);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            currentSpeed = Math.min(8, currentSpeed + 1);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            currentSpeed = Math.max(0, currentSpeed - 1);
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }
}
