package view;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.TrainingState;
import sprites.CombatCursorSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class TrainingView extends SubView {
    private final TrainingState state;
    private final SteppingMatrix<GameCharacter> matrix;

    private static final Sprite TEMPLE_PINNACLE = makeTempleSprite(0x81);
    private static final Sprite TEMPLE_TOP_LEFT = makeTempleSprite(0x90);
    private static final Sprite TEMPLE_TOP_MID = makeTempleSprite(0x91);
    private static final Sprite TEMPLE_TOP_RIGHT = makeTempleSprite(0x92);
    private static final Sprite TEMPLE_BOT_LEFT = makeTempleSprite(0xA0);
    private static final Sprite TEMPLE_BOT_MID = makeTempleSprite(0xA1);
    private static final Sprite TEMPLE_BOT_RIGHT = makeTempleSprite(0xA2);
    private static final Sprite TEMPLE_OUTER_LEFT = makeTempleSprite(0x83);
    private static final Sprite TEMPLE_OUTER_RIGHT = makeTempleSprite(0x84);
    private static final Sprite SKY = makeTempleSprite(0x72);
    private static final Sprite GRAVEL = new Sprite32x32("gravel", "world_foreground.png", 0xB2, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.PINK);
    private static final Sprite GRASS = new Sprite32x32("grass", "world_foreground.png", 0x72, MyColors.GREEN, MyColors.GRAY, MyColors.PINK);
    private static final Sprite PATH_LEFT = new Sprite32x32("pathleft", "world_foreground.png", 0xB0, MyColors.GREEN, MyColors.GRAY, MyColors.PINK);
    private static final Sprite PATH_RIGHT = new Sprite32x32("pathright", "world_foreground.png", 0xB1, MyColors.GREEN, MyColors.GRAY, MyColors.PINK);
    private static final Sprite WALL = new Sprite32x32("wall", "world_foreground.png", 0xA3, MyColors.GREEN, MyColors.BLACK, MyColors.RED);
    private static final Sprite WALL_TOP = new Sprite32x32("walltop", "world_foreground.png", 0x93, MyColors.GREEN, MyColors.BLACK, MyColors.RED);


    private static Sprite makeTempleSprite(int i) {
        return new Sprite32x32("templesprite" + i, "world_foreground.png", i, MyColors.CYAN, MyColors.BLACK, MyColors.RED, MyColors.LIGHT_YELLOW);
    }

    public TrainingView(TrainingState state, SteppingMatrix<GameCharacter> matrix) {
        this.state = state;
        this.matrix = matrix;
    }

    @Override
    protected void drawArea(Model model) {
        drawBackground(model);
        drawLessons(model);
        drawMatrix(model);
        drawCursor(model);
    }

    private void drawAt(Model model, int x, int y, Sprite sprite) {
        Point p = convertToScreen(x, y);
        model.getScreenHandler().put(p.x, p.y, sprite);
    }

    private void drawBackground(Model model) {

        Sprite[][] arr = new Sprite[][]{{SKY, SKY, SKY, SKY, SKY, TEMPLE_PINNACLE, SKY, SKY},
                {SKY, SKY, SKY, SKY, TEMPLE_TOP_LEFT, TEMPLE_TOP_MID, TEMPLE_TOP_RIGHT, SKY},
                {SKY, SKY, SKY, SKY, TEMPLE_BOT_LEFT, TEMPLE_TOP_MID, TEMPLE_BOT_RIGHT, SKY},
                {SKY, SKY, SKY, TEMPLE_OUTER_LEFT, TEMPLE_TOP_MID, TEMPLE_BOT_MID, TEMPLE_TOP_MID, TEMPLE_OUTER_RIGHT},
                {GRASS, GRASS, GRASS, GRASS, PATH_LEFT, PATH_RIGHT, GRASS, WALL_TOP},
                {GRAVEL, GRAVEL, GRAVEL, GRAVEL, GRASS, GRASS, GRASS, WALL},
                {GRAVEL, GRAVEL, GRAVEL, GRAVEL, GRASS, GRASS, GRASS, WALL},
                {GRAVEL, GRAVEL, GRAVEL, GRAVEL, GRASS, GRASS, GRASS, WALL},
                {TEMPLE_TOP_MID, TEMPLE_TOP_MID, TEMPLE_TOP_MID, TEMPLE_TOP_MID, TEMPLE_TOP_MID, TEMPLE_TOP_MID, TEMPLE_TOP_MID, TEMPLE_TOP_MID}
        };
        for (int y = 0; y < arr.length; ++y) {
            for (int x = 0; x < arr[0].length; ++x) {
                drawAt(model, x, y, arr[y][x]);
            }
        }
    }

    private void drawLessons(Model model) {
        List<Skill> lessons = state.getLessons();
        String[] levels = new String[]{"Expert", "Advanced", "Novice"};
        for (int i = 0; i < 3; ++i) {
            BorderFrame.drawString(model.getScreenHandler(), levels[i], X_OFFSET + 1, Y_OFFSET + (i+1)*4+1, MyColors.BLACK, MyColors.CYAN);
            BorderFrame.drawString(model.getScreenHandler(), lessons.get(i).getName(), X_OFFSET + 1, Y_OFFSET + (i+1)*4+2, MyColors.BLACK, MyColors.CYAN);
        }
        for (int i = 3; i < 6; ++i) {
            BorderFrame.drawString(model.getScreenHandler(), levels[i-3], X_OFFSET + 18, 4 + Y_OFFSET + (i+1)*4+1, MyColors.BLACK, MyColors.GREEN);
            BorderFrame.drawString(model.getScreenHandler(), lessons.get(i).getName(), X_OFFSET + 18, 4 + Y_OFFSET + (i+1)*4+2, MyColors.BLACK, MyColors.GREEN);
        }
    }

    private void drawMatrix(Model model) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (matrix.getElementAt(x, y) != null) {
                    Point p = convertToScreen(x, y);
                    GameCharacter ch = matrix.getElementAt(x, y);
                    model.getScreenHandler().register(ch.getAvatarSprite().getName(), p, ch.getAvatarSprite());
                }
            }
        }
    }

    private void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = new Point(matrix.getSelectedPoint());
        p = convertToScreen(p.x, p.y);
        p.y -= 4;
        model.getScreenHandler().register("trainingcursor", p, cursor, 2);
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + x*4, Y_OFFSET + y*4);
    }

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return "TEMPLE - TRAINING";
    }
}
