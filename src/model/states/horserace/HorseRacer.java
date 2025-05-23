package model.states.horserace;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.horses.Horse;
import view.sprites.RidingSprite;
import view.sprites.Sprite;
import view.subviews.HorseRacingSubView;

import java.util.List;
import java.awt.*;

public class HorseRacer {

    private final int jumpCooldownMax;
    private final GameCharacter character;
    private final int jumpLength;
    private final HorseRacingSubView subView;
    private Point position;
    private int positionShift = 0;
    private int currentSpeed = 0;
    private int accelStep = 0;
    private int laneChangeCooldown = 0;
    private int jumpCounter = 0;
    private int lap = 1;
    private final RidingSprite gallopSprite;
    private final RidingSprite trotSprite;
    private final int changeDelay;
    private int strafeShift = 0;
    private int jumpCooldown = 0;

    public HorseRacer(int xStart, GameCharacter chara, Horse horse, HorseRacingSubView subView) {
        position = new Point(xStart, 0);
        this.character = chara;
        trotSprite = new RidingSprite(chara, horse, 0);
        gallopSprite = new RidingSprite(chara, horse, 1);
        this.subView = subView;
        this.changeDelay = 25 - chara.getRankForSkill(Skill.Survival) * 2;
        this.jumpLength = 8 + chara.getRankForSkill(Skill.Survival);
        this.jumpCooldownMax = 22 - chara.getRankForSkill(Skill.Survival) * 2;
        //gallopSprite.setDelay(8);
    }

    public void drawHorse(Model model, int horseVerticalPosition, int shiftDiff) {
        RidingSprite spriteToUse = trotSprite;
        int yshift = 0;
        if (jumpCounter > 0) {
            spriteToUse = gallopSprite;
            spriteToUse.synch();
            int jumpHeight = (int)(3.0 * (jumpLength/2 - Math.abs(jumpLength/2 - jumpCounter)));
            yshift = -jumpHeight;
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
                spriteToUse, 1, strafeShift, yshift + shiftDiff);
        Sprite effectSprite = getCurrentTerrain().getEffectSprite();
        if (effectSprite != null && jumpCounter == 0) {
            Point effectPos = HorseRacingSubView.convertToScreen(position.x, horseVerticalPosition + 1);
            model.getScreenHandler().register(effectSprite.getName(), effectPos,
                    effectSprite, 2, strafeShift, shiftDiff);
        }
    }

    private TrackTerrain getCurrentTerrain() {
        return subView.getTrack().getTerrain(position, positionShift);
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
        //int totalPos = position.y * 100 + positionShift;
        move();
        accelerate();
        laneChangeCooldown = Math.max(0, laneChangeCooldown-1);
        jumpCounter = Math.max(0, jumpCounter-1);
        if (!inTheAir()) {
            jumpCooldown = Math.max(0, jumpCooldown-1);
        }
        applyTerrain();
        for (HorseRacer npc : allRacers) {
            if (npc.getPosition().x == position.x && npc.getPosition().y == position.y && npc != this) {
                currentSpeed = 0;
                accelStep = 0;
            }
        }
//        if (totalPos == position.y * 100 + positionShift) {
//            System.out.println(this.character.getName() + " has not moved! Position=" +
//                    position.y + ", shift=" + positionShift + ", speed=" + currentSpeed +
//                    ", jumpcounter=" + jumpCounter +
//                    ", jumpcooldown=" + jumpCooldown + ", accel step=" + accelStep +
//                    ", lanechangecooldown=" + laneChangeCooldown + ", changedelay=" + changeDelay +
//                    ", strafeshift=" + strafeShift +
//                    ", terraint=" + getCurrentTerrain().getName());
//        }
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
            return 15;
        }
        return 32 - character.getRankForSkill(Skill.Survival);
    }

    protected void applyTerrain() {
        TrackTerrain currentTerrain = getCurrentTerrain();
        int maxSpeed = currentTerrain.getMaximumSpeed(this);
        if (currentSpeed > maxSpeed) {
            boolean blockingObstacle = maxSpeed == 0 && !currentTerrain.canBeEntered();
            if (!blockingObstacle && maxSpeed < 3) {
                autoJump();
            }
            if (blockingObstacle || !inTheAir()) {
                currentSpeed = currentTerrain.getMaximumSpeed(this);
                accelStep = 0;
                jumpCounter = 0;
            }
        }
    }

    protected void autoJump() { }

    private boolean inTheAir() {
        return jumpCounter > 0;
    }

    public void possiblyMoveLeft(List<HorseRacer> allRacers) {
        int newX = Math.max(0, position.x - 1);
        if (canEnter(newX, allRacers) && strafeShift == 0) {
            changeLanes(newX);
            strafeShift = 28;
        }
    }

    public void possiblyMoveRight(List<HorseRacer> allRacers) {
        int newX = Math.min(HorseRaceTrack.TRACK_WIDTH-1, position.x + 1);
        if (canEnter(newX, allRacers) && strafeShift == 0) {
            changeLanes(newX);
            strafeShift = -28;
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
        laneChangeCooldown = changeDelay;
    }

    private boolean canEnter(int newX, List<HorseRacer> allRacers) {
        for (HorseRacer npc : allRacers) {
            if (npc.getPosition().x == newX && npc.getPosition().y == position.y) {
                return false;
            }
        }
        return jumpCounter == 0 &&
                subView.getTrack().getTerrain(new Point(newX, position.y), positionShift).canBeEntered();
    }

    public void possiblyJump() {
        if (jumpCounter == 0 && jumpCooldown == 0) {
            jumpCounter = jumpLength;
            jumpCooldown = jumpCooldownMax;
            if (getCurrentTerrain().getMaximumSpeed(this) == 0 && getCurrentTerrain().canBeEntered()) {
                positionShift += 5; // Jump over a log.
                currentSpeed = 1;
                jumpCounter = jumpLength / 2 + 2;
            }
        }
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setSpeed(int i) {
        this.currentSpeed = i;
    }

    protected HorseRaceTrack getHorseRaceTrack() {
        return subView.getTrack();
    }

    protected int getLaneChangeCooldown() {
        return laneChangeCooldown;
    }

    public int getLap() {
        return lap;
    }

    public void updateStrafeAnimation() {
        if (strafeShift > 0) {
            strafeShift -= 2;
        } else if (strafeShift < 0) {
            strafeShift += 2;
        }
    }
}
