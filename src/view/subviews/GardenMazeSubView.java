package view.subviews;

import model.Model;
import model.states.maze.GardenMaze;
import util.Arithmetics;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GardenMazeSubView extends BottomMenuSubView {

    private static final MyColors SKY_COLOR = MyColors.LIGHT_BLUE;
    private static final Sprite8x8[][] WALL_SPRITES = makeWallSprites();
    private static final Sprite8x8[] DIAGONAL_WALL_SKY = WALL_SPRITES[0];
    private static final Sprite8x8[] ANTI_DIAGONAL_WALL_SKY = WALL_SPRITES[1];
    private static final Sprite8x8[] DIAGONAL_WALL_GROUND = WALL_SPRITES[5];
    private static final Sprite8x8[] ANTI_DIAGONAL_WALL_GROUND = WALL_SPRITES[4];
    private static final Sprite8x8[] ANGLED_WALL = WALL_SPRITES[2];
    private static final Sprite8x8[] FACING_WALL = WALL_SPRITES[3];
    private static final Sprite GROUND_SPRITE = new FilledBlockSprite(MyColors.BROWN);
    private static final Sprite SKY_SPRITE = new FilledBlockSprite(SKY_COLOR);
    private static final Sprite8x8[][] CLOUD_SPRITES = makeCloudSprites();
    private static final Sprite[] STATUE_SPRITES = {makeStatueSprite(0, 0, 64, 192),
                                                    makeStatueSprite(2, 1, 32, 96),
                                                    makeStatueSprite(4, 0, 16, 48),
                                                    makeStatueSprite(8, 2, 8, 24),
                                                    makeStatueSprite(8, 5, 8, 16)};


    private static final int DISTANCE_1 = 5;
    private static final int DISTANCE_2 = 13;
    private static final int[] STATUE_HEIGHTS = new int[]{9, 15, 14, 15, 15};
    private final GardenMaze maze;

    private int currentFacing;
    private final Point currentPoint;
    private List<Boolean> leftWall;
    private List<Boolean> rightWall;
    private int hallDistance;
    private int height = Y_MAX - Y_OFFSET - 4;
    private int width = X_MAX - X_OFFSET;

    //                                              Gap sizes  3   2   2   1   1
    private static final int[] RANGE_LOWER_BOUNDS = new int[]{ 0,  5,  9, 12, 14};
    private static final int[] RANGE_UPPER_BOUNDS = new int[]{ 2,  6, 10, 12, 14};
    private int statueDistance = -1;
    private int yShift = 0;

    public GardenMazeSubView(GardenMaze maze, Point currentPoint) {
        super(2, new int[]{X_OFFSET + 5, X_OFFSET + 17});
        leftWall  = new ArrayList<>(List.of(true, true, true, true, true));
        rightWall = new ArrayList<>(List.of(true, true, true, true, true));
        this.hallDistance = leftWall.size();
        this.maze = maze;

        this.currentPoint = currentPoint;
        this.currentFacing = 2;
        maze.setPerspective(this, currentPoint.x, currentPoint.y, currentFacing);
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP && hallDistance > 0) {
            maze.goForward(currentPoint, currentFacing);
            maze.setPerspective(this, currentPoint.x, currentPoint.y, currentFacing);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            currentFacing = Arithmetics.decrementWithWrap(currentFacing, 4);
            maze.setPerspective(this, currentPoint.x, currentPoint.y, currentFacing);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentFacing = Arithmetics.incrementWithWrap(currentFacing, 4);
            maze.setPerspective(this, currentPoint.x, currentPoint.y, currentFacing);
            return true;
        }
        return false;
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

    @Override
    protected String getUnderText(Model model) {
        return "In the maze...";
    }

    @Override
    protected String getTitleText(Model model) {
        return "GARDEN MAZE";
    }

    @Override
    protected void drawCursor(Model model) {
        model.getScreenHandler().put(X_OFFSET+13, Y_MAX-2, ArrowSprites.RIGHT_BLACK_BLINK);
    }

    @Override
    protected boolean showArrowWhileNotInBorderMenu() {
        return false;
    }

    @Override
    protected void drawInnerArea(Model model) {
        if (getBorderIndex() == 0) {
            maze.drawMap(model.getScreenHandler(), X_OFFSET, Y_OFFSET);
        } else {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    Sprite spriteToUse = getSpriteToUse(x, y);
                    int finalX = X_OFFSET + x;
                    int finalY = Y_OFFSET + y;
                    model.getScreenHandler().put(finalX, finalY, spriteToUse);
                }
            }
            if (0 <= statueDistance && statueDistance <= 5) {
                Sprite toUse = STATUE_SPRITES[statueDistance];
                int xShift = toUse.getWidth() == 8 ? -4 : 0;
                model.getScreenHandler().register(toUse.getName(),
                        new Point(X_OFFSET + width/2 - toUse.getWidth()/16,
                                Y_OFFSET + STATUE_HEIGHTS[statueDistance] + yShift),
                        toUse, 1, xShift, 0);
            }

        }
        BorderFrame.drawString(model.getScreenHandler(), "Move", X_OFFSET + 14, Y_MAX-2, MyColors.WHITE);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "Show map";
        }
        return "Give up";
    }

    private Sprite getSpriteToUse(int x, int y) {
        boolean isOnLeft = x < width / 2;
        int antiX = width - x - 1;

        if (hallDistance < RANGE_UPPER_BOUNDS.length) {
            if (RANGE_UPPER_BOUNDS[hallDistance] < x && x < width - 1 - RANGE_UPPER_BOUNDS[hallDistance]) {
                return getGapSprite(hallDistance, x, y);
            }
        }
        // See all the way down

        int gapIndex = findGapIndex(x, antiX, isOnLeft);
        if (gapIndex >= 0) {
            return getGapSprite(gapIndex, x, y);
        }
        return angledSprite(x, y, antiX);
    }

    private Sprite angledSprite(int x, int y, int antiX) {
        int s = wallSize(x);
        if (y == x) { // Diagonal
            if (x < width / 2) {
                return DIAGONAL_WALL_SKY[s];
            }
            return DIAGONAL_WALL_GROUND[s];
        }

        if (y == antiX) { // Anti-diagonal
            if (x < width / 2) {
                return ANTI_DIAGONAL_WALL_GROUND[s];
            }
            return ANTI_DIAGONAL_WALL_SKY[s];
        }

        if (y > x) {
            if (y < antiX) {
                return ANGLED_WALL[s];
            }
            return GROUND_SPRITE;
        }

        if (y > antiX) {
            return ANGLED_WALL[s];
        }
        return getSkySprite(x, y);
    }

    private Sprite getSkySprite(int x, int y) {
        if (currentFacing >= 1) {
            if (y > 6) {
                return CLOUD_SPRITES[3 + (x+y) % 3][1];
            }
        }
        if (currentFacing >= 2) {
            if (y == 4 && x > width / 2) {
                return CLOUD_SPRITES[3 + (x+y) % 3][0];
            } else if ((y == 3 || y == 5) && x < width / 2) {
                return CLOUD_SPRITES[3 + (x+y) % 3][0];
            }
        }
        if (currentFacing == 3) {
            if ((x / 3) % 2 == 0) {
                if (1 < y && y < 4) {
                    return CLOUD_SPRITES[x % 3][y - 2];
                }
            } else if (y < 2) {
                return CLOUD_SPRITES[x % 3][y];
            }
        }
        return SKY_SPRITE;
    }

    private Sprite getGapSprite(int gapIndex, int x, int y) {
        if (y <= RANGE_UPPER_BOUNDS[gapIndex]) {
            return getSkySprite(x, y);
        }
        if (y >= height - RANGE_UPPER_BOUNDS[gapIndex] - 3) {
            return GROUND_SPRITE;
        }
        return FACING_WALL[sizeForGapIndex(gapIndex)];
    }

    private int findGapIndex(int x, int antiX, boolean isOnLeft) {
        for (int i = 0; i < RANGE_LOWER_BOUNDS.length && i < leftWall.size(); ++i) {
            if (isOnLeft) { // left side
                if (leftWall.get(i) && RANGE_LOWER_BOUNDS[i] <= x && x <= RANGE_UPPER_BOUNDS[i]) {
                    return i;
                }
            } else { // right side
                if (rightWall.get(i) && RANGE_LOWER_BOUNDS[i] <= antiX && antiX <= RANGE_UPPER_BOUNDS[i]) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int sizeForGapIndex(int gapIndex) {
        if (gapIndex < 1) {
            return 0;
        }
        if (gapIndex < 3) {
            return 1;
        }
        return 2;
    }

    private int wallSize(int x) {
        if (x < DISTANCE_1 || x >= width - DISTANCE_1) {
            return 0;
        }
        if (x < DISTANCE_2 || x >= width - DISTANCE_2) {
            return 1;
        }
        return 2;
    }

    private static Sprite8x8[][] makeWallSprites() {
        Sprite8x8[][] result = new Sprite8x8[6][3];
        for (int y = 0; y < result[0].length; ++y) {
            for (int x = 0; x < result.length; ++x) {
                MyColors color1 = x < 4 ? MyColors.DARK_GRAY : MyColors.BROWN;
                result[x][y] = new Sprite8x8("mazewall" + x +"-"+ y, "maze.png", 0x10 * y + x,
                        color1, MyColors.DARK_GREEN, MyColors.GREEN, SKY_COLOR);
            }
        }
        return result;
    }

    private static Sprite8x8[][] makeCloudSprites() {
        Sprite8x8[][] result = new Sprite8x8[6][2];
        for (int y = 0; y < result[0].length; ++y) {
            for (int x = 0; x < result.length; ++x) {
                result[x][y] = new Sprite8x8("mazeclouds" + x +"-"+ y, "maze.png", 0x30 + 0x10 * y + x,
                        SKY_COLOR, MyColors.WHITE, MyColors.GREEN, SKY_COLOR);
            }
        }
        return result;
    }

    @Override
    protected boolean isOnBorderToBottomMenu() {
        return true;
    }

    public void setWalls(List<Boolean> leftSide, List<Boolean> rightSide, int distance, int statueDistance) {
        this.leftWall = leftSide;
        this.rightWall = rightSide;
        this.hallDistance = distance;
        this.statueDistance = statueDistance;
    }


    private static Sprite makeStatueSprite(int col, int row, int width, int height) {
        Sprite spr = new Sprite("statuebig", "statue.png", col, row, width, height);
        spr.setColor1(MyColors.BLACK);
        spr.setColor2(MyColors.WHITE);
        spr.setColor3(MyColors.LIGHT_GRAY);
        spr.setColor4(MyColors.GRAY);
        return spr;
    }
}
