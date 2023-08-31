package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.states.horserace.HorseRaceTrack;
import model.states.horserace.TrackTerrain;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HorseRacingSubView extends SubView implements Animation {

    public static final int HORSE_VERTICAL_POSITION = 2;
    private static final long ANIMATION_DELAY = 3;
    private static final long ACCELERATION_DELAY = 30;
    private static final int JUMP_LENGTH = 10;
    private final Horse horse;
    private final RidingSprite gallopSprite;
    private final RidingSprite trotSprite;
    private HorseRaceTrack horseRaceTrack = new HorseRaceTrack();
    private Point position = new Point(3, 0);
    private int positionShift = 0;
    private int currentSpeed = 1;
    private long internalStep = 0;
    private int accelStep = 0;
    private int laneChangeCooldown = 0;
    private int jumpCounter = 0;

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
        drawHorse(model);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+4, blackBlock, 3);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_MAX-4, Y_MAX, blackBlock, 3);
    }

    private void drawHorse(Model model) {
        RidingSprite spriteToUse = trotSprite;
        int yshift = 0;
        if (jumpCounter > 0) {
            spriteToUse = gallopSprite;
            spriteToUse.synch();
            yshift = -(JUMP_LENGTH/2 - Math.abs(JUMP_LENGTH/2 - jumpCounter));
        } else {
            if (currentSpeed > 2) {
                spriteToUse = gallopSprite;
            }
            if (currentSpeed == 0) {
                spriteToUse.synch();
            } else if (currentSpeed == 2 || currentSpeed == 4) {
                spriteToUse.setDelay(12);
            } else if (currentSpeed > 4) {
                spriteToUse.setDelay(20 - currentSpeed * 2);
            }
        }

        model.getScreenHandler().register(gallopSprite.getName(), convertToScreen(position.x, HORSE_VERTICAL_POSITION),
                spriteToUse, 1, 0, yshift);
        Sprite effectSprite = getCurrentTerrain().getEffectSprite();
        if (effectSprite != null && jumpCounter == 0) {
            Point effectPos = convertToScreen(position.x, HORSE_VERTICAL_POSITION + 1);
            model.getScreenHandler().register(effectSprite.getName(), effectPos,
                    effectSprite, 2);
        }
    }

    public Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col*4 + 2,
                Y_OFFSET + row * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Speed: " + currentSpeed; // + ", Strafe " + (laneChangeCooldown>0?"not ready":"READY") + ", " +
                //(jumpCounter>0?"JUMPING":"");
    }

    private TrackTerrain getCurrentTerrain() {
        return horseRaceTrack.getTerrain(position, positionShift);
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
        if (internalStep % ANIMATION_DELAY == 0) {
            positionShift += currentSpeed;
            while (positionShift >= 32) {
                positionShift -= 32;
                position.y += 1;
            }
            if (jumpCounter == 0) {
                accelStep++;
            }
            laneChangeCooldown = Math.max(0, laneChangeCooldown-1);
            jumpCounter = Math.max(0, jumpCounter-1);
        }
        
        if (accelStep > ACCELERATION_DELAY) {
            accelStep = 0;
            currentSpeed = Math.min(currentSpeed + 1, 8);
        }

        TrackTerrain currentTerrain = getCurrentTerrain();
        if (currentSpeed > currentTerrain.getMaximumSpeed()) {
            if (currentTerrain.getMaximumSpeed() == 0 || jumpCounter == 0) {
                currentSpeed = currentTerrain.getMaximumSpeed();
                accelStep = 0;
                jumpCounter = 0;
            }
        }
    }

    @Override
    public void synch() {   }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            int newX = Math.max(0, position.x - 1);
            if (canEnter(newX)) {
                changeLanes(newX);
            }
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            int newX = Math.min(HorseRaceTrack.TRACK_WIDTH-1, position.x + 1);
            if (canEnter(newX)) {
               changeLanes(newX);
            }
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            if (jumpCounter == 0) {
                jumpCounter = JUMP_LENGTH;
            }
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    private void changeLanes(int newX) {
        position.x = newX;
        if (laneChangeCooldown > 0) {
            currentSpeed = Math.max(1, currentSpeed - 1);
            accelStep = 0;
        } else {
            currentSpeed = Math.max(1, currentSpeed);
        }
        laneChangeCooldown = 20;
    }

    private boolean canEnter(int newX) {
        return jumpCounter == 0 &&
                horseRaceTrack.getTerrain(new Point(newX, position.y), positionShift).canBeEntered();
    }
}
