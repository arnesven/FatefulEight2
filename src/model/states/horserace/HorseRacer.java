package model.states.horserace;

import model.Model;
import model.characters.GameCharacter;;
import model.horses.Horse;
import util.Arithmetics;
import util.MyRandom;
import view.sprites.RidingSprite;
import view.sprites.Sprite;
import view.subviews.HorseRacingSubView;

import java.util.List;
import java.awt.*;

public class HorseRacer {

    private static final int JUMP_LENGTH = 10;

    private final HorseRaceTrack horseRaceTrack;
    private final GameCharacter character;
    private Point position;
    private int positionShift = 0;
    private int currentSpeed = 0;
    private int accelStep = 0;
    private int laneChangeCooldown = 0;
    private int jumpCounter = 0;
    private int lap = 1;
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

    public void drawHorse(Model model, int horseVerticalPosition, int shiftDiff) {
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
                spriteToUse, 1, 0, yshift + shiftDiff);
        Sprite effectSprite = getCurrentTerrain().getEffectSprite();
        if (effectSprite != null && jumpCounter == 0) {
            Point effectPos = HorseRacingSubView.convertToScreen(position.x, horseVerticalPosition + 1);
            model.getScreenHandler().register(effectSprite.getName(), effectPos,
                    effectSprite, 2, 0, shiftDiff);
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

    public void updateYourself(List<HorseRacer> allRacers) {
        move();
        accelerate();
        laneChangeCooldown = Math.max(0, laneChangeCooldown-1);
        jumpCounter = Math.max(0, jumpCounter-1);
        applyTerrain();
        for (HorseRacer npc : allRacers) {
            if (npc.getPosition().x == position.x && npc.getPosition().y == position.y && npc != this) {
                currentSpeed = 0;
                accelStep = 0;
            }
        }
    }

    private void move() {
        positionShift += currentSpeed;
        while (positionShift >= 32) {
            positionShift -= 32;
            position.y = position.y + 1;
            if (position.y == HorseRaceTrack.TRACK_LENGTH) {
                position.y = 0;
                lap++;
            }
        }
    }

    private void accelerate() {
        if (jumpCounter == 0) {
            accelStep++;
        }
        if (accelStep > getAccelerationDelay()) {
            accelStep = 0;
            currentSpeed = Math.min(currentSpeed + 1, 8);
        }
    }

    protected int getAccelerationDelay() {
        if (currentSpeed == 0) {
            return 5;
        }
        return 30;
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

    public void possiblyMoveLeft(List<HorseRacer> allRacers) {
        int newX = Math.max(0, position.x - 1);
        if (canEnter(newX, allRacers)) {
            changeLanes(newX);
        }
    }

    public void possiblyMoveRight(List<HorseRacer> allRacers) {
        int newX = Math.min(HorseRaceTrack.TRACK_WIDTH-1, position.x + 1);
        if (canEnter(newX, allRacers)) {
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

    private boolean canEnter(int newX, List<HorseRacer> allRacers) {
        for (HorseRacer npc : allRacers) {
            if (npc.getPosition().x == newX && npc.getPosition().y == position.y) {
                return false;
            }
        }
        return jumpCounter == 0 &&
                horseRaceTrack.getTerrain(new Point(newX, position.y), positionShift).canBeEntered();
    }

    public void possiblyJump() {
        if (jumpCounter == 0) {
            jumpCounter = JUMP_LENGTH;
        }
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setSpeed(int i) {
        this.currentSpeed = i;
    }

    protected HorseRaceTrack getHorseRaceTrack() {
        return horseRaceTrack;
    }

    protected int getLaneChangeCooldown() {
        return laneChangeCooldown;
    }

    public int getLap() {
        return lap;
    }
}
