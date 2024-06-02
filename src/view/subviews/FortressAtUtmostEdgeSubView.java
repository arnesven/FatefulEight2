package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.map.CaveHex;
import model.ruins.objects.FatueKeyObject;
import model.states.fatue.FortressAtUtmostEdgeState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FortressAtUtmostEdgeSubView extends DailyActionSubView {
    private static final Sprite32x32[][] backgroundSprites = makeFatueSprites();
    private static final Sprite32x32 STAFF_PIECE_BOTTOM = new Sprite32x32(
            "staffbottom", "fatue_plan.png", 0x28, MyColors.DARK_GRAY, MyColors.GOLD, MyColors.BROWN, MyColors.TAN);
    private static final Sprite32x32 STAFF_PIECE_TOP = new Sprite32x32(
            "stafftop", "fatue_plan.png", 0x08, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.DARK_RED, MyColors.CYAN);
    private static final Sprite32x32 STAFF_PIECE_MIDDLE = new Sprite32x32("staffmiddle", "fatue_plan.png", 0x18,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.DARK_RED, MyColors.BEIGE);
    private static final Map<MyColors, Sprite32x32> KEY_SPRITE_MAP = makeKeySpriteMap();

    private final FortressAtUtmostEdgeState state;

    public FortressAtUtmostEdgeSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix) {
        super(state, matrix);
        this.state = (FortressAtUtmostEdgeState)state;
    }

    @Override
    protected void drawBackground(Model model) {
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 8; ++x) {
                Point p = convertToScreen(new Point(x, y));
                model.getScreenHandler().put(p.x, p.y, backgroundSprites[x][y]);
            }
        }
        drawStaff(model);
        drawKeys(model);
    }

    private void drawStaff(Model model) {
        int pieces = state.getNumberOfPiecesOfStaffFound(model);
        if (pieces > 0) {
            model.getScreenHandler().register(STAFF_PIECE_BOTTOM.getName(),
                    convertToScreen(new Point(7, 6)), STAFF_PIECE_BOTTOM);
        }
        for (int piece = 1; piece < 6 && piece < pieces; ++piece) {
            model.getScreenHandler().register(STAFF_PIECE_MIDDLE.getName(),
                    convertToScreen(new Point(7, 6-piece)), STAFF_PIECE_MIDDLE);
        }
        if (pieces > 6) {
            model.getScreenHandler().register(STAFF_PIECE_TOP.getName(),
                    convertToScreen(new Point(7, 0)), STAFF_PIECE_TOP);
        }
    }

    private void drawKeys(Model model) {
        List<MyColors> keys = state.getKeysColoected(model);
        for (int i = 0; i < keys.size(); i++) {
            Sprite spr = KEY_SPRITE_MAP.get(keys.get(i));
            assert spr != null;
            model.getScreenHandler().register(spr.getName(), convertToScreen(new Point(0, i)), spr);
        }
    }

    @Override
    protected String getPlaceType() {
        return "F.A.T.U.E.";
    }

    public void animateMovement(Model model, Point from, Point to) {
        Point center = FortressAtUtmostEdgeState.getCastleProperPosition();
        if (!from.equals(center) && !to.equals(center) && from.distance(to) > 1.0) {
            super.animateMovement(model, from, center);
            super.animateMovement(model, center, to);
        } else {
            super.animateMovement(model, from, to);
        }
    }

    private static Sprite32x32[][] makeFatueSprites() {
        Sprite32x32[][] result = new Sprite32x32[8][9];
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 8; ++x) {

                MyColors color2 = MyColors.LIGHT_GRAY;
                MyColors color3 = MyColors.DARK_RED;
                if (x == 7 && y == 8) {
                    color2 = MyColors.YELLOW;
                    color3 = CaveHex.GROUND_COLOR;
                } else if (x == 0 && y == 4) {
                    color2 = MyColors.LIGHT_GRAY;
                } else if (x == 0 || x == 7 || y == 7 || y == 8) {
                    color2 = MyColors.WHITE;
                }


                MyColors color4 = MyColors.DARK_GREEN;
                if (y == 0) {
                    color4 = MyColors.GRAY;
                } else if (y == 1 || y == 2) {
                    color4 = MyColors.GOLD;
                } else if ((y == 4 || y == 5) && x > 4) {
                    color4 = MyColors.DARK_PURPLE;
                } else if ((y == 6 || y == 7) && x < 3) {
                    color4 = MyColors.BROWN;
                } else if (x < 2) {
                    color4 = MyColors.BROWN;
                }
                result[x][y] = new FatueSprite(0x10*y + x, color2, color3, color4);
            }
        }
        return result;
    }

    private static class FatueSprite extends Sprite32x32 {
        public FatueSprite(int num, MyColors color1, MyColors color2, MyColors color3) {
            super("fatue"+num, "fatue_plan.png", num, MyColors.BLACK, color1, color2, color3);
        }
    }


    private static Map<MyColors, Sprite32x32> makeKeySpriteMap() {
        Map<MyColors, Sprite32x32> map = new HashMap<>();
        map.put(MyColors.GOLD, new Sprite32x32("fatuekeygold", "fatue_plan.png", 0x38,
                MyColors.DARK_GRAY, MyColors.GOLD, MyColors.YELLOW));
        map.put(MyColors.DARK_RED, new Sprite32x32("fatuekeyred", "fatue_plan.png", 0x38,
                MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.RED));
        return map;
    }
}
