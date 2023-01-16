package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TavernDailyActionState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CastleSubView extends DailyActionSubView {

    public static final MyColors GROUND_COLOR = MyColors.TAN;
    private static final Sprite ground = new Sprite32x32("castleGround", "world_foreground.png", 0x02,
            GROUND_COLOR, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);
    private static final Sprite groundNoPath = new Sprite32x32("castleGroundNoPath", "world_foreground.png", 0x72,
            GROUND_COLOR, MyColors.GRAY, MyColors.LIGHT_GRAY);
    private static final Sprite towerUL = new Sprite32x32("towerUL", "world_foreground.png", 0x16,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite horiWall = new Sprite32x32("castleWall", "world_foreground.png", 0x17,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
   private static final Sprite towerUR = new Sprite32x32("towerUR", "world_foreground.png", 0x1B,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite vertiWall = new Sprite32x32("castleVWall", "world_foreground.png", 0x26,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite castleLL = new Sprite32x32("castleLL", "world_foreground.png", 0x38,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite castleLC = new Sprite32x32("castleLC", "world_foreground.png", 0x39,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite castleLR = new Sprite32x32("castleLR", "world_foreground.png", 0x3A,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite towerLL = new Sprite32x32("towerLL", "world_foreground.png", 0x46,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite towerLR = new Sprite32x32("towerLL", "world_foreground.png", 0x4B,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);
    private static final Sprite gate = new Sprite32x32("castleGate", "world_foreground.png", 0x49,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, GROUND_COLOR);


    private Sprite[][] rows;

    private static Map<String, Sprite> castleSprites = new HashMap<>();

    private final String placeName;
    private final MyColors color;

    public CastleSubView(AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix, String placeName, MyColors color) {
        super(advancedDailyActionState, matrix);
        this.placeName = placeName;
        this.color = color;

        rows = new Sprite[][]{
                new Sprite[]{groundNoPath, groundNoPath, makeSprite(0x08, color), makeSprite(0x09, color), makeSprite(0xA, color), groundNoPath, groundNoPath, groundNoPath},
                new Sprite[]{towerUL, horiWall, makeSprite(0x18, color), makeSprite(0x19, color), makeSprite(0x1A, color),  horiWall, horiWall, towerUR},
                new Sprite[]{vertiWall, ground, makeSprite(0x28, color), makeSprite(0x29, color), makeSprite(0x2A, color), ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, castleLL, castleLC, castleLR, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{vertiWall, ground, ground, ground, ground, ground, ground, vertiWall},
                new Sprite[]{towerLL, horiWall, horiWall, gate, horiWall, horiWall, horiWall, towerLR},
                new Sprite[]{ground, ground, ground, ground, ground, ground, ground, ground},
        };
    }

    private Sprite makeSprite(int i, MyColors color) {
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
        for (int row = 0; row < rows.length; ++row ) {
            for (int i = 0; i < rows[0].length; ++i) {
                Point p = convertToScreen(new Point(i, row-1));
                if (row == 0) {
                    p.y += 2;
                }
                model.getScreenHandler().put(p.x, p.y, rows[row][i]);
            }
        }
    }

    @Override
    protected String getPlaceType() {
        return "CASTLE";
    }

    @Override
    public void animateMovement(Model model, Point from, Point to) {
        Point gatePosition = new Point(3, 7);
        if (insideToOutside(from, to) || insideToOutside(to, from)) {
            super.animateMovement(model, from, gatePosition);
            super.animateMovement(model, gatePosition, to);
        } else {
            super.animateMovement(model, from, to);
        }
    }

    private boolean insideToOutside(Point from, Point to) {
        return to.y != AdvancedDailyActionState.TOWN_MATRIX_ROWS-1 &&
                from.y == AdvancedDailyActionState.TOWN_MATRIX_ROWS-1;
    }
}
