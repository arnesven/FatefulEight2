package model.states.horserace;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.horses.Horse;
import view.sprites.RidingSprite;
import view.sprites.Sprite;
import view.subviews.HorseRacingSubView;

import java.awt.*;

public class HorseRacer {

    private static final long ACCELERATION_DELAY = 30;
    private static final int JUMP_LENGTH = 10;

    private final HorseRaceTrack horseRaceTrack;
    private final GameCharacter character;
    private Point position;
    private int positionShift = 0;
    private int currentSpeed = 0;
    private int accelStep = 0;
    private int laneChangeCooldown = 0;
    private int jumpCounter = 0;
    private final RidingSprite gallopSprite;
    private final RidingSprite trotSprite;

    public HorseRacer(int xStart, GameCharacter chara, Horse horse, HorseRaceTrack horseRaceTrack) {
        position = new Point(xStart, 0);
        this.character = chara;
        trotSprite = new RidingSprite(chara, horse, 0);
        gallopSprite = new RidingSprite(chara, horse, 1);
        this.horseRaceTrack = horseRaceTrack;
        //gallopSprite.setDelay(8);
    }

    public void drawHorse(Model model, int horseVerticalPosition) {
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

        model.getScreenHandler().register(gallopSprite.getName(), HorseRacingSubView.convertToScreen(position.x, horseVerticalPosition),
                spriteToUse, 1, 0, yshift);
        Sprite effectSprite = getCurrentTerrain().getEffectSprite();
        if (effectSprite != null && jumpCounter == 0) {
            Point effectPos = HorseRacingSubView.convertToScreen(position.x, horseVerticalPosition + 1);
            model.getScreenHandler().register(effectSprite.getName(), effectPos,
                    effectSprite, 2);
        }
    }

    private TrackTerrain getCurrentTerrain() {
        return horseRaceTrack.getTerrain(position, positionShift);
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public Point getPosition() {
        return position;
    }

    public int getYShift() {
        return positionShift;
    }

    public void updateYourself(Model model) {
        move();
        accelerate();
        laneChangeCooldown = Math.max(0, laneChangeCooldown-1);
        jumpCounter = Math.max(0, jumpCounter-1);
        applyTerrain();
    }

    private void move() {
        positionShift += currentSpeed;
        while (positionShift >= 32) {
            positionShift -= 32;
            position.y += 1;
        }
    }

    private void accelerate() {
        if (jumpCounter == 0) {
            accelStep++;
        }
        if (accelStep > ACCELERATION_DELAY) {
            accelStep = 0;
            currentSpeed = Math.min(currentSpeed + 1, 8);
        }
    }

    private void applyTerrain() {
        TrackTerrain currentTerrain = getCurrentTerrain();
        if (currentSpeed > currentTerrain.getMaximumSpeed(this)) {
            if ((currentTerrain.getMaximumSpeed(this) == 0 && !currentTerrain.canBeEntered()) || jumpCounter == 0) {
                currentSpeed = currentTerrain.getMaximumSpeed(this);
                accelStep = 0;
                jumpCounter = 0;
            }
        }
    }

    public void possiblyMoveLeft() {
        int newX = Math.max(0, position.x - 1);
        if (canEnter(newX)) {
            changeLanes(newX);
        }
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

    public void possiblyMoveRight() {
        int newX = Math.min(HorseRaceTrack.TRACK_WIDTH-1, position.x + 1);
        if (canEnter(newX)) {
            changeLanes(newX);
        }
    }

    public void possiblyJump() {
        if (jumpCounter == 0) {
            jumpCounter = JUMP_LENGTH;
        }
    }

    public GameCharacter getCharacter() {
        return character;
    }
}
