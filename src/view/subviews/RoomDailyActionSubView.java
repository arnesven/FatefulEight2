package view.subviews;


import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.tavern.TavernDailyActionState;
import sound.SoundEffects;
import view.sprites.Sprite;

import java.awt.*;
import java.util.Random;

import static view.subviews.TownHallSubView.DOOR;
import static view.subviews.TownHallSubView.OPEN_DOOR;

public abstract class RoomDailyActionSubView extends DailyActionSubView {

    private boolean openDoorAnimation = false;
    private boolean playedOpenDoorSound = false;
    private boolean playCloseDoorSound = false;

    public RoomDailyActionSubView(AdvancedDailyActionState state,
                                  SteppingMatrix<DailyActionNode> matrix) {
        super(state, matrix);
    }

    @Override
    protected final void drawBackground(Model model) {
        Random random = new Random(9847);
        drawBackgroundRoom(model, random);
        drawDoorWithAnimation(model);
        drawDecorations(model);
        drawParty(model);
    }

    protected abstract void drawBackgroundRoom(Model model, Random random);

    protected abstract void drawParty(Model model);

    protected abstract void drawDecorations(Model model);

    protected abstract Point getDoorPosition();

    protected abstract Sprite getOpenDoorSprite();

    protected abstract Sprite getClosedDoorSprite();

    private void drawDoorWithAnimation(Model model) {
        Point p = convertToScreen(getDoorPosition());
        if (openDoorAnimation &&
                getMovementAnimation().getCurrentPosition().distance(p) < 3.0) {
            if (!playedOpenDoorSound) {
                SoundEffects.playHitWood();
                playedOpenDoorSound = true;
                playCloseDoorSound = true;
            }
            model.getScreenHandler().put(p.x, p.y, getOpenDoorSprite());
        } else {
            if (playCloseDoorSound) {
                SoundEffects.playHitWood();
                playCloseDoorSound = false;
            }
            model.getScreenHandler().put(p.x, p.y, getClosedDoorSprite());
        }
    }

    public void animateMovement(Model model, Point from, Point to) {
        if (insideToOutside(from, to) || insideToOutside(to, from)) {
            Point doorPos = getDoorPosition();
            openDoorAnimation = true;
            playedOpenDoorSound = false;
            super.animateMovement(model, from, doorPos);
            Point below = new Point(doorPos);
            below.y++;
            super.animateMovement(model, doorPos, below);
            openDoorAnimation = false;
            super.animateMovement(model, below, to);
        } else {
            super.animateMovement(model, from, to);
        }
    }


    private boolean insideToOutside(Point from, Point to) {
        return to.y >= AdvancedDailyActionState.TOWN_MATRIX_ROWS-2 &&
                from.y < AdvancedDailyActionState.TOWN_MATRIX_ROWS-2;
    }

}
