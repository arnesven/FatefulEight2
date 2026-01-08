package model.mainstory.jungletribe;

import model.Model;
import model.SteppingMatrix;
import util.MyRandom;
import view.MyColors;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;
import view.sprites.Sprite32x32;
import view.subviews.SubView;

import java.awt.event.KeyEvent;
import java.util.List;

import java.awt.*;
import java.util.Random;

public class RubiqPuzzleSubView extends SubView {

    private static final Sprite[] BG_SPRITES = new Sprite[]{new Sprite32x32("rubiqwall", "quest.png",0xCD,
            MyColors.DARK_GRAY, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.BEIGE),
            new Sprite32x32("rubiqfullSquare", "quest.png",0xBE,
            MyColors.DARK_GRAY, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.RED)} ;

    private static final Sprite[] NO_GLOW = makeGridSprites(false, false);
    private static final Sprite[] LITTLE_GLOW = makeGridSprites(true, false);
    private static final Sprite[] MORE_GLOW = makeGridSprites(false, true);
    private static final Sprite[] MOST_GLOW = makeGridSprites(true, true);

    private static final Sprite16x16[] CRACK_SPRITES = new Sprite16x16[]{
            new Sprite16x16("cracksprite1", "quest.png", 0x1E0),
            new Sprite16x16("cracksprite2", "quest.png", 0x1E1),
            new Sprite16x16("cracksprite3", "quest.png", 0x1F0),
            new Sprite16x16("cracksprite4", "quest.png", 0x1F1)
    };

    private static final Sprite32x32[] VINES = new Sprite32x32[]{
            new Sprite32x32("vines1", "quest.png", 0xF1,
                    MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.BLUE, MyColors.BEIGE),
            new Sprite32x32("vines2", "quest.png", 0xF2,
                    MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.BLUE, MyColors.BEIGE),
            new Sprite32x32("vines3", "quest.png", 0xF3,
                    MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.BLUE, MyColors.BEIGE),
    };

    private final List<RubiqBall> balls;
    private final List<List<Sprite>> grid;
    private final List<Point> positions;
    private final SteppingMatrix<RubiqButton> buttons;
    private int cracks = 0;
    private boolean shake = false;
    private int shakeCount = 0;

    public RubiqPuzzleSubView(SteppingMatrix<RubiqButton> buttons, List<RubiqBall> balls) {
        this.balls = balls;
        this.grid = List.of(
                List.of(NO_GLOW[0], NO_GLOW[1], NO_GLOW[2], NO_GLOW[3], NO_GLOW[4],
                NO_GLOW[5], NO_GLOW[6], NO_GLOW[7], NO_GLOW[8]),
                List.of(LITTLE_GLOW[0], LITTLE_GLOW[1], LITTLE_GLOW[2], LITTLE_GLOW[3], LITTLE_GLOW[4],
                        LITTLE_GLOW[5], LITTLE_GLOW[6], LITTLE_GLOW[7], LITTLE_GLOW[8]),
                List.of(MORE_GLOW[0], MORE_GLOW[1], MORE_GLOW[2], MORE_GLOW[3], MORE_GLOW[4],
                        MORE_GLOW[5], MORE_GLOW[6], MORE_GLOW[7], MORE_GLOW[8]),
                List.of(MOST_GLOW[0], MOST_GLOW[1], MOST_GLOW[2], MOST_GLOW[3], MOST_GLOW[4],
                        MOST_GLOW[5], MOST_GLOW[6], MOST_GLOW[7], MOST_GLOW[8])
                );
        this.positions = List.of(new Point(3, 1), new Point(3, 2), new Point(3, 3), new Point(3, 4), new Point(3, 5),
                new Point(1, 3), new Point(2, 3), new Point(4, 3), new Point(5, 3));
        this.buttons = buttons;
    }

    @Override
    protected void drawArea(Model model) {
        shakeCount++;
        drawBackground(model);
        drawBalls(model);
        drawButtons(model);
        drawCursor(model);
    }

