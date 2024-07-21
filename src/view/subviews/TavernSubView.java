package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.races.Race;
import model.states.dailyaction.*;
import sprites.CombatSpeechBubble;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.combat.GrassCombatTheme;
import view.sprites.FirePlaceSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static view.subviews.TownHallSubView.DOOR;

public class TavernSubView extends DailyActionSubView {
    public static final MyColors FLOOR_COLOR = MyColors.DARK_BROWN;

    public static final Sprite WALL = new Sprite32x32("tavernfarwall", "world_foreground.png", 0x44,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.TAN);
    public static final Sprite SIDE_WALL = new Sprite32x32("sidewall", "world_foreground.png", 0x14,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.BROWN);
    public static final Sprite FLOOR = new Sprite32x32("tavernfloor", "combat.png", 0x53,
            MyColors.BROWN, FLOOR_COLOR, MyColors.TAN);
    public static final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN);
    private static final Sprite BAR_UPPER = new Sprite32x32("barupper", "world_foreground.png", 0x15,
            MyColors.BLACK, MyColors.TAN, Race.NORTHERN_HUMAN.getColor(), MyColors.BEIGE);
    private static final Sprite BAR_LOWER = new Sprite32x32("barlower", "world_foreground.png", 0x25,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN);
    private static final Sprite FIREPLACE = new FirePlaceSprite();
    private static final Sprite CHIMNEY = new Sprite32x32("fireplace", "world_foreground.png", 0x68,
            MyColors.DARK_GRAY, MyColors.YELLOW, MyColors.RED, MyColors.GRAY);
    private static final Sprite PLANT = new Sprite32x32("plant", "world_foreground.png", 0x45,
            MyColors.DARK_GRAY, MyColors.BLACK, MyColors.DARK_GREEN, MyColors.CYAN);
    private static final Sprite MERCHANT = new Sprite32x32("merchant", "world_foreground.png", 0x65,
            MyColors.BLACK, MyColors.GOLD, Race.NORTHERN_HUMAN.getColor(), MyColors.RED);
    private static final Sprite SIGN = new Sprite32x32("innsign", "world_foreground.png", 0x64,
            MyColors.BLACK, MyColors.BROWN, MyColors.BEIGE);
    private static final Sprite OVER_DOOR = new Sprite32x32("overdoor", "world_foreground.png", 0x06,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.BLACK);

    private final boolean inTown;
    private final Sprite[] colorGuys;
    private boolean[] showColorGuys;
    private final List<MyPair<RunOnceAnimationSprite, Point>> otherEffects = new ArrayList<>();

    public TavernSubView(AdvancedDailyActionState state,
                         SteppingMatrix<DailyActionNode> matrix, boolean inTown) {
        super(state, matrix);
        this.inTown = inTown;
        colorGuys = new Sprite32x32[]{
                new Sprite32x32("colorguy1", "avatars.png", 0x108,
                        MyColors.BLACK, MyRandom.nextColor(), MyRandom.nextRace().getColor()),
                new Sprite32x32("colorguy2", "avatars.png", 0x108,
                        MyColors.BLACK, MyRandom.nextColor(), MyRandom.nextRace().getColor()),
                new Sprite32x32("colorguy3", "avatars.png", 0x108,
                        MyColors.BLACK, MyRandom.nextColor(), MyRandom.nextRace().getColor())
        };
        do {
            showColorGuys = new boolean[]{MyRandom.flipCoin(), MyRandom.flipCoin(), MyRandom.flipCoin()};
        } while (!showColorGuys[0] && !showColorGuys[1] && !showColorGuys[2]);
    }

    @Override
    protected void drawBackground(Model model) {
        Random random = new Random(9847);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if ((col == 0 || col == 7) && row < 7) {
                    model.getScreenHandler().put(p.x, p.y, SIDE_WALL);
                } else if (0 < row && row < 7) {
                    model.getScreenHandler().put(p.x, p.y, FLOOR);
                } else if (row == 0) {
                    if (col == 6) {
                        model.getScreenHandler().put(p.x, p.y, SIDE_WALL);
                    } else {
                        model.getScreenHandler().put(p.x, p.y, WALL);
                    }
                } else if (row == 7) {
                    model.getScreenHandler().put(p.x, p.y, LOWER_WALL);
                } else {
                    Sprite spr;
                    if (model.getTimeOfDay() == TimeOfDay.EVENING) {
                        spr = GrassCombatTheme.darkGrassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    } else {
                        spr = GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    }
                    model.getScreenHandler().put(p.x, p.y, spr);
                }
            }
        }
        if (!inTown) {
            Point p = convertToScreen(TavernDailyActionState.getDoorPosition());
            model.getScreenHandler().put(p.x, p.y, DOOR);
        }

        drawDecorations(model);
        drawRecruitArea(model);
        drawPartyArea(model, List.of(new Point(6, 6), new Point(6, 5),
                new Point(5, 6), new Point(6, 4), new Point(4, 6),
                new Point(5, 4), new Point(4, 5)));
    }



    private void drawDecorations(Model model) {
        drawForeground(model, 3, 0, CHIMNEY);
        drawForeground(model, 3, 1, FIREPLACE);
        drawForeground(model, 1, 1, PLANT);
        drawForeground(model, 1, 3, BAR_UPPER);
        drawForeground(model, 1, 4, BAR_LOWER);
        drawForeground(model, 5, 5, RecruitNode.TABLE);
        drawForeground(model, 5, 1, RecruitNode.TABLE);
        drawForeground(model, 4, 7, SIGN);
        if (!inTown) {
            drawForeground(model, 4, 1, MERCHANT);
        }
        for (int x = 2; x < 5; ++x) {
            Point p = convertToScreen(new Point(x, 7));
            model.getScreenHandler().register(OVER_DOOR.getName(), p, OVER_DOOR, 4);
        }
        for (MyPair<RunOnceAnimationSprite, Point> effect : new ArrayList<>(otherEffects)) {
            model.getScreenHandler().register(effect.first.getName(), effect.second, effect.first);
            if (effect.first.isDone()) {
                otherEffects.remove(effect);
            }
        }
    }


    private void drawRecruitArea(Model model) {
        if (showColorGuys[0]) {
            drawForeground(model, 1, 5, colorGuys[0]);
        }
        if (showColorGuys[1]) {
            drawForeground(model, 1, 6, colorGuys[1]);
        }
        if (showColorGuys[2]) {
            drawForeground(model, 2, 6, colorGuys[2]);
        }
    }

    @Override
    protected String getPlaceType() {
        if (inTown) {
            return "TAVERN";
        }
        return "INN";
    }

    public void animateMovement(Model model, Point from, Point to) {
        if (insideToOutside(from, to) || insideToOutside(to, from)) {
            Point doorPos = TavernDailyActionState.getDoorPosition();
            super.animateMovement(model, from, doorPos);
            Point below = new Point(doorPos);
            below.y++;
            super.animateMovement(model, doorPos, below);
            super.animateMovement(model, below, to);
        } else {
            super.animateMovement(model, from, to);
        }
    }

    private boolean insideToOutside(Point from, Point to) {
        return to.y != AdvancedDailyActionState.TOWN_MATRIX_ROWS-1 &&
                from.y == AdvancedDailyActionState.TOWN_MATRIX_ROWS-1;
    }

    public void addCalloutAtBartender(int lengthOfLine) {
        otherEffects.add(new MyPair<>(new TavernSpeechBubble(lengthOfLine), convertToScreen(new Point(1, 3))));
    }

    private static class TavernSpeechBubble extends CombatSpeechBubble {
        public TavernSpeechBubble(int lengthOfLine) {
            setAnimationDelay(lengthOfLine / 4);
        }
    }
}
