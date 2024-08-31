package view.subviews;

import model.Model;
import util.Arithmetics;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GardenMazeSubView extends BottomMenuSubView {

    private static final Sprite8x8[][] WALL_SPRITES = makeWallSprites();
    private static final Sprite8x8[] DIAGONAL_WALL_SKY = WALL_SPRITES[0];
    private static final Sprite8x8[] ANTI_DIAGONAL_WALL_SKY = WALL_SPRITES[1];
    private static final Sprite8x8[] DIAGONAL_WALL_GROUND = WALL_SPRITES[5];
    private static final Sprite8x8[] ANTI_DIAGONAL_WALL_GROUND = WALL_SPRITES[4];
    private static final Sprite8x8[] ANGLED_WALL = WALL_SPRITES[2];
    private static final Sprite8x8[] FACING_WALL = WALL_SPRITES[3];
    private static final Sprite GROUND_SPRITE = new FilledBlockSprite(MyColors.BROWN);
    private static final Sprite SKY_SPRITE = new FilledBlockSprite(MyColors.LIGHT_BLUE);
    private static final int DISTANCE_1 = 5;
    private static final int DISTANCE_2 = 13;
    private final List<Boolean> leftWall;
    private final List<Boolean> rightWall;
    private int hallDistance;
    private int height = Y_MAX - Y_OFFSET - 4;
    private int width = X_MAX - X_OFFSET;

    //                                              Gap sizes  3   2   2   1   1
    private static final int[] RANGE_LOWER_BOUNDS = new int[]{ 0,  5,  9, 12, 14};
    private static final int[] RANGE_UPPER_BOUNDS = new int[]{ 2,  6, 10, 12, 14};

    public GardenMazeSubView() {
        super(2, new int[]{X_OFFSET + 12});
        leftWall  = new ArrayList<>(List.of(true, true, true, true, true));
        rightWall = new ArrayList<>(List.of(true, true, true, true, true));
        this.hallDistance = leftWall.size();
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        if ('0' < keyEvent.getKeyChar() && keyEvent.getKeyChar() < '6') {
            int index = keyEvent.getKeyChar() - '0' - 1;
            int current = (rightWall.get(index) ? 2 : 0) + (leftWall.get(index) ? 1 : 0);
            current = Arithmetics.incrementWithWrap(current, 4);
            rightWall.set(index, current / 2 == 1);
            leftWall.set(index, current % 2 == 1);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            hallDistance = Arithmetics.incrementWithWrap(hallDistance, leftWall.size() + 1);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            hallDistance = Arithmetics.decrementWithWrap(hallDistance, leftWall.size() + 1);
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
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Sprite spriteToUse = getSpriteToUse(x, y);
                int finalX = X_OFFSET + x;
                int finalY = Y_OFFSET + y;
                model.getScreenHandler().put(finalX, finalY, spriteToUse);
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
        return "Give up";
    }

    private Sprite getSpriteToUse(int x, int y) {
        boolean isOnLeft = x < width / 2;
        int antiX = width - x - 1;

        if (hallDistance < RANGE_UPPER_BOUNDS.length) {
            if (RANGE_UPPER_BOUNDS[hallDistance] < x && x < width - 1 - RANGE_UPPER_BOUNDS[hallDistance]) {
                return getGapSprite(hallDistance, y);
            }
        }
        // See all the way down

        int gapIndex = findGapIndex(x, antiX, isOnLeft);
        if (gapIndex >= 0) {
            return getGapSprite(gapIndex, y);
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
        return SKY_SPRITE;
    }

    private Sprite getGapSprite(int gapIndex, int y) {
        if (y <= RANGE_UPPER_BOUNDS[gapIndex]) {
            return SKY_SPRITE;
        }
        if (y >= height - RANGE_UPPER_BOUNDS[gapIndex] - 3) {
            return GROUND_SPRITE;
        }
        return FACING_WALL[sizeForGapIndex(gapIndex)];
    }

    private int findGapIndex(int x, int antiX, boolean isOnLeft) {
        for (int i = 0; i < RANGE_LOWER_BOUNDS.length; ++i) {
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
                result[x][y] = new Sprite8x8("mazewall" + x +"-"+ y, "maze.png", 0x10 * y + x,
                        MyColors.BROWN, MyColors.DARK_GREEN, MyColors.GREEN, MyColors.LIGHT_BLUE);
            }
        }
        return result;
    }

    @Override
    protected boolean isOnBorderToBottomMenu() {
        return true;
    }
}