    private void drawBackground(Model model) {
        Random random = new Random(1234);
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 7; ++x) {
                Point p = convertToScreen(x, y);
                model.getScreenHandler().put(p.x, p.y, BG_SPRITES[0]);
                Point p2 = new Point(p);
                p2.x += random.nextInt(2)*2;
                p2.y += random.nextInt(2)*2;
                Sprite toUse = CRACK_SPRITES[random.nextInt(CRACK_SPRITES.length)];
                if (random.nextInt(10) < cracks*2) {
                    model.getScreenHandler().register(toUse.getName(), p2, toUse);
                }
            }
            Sprite vineSprite;
            if (y == 0) {
                vineSprite = VINES[0];
            } else {
                vineSprite = VINES[y % 2 + 1];
            }
            model.getScreenHandler().register(vineSprite.getName(), convertToScreen(0, y), vineSprite);
        }
    }

    private void drawBalls(Model model) {
        int ballIndex = 0;
        for (int i = 0; i < grid.get(0).size(); ++i) {
            Point p = convertToScreen(positions.get(i).x, positions.get(i).y);
            model.getScreenHandler().put(p.x, p.y, BG_SPRITES[1]);
            Sprite toUse = grid.get(RubiqPuzzleEvent.getGlowLevel(balls, ballIndex)).get(i);
            model.getScreenHandler().register(toUse.getName(), p, toUse);
            if (i != 2) {
                balls.get(ballIndex++).drawYourself(model.getScreenHandler(), p);
            }
        }
    }

    private void drawButtons(Model model) {
        for (int row = 0; row < buttons.getRows(); ++row) {
            for (int col = 0; col < buttons.getColumns(); ++col) {
                if (buttons.getElementAt(col, row) != null) {
                    Point p = matrixToScreen(col, row);
                    buttons.getElementAt(col, row).drawYourself(model.getScreenHandler(), p);
                }
            }
        }
    }

    private void drawCursor(Model model) {
        Point cursorPos = buttons.getSelectedPoint();
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = matrixToScreen(cursorPos.x, cursorPos.y);
        p.y -= 2;
        model.getScreenHandler().register(cursor.getName(), p, cursor, 2);
    }

    private Point matrixToScreen(int col, int row) {
        int shakeExtra = xOffFromShake();
        int xXtra = (col % 2) * 4;
        col /= 2;
        return new Point(X_OFFSET + (col+1) * 12 - 6 + xXtra + shakeExtra, Y_OFFSET + (row+1) * 12 - 6);
    }

    private int xOffFromShake() {
        return shake ? (shakeCount/2 % 3 - 1) : 0;
    }

    private Point convertToScreen(int x, int y) {
        int shakeExtra = xOffFromShake();
        return new Point(X_OFFSET + x * 4 + 2 + shakeExtra, Y_OFFSET + y * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return buttons.getSelectedElement().getText();
    }

    @Override
    protected String getTitleText(Model model) {
        return "PUZZLE RUBIQ";
    }

    private static Sprite[] makeGridSprites(boolean setColor3, boolean setColor4) {
        Sprite[] arr = new Sprite[9];

        MyColors brickColor = MyColors.GRAY;

        arr[0] = new Sprite32x32("up", "quest.png", 0xBF,
                MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.RED : brickColor, setColor4 ? MyColors.RED : brickColor);

        arr[1] = new Sprite32x32("upmid", "quest.png", 0xCF,
                MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.RED : brickColor, setColor4 ? MyColors.RED : brickColor);

        arr[2] = new Sprite32x32("center", "quest.png", 0xDF,
                MyColors.BLACK, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.BEIGE);

        arr[3] = new Sprite32x32("downmid", "quest.png", 0xCF,
                MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.BLUE : brickColor, setColor4 ? MyColors.BLUE : brickColor);

        arr[4] = new Sprite32x32("down", "quest.png", 0xBF,
                MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.BLUE : brickColor, setColor4 ? MyColors.BLUE : brickColor);
        arr[4].setRotation(180);

        arr[5] = new Sprite32x32("left", "quest.png", 0xBF,
               MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.YELLOW : brickColor, setColor4 ? MyColors.YELLOW : brickColor);
        arr[5].setRotation(270);

        arr[6] = new Sprite32x32("leftmid", "quest.png", 0xCF,
                MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.YELLOW : brickColor, setColor4 ? MyColors.YELLOW : brickColor);
        arr[6].setRotation(90);

        arr[7] = new Sprite32x32("rightmid", "quest.png", 0xCF,
                MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.GREEN : brickColor, setColor4 ? MyColors.GREEN : brickColor);
        arr[7].setRotation(90);

        arr[8] = new Sprite32x32("right", "quest.png", 0xBF,
                MyColors.BLACK, MyColors.DARK_GRAY, setColor3 ? MyColors.GREEN : brickColor, setColor4 ? MyColors.GREEN : brickColor);
        arr[8].setRotation(90);

        return arr;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (buttons.handleKeyEvent(keyEvent)) {
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public void setCracks(int cracks) {
        this.cracks = cracks;
    }

    public void setShakeEnabled(boolean b) {
        this.shake = b;
        if (shake) {
            this.shakeCount = 0;
        }
    }
}
