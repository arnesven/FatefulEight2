package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.states.events.BoatsEvent;
import sprites.CombatCursorSprite;
import view.MyColors;
import view.sprites.BoatSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BoatPlacementSubView extends SubView {
    private static final Sprite[] BOAT_SPRITES = new BoatSprite[]{new BoatSprite(0x94),
            new BoatSprite(0xA4), new BoatSprite(0xB4)};
    private static final Sprite BEACH_LEFT = new Sprite32x32("beachleft", "world_foreground.png", 0xB5, MyColors.GREEN,
            MyColors.LIGHT_YELLOW, MyColors.LIGHT_BLUE);
    private static final Sprite BEACH_RIGHT = new Sprite32x32("beachright", "world_foreground.png", 0xA5, MyColors.GREEN,
            MyColors.LIGHT_YELLOW, MyColors.LIGHT_BLUE);
    private static final Sprite BEACH = new Sprite32x32("beach", "world_foreground.png", 0xB3, MyColors.GREEN,
            MyColors.LIGHT_YELLOW, MyColors.LIGHT_BLUE);
    private static final Sprite WATER = new Sprite32x32("water", "world.png", 0x20, MyColors.LIGHT_BLUE, MyColors.BLUE,
            MyColors.PINK, MyColors.BEIGE);
    private final SteppingMatrix<GameCharacter> matrix;
    private final BoatsEvent state;
    private final ArrayList<BoatsEvent.Boat> boats;
    private boolean cursorEnabled = true;

    public BoatPlacementSubView(BoatsEvent state, SteppingMatrix<GameCharacter> matrix, ArrayList<BoatsEvent.Boat> boats) {
        this.matrix = matrix;
        this.state = state;
        this.boats = boats;
    }

    @Override
    protected void drawArea(Model model) {
        drawBackground(model);
        drawMatrix(model);
        if (cursorEnabled) {
            drawCursor(model);
        }
    }

    private void drawBackground(Model model) {
        Random random = new Random(431);
        for (int i = 0; i < matrix.getColumns(); ++i) {
            for (int y = 0; y < 9; ++y) {
                Point p = convertToScreen(i, y);
                if (y == 4) {
                    if (i == 0) {
                        model.getScreenHandler().put(p.x, p.y, BEACH_LEFT);
                    } else if (i == matrix.getColumns()-1) {
                        model.getScreenHandler().put(p.x, p.y, BEACH_RIGHT);
                    } else {
                        model.getScreenHandler().put(p.x, p.y, BEACH);
                    }
                } else if (y < 4) {
                    model.getScreenHandler().put(p.x, p.y,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]);
                } else {
                    model.getScreenHandler().put(p.x, p.y, WATER);
                }
            }
        }

        for (BoatsEvent.Boat boat : boats) {
            for (int y = 0; y < boat.getLength(); ++y) {
                Point p = convertToScreen(boat.getXPos(), y+3);
                int index = y == 0 ? 0 : y == boat.getLength()-1 ? 2 : 1;
                p.y += 1;
                model.getScreenHandler().register(BOAT_SPRITES[index].getName(), p, BOAT_SPRITES[index], 0);
            }
        }
    }

    private void drawMatrix(Model model) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (matrix.getElementAt(x, y) != null) {
                    Point p = matrixPosToScreen(x, y);
                    GameCharacter ch = matrix.getElementAt(x, y);
                    model.getScreenHandler().register(ch.getAvatarSprite().getName(), p, ch.getAvatarSprite(), 1);
                    if (matrix.getSelectedElement() != ch) {
                        ch.getAvatarSprite().synch();
                    }
                }
            }
        }
    }

    private void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = new Point(matrix.getSelectedPoint());
        p = matrixPosToScreen(p.x, p.y);
        p.y -= 4;
        model.getScreenHandler().register("boatscursor", p, cursor, 2);
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + x*4, Y_OFFSET + y*4);
    }

    private Point matrixPosToScreen(int x, int y) {
        return convertToScreen(x, y == 0 ? 1 : y + 2);
    }


    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return "RIVER CROSSING - BOATS";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            state.shiftCharacter(model);
        }
        return matrix.handleKeyEvent(keyEvent);
    }

    public void setCursorEnabled(boolean b) {
        cursorEnabled = b;
    }
}
