package model.states.horserace;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.horses.Horse;
import util.MyRandom;
import view.subviews.HorseRacingSubView;

import java.awt.*;
import java.util.List;

public class NPCHorseRacer extends HorseRacer {
    private static final int LOOK_AHEAD_DISTANCE = 4;

    public NPCHorseRacer(int xStart, GameCharacter chara, Horse horse, HorseRacingSubView subView) {
        super(xStart, chara, horse, subView);
    }

    @Override
    protected int getAccelerationDelay() {
        if (getCurrentSpeed() == 0) {
            return 10;
        }
        return 25 - getCharacter().getRankForSkill(Skill.Survival);
    }

    @Override
    protected void autoJump() {
        if (getCharacter().testSkill(Skill.Survival, 4).isSuccessful()) {
            possiblyJump();
        }
    }

    public void steer(List<HorseRacer> allRacers) {
        if (getCurrentSpeed() == 0) {
            if (MyRandom.flipCoin()) {
                possiblyMoveLeft(allRacers);
            } else {
                possiblyMoveRight(allRacers);
            }
        } else if (getLaneChangeCooldown() == 0) {
            Point position = getPosition();
            int positionShift = getYShift();
            int leftResist;
            if (position.x == 0) {
                leftResist = Integer.MAX_VALUE;
            } else {
                leftResist = countResistance(position.x - 1, position.y, positionShift, allRacers);
            }
            int centerResist = countResistance(position.x, position.y, positionShift, allRacers);
            int rightResist;
            if (position.x == HorseRaceTrack.TRACK_WIDTH - 1) {
                rightResist = Integer.MAX_VALUE;
            } else {
                rightResist = countResistance(position.x + 1, position.y, positionShift, allRacers);
            }

            if (leftResist < centerResist && leftResist <= rightResist) {
                possiblyMoveLeft(allRacers);
            } else if (rightResist < centerResist) {
                possiblyMoveRight(allRacers);
            } else if (getHorseRaceTrack().getTerrain(new Point(position.x, position.y + 1), getYShift()).
                    getMaximumSpeed(this) < 3 && getCurrentSpeed() > 1 && positionShift < 16) {
                possiblyJump();
            }
        }
    }

    private int countResistance(int x, int y, int shift, List<HorseRacer> allRaces) {
        int resist = 0;
        for (int count = 1; count < LOOK_AHEAD_DISTANCE + 1; ++count) {

            resist += getHorseRaceTrack().getTerrain(new Point(x, y+count), shift).getResistance(this) * (LOOK_AHEAD_DISTANCE + 1 - count);
            for (HorseRacer r : allRaces) {
                if (r.getPosition().y == y+count && r.getPosition().x == x) {
                    resist += 10 * (10 - count);
                }
            }
        }
        return resist;
    }
}
