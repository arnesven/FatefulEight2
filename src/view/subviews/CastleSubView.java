package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.tavern.TavernDailyActionState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CastleSubView extends DailyActionSubView {

    public static final MyColors GROUND_COLOR = MyColors.TAN;
    public static final MyColors GROUND_COLOR_NIGHT = MyColors.DARK_BROWN;
    private final Sprite[] gateDay;
    private final Sprite[] gateNight;
    private static List<Point> townPoints;
    private final Sprite[][] rowsDay;
    private final Sprite[][] rowsNight;

    private static final Map<String, Sprite> castleSprites = new HashMap<>();

    private final String placeName;

    public CastleSubView(AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix, String placeName, MyColors color) {
        super(advancedDailyActionState, matrix);
        this.placeName = placeName;


        rowsDay = makeRows(color, GROUND_COLOR);
        rowsNight = makeRows(color, GROUND_COLOR_NIGHT);
        gateDay = makeGate(GROUND_COLOR);
        gateNight = makeGate(GROUND_COLOR_NIGHT);


        townPoints = new ArrayList<>();
        for (int x = 1; x < 7; ++x) {
            for (int y = 1; y < 7; ++y) {
                if (!( 1 < x && x < 5 && y < 3) && x != 3) {
                    townPoints.add(new Point(x, y));
                }
            }
        }
    }

    private Sprite[] makeGate(MyColors groundColor) {
        return new Sprite[]{
                new Sprite32x32("castleGate", "world_foreground.png", 0x79,
                        MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor),
                new Sprite32x32("castleGate", "world_foreground.png", 0x78,
                        MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor)
        };
    }

    public static Sprite[][] makeRows(MyColors color, MyColors groundColor) {
        Sprite ground = new Sprite32x32("castleGround", "world_foreground.png", 0x02,
                groundColor, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);
        Sprite groundNoPath = new Sprite32x32("castleGroundNoPath", "world_foreground.png", 0x72,
                groundColor, MyColors.GRAY, MyColors.LIGHT_GRAY);
        Sprite towerUL = new Sprite32x32("towerUL", "world_foreground.png", 0x16,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite horiWall = new Sprite32x32("castleWall", "world_foreground.png", 0x17,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite towerUR = new Sprite32x32("towerUR", "world_foreground.png", 0x1B,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite vertiWall = new Sprite32x32("castleVWall", "world_foreground.png", 0x26,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite castleLL = new Sprite32x32("castleLL", "world_foreground.png", 0x38,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite castleLC = new Sprite32x32("castleLC", "world_foreground.png", 0x39,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite castleLR = new Sprite32x32("castleLR", "world_foreground.png", 0x3A,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite towerLL = new Sprite32x32("towerLL", "world_foreground.png", 0x46,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite towerLR = new Sprite32x32("towerLL", "world_foreground.png", 0x4B,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite gate = new Sprite32x32("castleGate", "world_foreground.png", 0x49,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, groundColor);
        Sprite[][] rows = new Sprite[][]{
                new Sprite[]{groundNoPath, groundNoPath, makeSprite(0x08, color), makeSprite(0x09, color), makeSprite(0xA, color), groundNoPath, groundNoPath, groundNoPath},
                new Sprite[]{towerUL, horiWall, makeSprite(0x18, color), makeSprite(0x19, color), makeSprite(0x1A, color), horiWall, horiWall, towerUR},
                new Sprite[]{vertiWall, ground, makeSprite(0x28, color), makeSprite(0x29, color), makeSprite(0x2A, color), ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, castleLL, castleLC, castleLR, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{towerLL, horiWall, horiWall, gate, horiWall, horiWall, horiWall, towerLR},
                new Sprite[]{ground, ground, ground, ground, ground, ground, ground, ground},
        };
        return rows;
    }

    private static Sprite makeSprite(int i, MyColors color) {
        String key = color.name() + "x" + i;
        if (castleSprites.containsKey(key)) {
            return castleSprites.get(key);
        }
        Sprite sprite = new Sprite32x32("castle"+key, "world_foreground.png", i,
                MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, color);
        castleSprites.put(key, sprite);
        return sprite;
    }

    @Override
    protected void drawBackground(Model model) {
        Sprite[][] rows = rowsDay;
        Sprite over_wall = gateDay[1];
        Sprite over_gate = gateDay[0];
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            rows = rowsNight;
            over_wall = gateNight[1];
            over_gate = gateNight[0];
        }
        for (int row = 0; row < rows.length; ++row ) {
            for (int i = 0; i < rows[0].length; ++i) {
                Point p = convertToScreen(new Point(i, row-1));
                if (row == 0) {
                    p.y += 2;
                }
                model.getScreenHandler().put(p.x, p.y, rows[row][i]);
            }
        }

        Point p = convertToScreen(new Point(2, 7));
        model.getScreenHandler().register(over_wall.getName(), p, over_wall, 4);
        p = convertToScreen(new Point(3, 7));
        model.getScreenHandler().register(over_gate.getName(), p, over_gate, 4);
        p = convertToScreen(new Point(4, 7));
        model.getScreenHandler().register(over_wall.getName(), p, over_wall, 4);

        Random rnd = new Random(placeName.hashCode());
        for (Point townPoint : townPoints) {
            double TOWN_DENSITY = 0.5;
            if (getMatrix().getElementAt(townPoint.x, townPoint.y) == null &&
                    rnd.nextDouble() > (1.0- TOWN_DENSITY)) {
                p = convertToScreen(townPoint);
                Sprite townHouse = TownSubView.TOWN_HOUSES[rnd.nextInt(TownSubView.TOWN_HOUSES.length)];
                model.getScreenHandler().register(townHouse.getName(), p, townHouse);
            }
        }
    }

    @Override
    protected String getPlaceType() {
        return "CASTLE";
    }

    public void animateMovement(Model model, Point from, Point to) {
        if (insideToOutside(to, from)) {
            Point doorPos = TavernDailyActionState.getDoorPosition();
            super.animateMovement(model, from, doorPos);
            Point below = new Point(doorPos);
            below.y++;
            super.animateMovement(model, doorPos, below);
            super.animateMovement(model, below, to);
        } else if (insideToOutside(from, to)) {
            Point doorPos = TavernDailyActionState.getDoorPosition();
            Point below = new Point(doorPos);
            below.y++;
            super.animateMovement(model, from, below);
            super.animateMovement(model, below, doorPos);
            super.animateMovement(model, doorPos, to);
        } else {
            super.animateMovement(model, from, to);
        }
    }

    private boolean insideToOutside(Point from, Point to) {
        return to.y != AdvancedDailyActionState.TOWN_MATRIX_ROWS-1 &&
                from.y == AdvancedDailyActionState.TOWN_MATRIX_ROWS-1;
    }
}
