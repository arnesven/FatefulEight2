package view.subviews;


import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import sound.SoundEffects;
import util.MyPair;
import view.MyColors;
import view.combat.GrassCombatTheme;
import view.sprites.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class RoomDailyActionSubView extends DailyActionSubView {

    public static final Sprite BAR = new Sprite32x32("bar", "world_foreground.png", 0x5A,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN);
    public static final Sprite OPEN_DOOR = new Sprite32x32("door", "world_foreground.png", 0x6E,
            MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.DARK_RED);

    private static final Sprite[] STREET_DAY = makeStreetSprites(MyColors.GRAY, MyColors.GREEN, MyColors.LIGHT_GREEN);
    private static final Sprite STREET_PATH_DAY = new Sprite32x32("innstreetpathday", "world_foreground.png", 0xE9,
            MyColors.GRAY, MyColors.GREEN, MyColors.LIGHT_GREEN, MyColors.DARK_GRAY);
    private static final Sprite[] STREET_NIGHT = makeStreetSprites(MyColors.DARK_GRAY, MyColors.DARK_GREEN, MyColors.TAN);
    private static final Sprite STREET_PATH_NIGHT = new Sprite32x32("innstreetnight", "world_foreground.png", 0xE9,
            MyColors.DARK_GRAY, MyColors.DARK_GREEN, MyColors.TAN, MyColors.DARK_GRAY);

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
        if (openDoorAnimation && getMovementAnimation() != null) {
            if (getMovementAnimation().getCurrentPosition().distance(p) < 3.0) {
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
    }

    public void animateMovement(Model model, Point from, Point to) {
        if (insideToOutside(from, to) || insideToOutside(to, from)) {
            Point doorPos = getDoorPosition();
            openDoorAnimation = true;
            playedOpenDoorSound = false;
            Point above = new Point(doorPos);
            above.y--;
            super.animateMovement(model, from, above);
            super.animateMovement(model, above, doorPos);
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
        otherEffects.add(new MyPair<>(new NonCombatSpeechBubble(length), convertToScreen(p)));
    }

    private static Sprite[] makeStreetSprites(MyColors color1, MyColors color2, MyColors color3) {
        Sprite[] sprites = new Sprite[3];
        for (int x = 0; x < 3; ++x) {
            sprites[x] = new Sprite32x32("street", "world_foreground.png", 0xE6 + x, color1, color2, color3, MyColors.DARK_GRAY);
        }
        return sprites;
    }

    protected void drawStreetOrPath(Model model, Random random, boolean inTown, int row, int width) {
         for (int col = 0; col < width; col++) {
            Point p = convertToScreen(new Point(col, row));
            Sprite spr;
            if (col == 3) {
                spr = model.getTimeOfDay() == TimeOfDay.EVENING ? STREET_PATH_NIGHT : STREET_PATH_DAY;
            } else {
                if (inTown) {
                    if (model.getTimeOfDay() == TimeOfDay.EVENING) {
                        spr = STREET_NIGHT[random.nextInt(STREET_NIGHT.length)];
                    } else {
                        spr = STREET_DAY[random.nextInt(STREET_DAY.length)];
                    }
                 } else {
                    if (model.getTimeOfDay() == TimeOfDay.EVENING) {
                        spr = GrassCombatTheme.darkGrassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    } else {
                        spr = GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    }
                }
            }
            model.getScreenHandler().put(p.x, p.y, spr);
        }
    }
}
