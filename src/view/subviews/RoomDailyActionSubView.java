package view.subviews;


import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import sound.SoundEffects;
import sprites.CombatSpeechBubble;
import util.MyPair;
import view.MyColors;
import view.sprites.AnimationManager;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class RoomDailyActionSubView extends DailyActionSubView {

    public static final Sprite BAR = new Sprite32x32("bar", "world_foreground.png", 0x5A,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN);
    public static final Sprite OPEN_DOOR = new Sprite32x32("door", "world_foreground.png", 0x6E,
            MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.DARK_RED);

    private boolean openDoorAnimation = false;
    private boolean playedOpenDoorSound = false;
    private boolean playCloseDoorSound = false;

    private final List<MyPair<RunOnceAnimationSprite, Point>> otherEffects = new ArrayList<>();

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

    protected final void drawDecorations(Model model) {
        specificDrawDecorations(model);
        for (MyPair<RunOnceAnimationSprite, Point> effect : new ArrayList<>(otherEffects)) {
            model.getScreenHandler().register(effect.first.getName(), effect.second, effect.first, 3);
            if (effect.first.isDone()) {
                otherEffects.remove(effect);
                AnimationManager.unregister(effect.first);
            }
        }
        Sprite overDoor = getOverDoorSprite();
        int doorY = getDoorPosition().y;
        for (int x = 2; x < 5; ++x) {
            Point p = convertToScreen(new Point(x, doorY));
            model.getScreenHandler().register(overDoor.getName(), p, overDoor, 4);
        }
    }

    protected abstract Sprite getOverDoorSprite();

    protected abstract void specificDrawDecorations(Model model);

    protected abstract Point getDoorPosition();

    private void drawDoorWithAnimation(Model model) {
        Point p = convertToScreen(getDoorPosition());
        if (openDoorAnimation &&
                getMovementAnimation().getCurrentPosition().distance(p) < 3.0) {
            if (!playedOpenDoorSound) {
                SoundEffects.playHitWood();
                playedOpenDoorSound = true;
                playCloseDoorSound = true;
            }
            model.getScreenHandler().register(OPEN_DOOR.getName(), p, OPEN_DOOR);
        } else {
            if (playCloseDoorSound) {
                SoundEffects.playHitWood();
                playCloseDoorSound = false;
            }
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

    protected void drawBar(Model model) {
        for (int x = 1; x < 6; ++x) {
            Point drawPos = convertToScreen(new Point(x, 3));
            model.getScreenHandler().register("bar", drawPos, BAR);
        }
    }

    protected void addCallout(int length, Point p) {
        otherEffects.add(new MyPair<>(new TavernSpeechBubble(length), convertToScreen(p)));
    }

    private static class TavernSpeechBubble extends CombatSpeechBubble {
        public TavernSpeechBubble(int lengthOfLine) {
            setAnimationDelay(lengthOfLine / 4);
        }
    }
}
