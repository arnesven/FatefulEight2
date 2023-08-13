package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import sprites.CombatCursorSprite;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class TournamentSubView extends AvatarSubView {
    private static final Sprite VERTICAL_LINE = CharSprite.make(0xAC, MyColors.WHITE, MyColors.BROWN, MyColors.BLUE);
    private static final Sprite HORIZONTAL_LINE = CharSprite.make(0xAD, MyColors.WHITE, MyColors.BROWN, MyColors.BLUE);
    private static final Sprite CORNER = CharSprite.make(0xAE, MyColors.WHITE, MyColors.BROWN, MyColors.BLUE);
    private final SteppingMatrix<GameCharacter> matrix;

    public TournamentSubView(List<GameCharacter> fighters) {
        this.matrix = new SteppingMatrix<>(8, 5);
        int treeHeight = 3;
        if (fighters.size() <= 4) {
            treeHeight--;
        }
        if (fighters.size() <= 2) {
            treeHeight--;
        }
        int missing = (int)Math.pow(2, treeHeight) - fighters.size();
        int fightersInBottom = fighters.size() - missing;
        System.out.println("Fighters in bottom: "+ fightersInBottom);
        for (int i = 0; i < fightersInBottom; ++i) {
            matrix.addElement(i,
                    3 - treeHeight,
                    fighters.get(i));
        }
        int slotsOneLevelUp = (int)Math.pow(2, treeHeight-1);
        for (int i = fightersInBottom; i < fighters.size(); ++i) {
            matrix.addElement(
                     slotsOneLevelUp - (i - fightersInBottom) - 1,
                    3 - treeHeight + 1,
                    fighters.get(i));
        }
    }

    @Override
    protected void specificDrawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        drawTree(model, matrix.getElementList().size()-1);
        drawFighters(model);
    }

    private void drawTree(Model model, int fightsRemaining) {
        int firstLevelFights = Math.max(0, fightsRemaining - 3);
        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < firstLevelFights*2; ++i) {
                drawSymbol(model, 0, 2, 1 + i * 4, j, VERTICAL_LINE);
            }
        }

        for (int j = 0; j < firstLevelFights; ++j) {
            for (int i = 0; i < 3; ++i) {
                drawSymbol(model, 0, 2,  2 + i + j * 8, 3, HORIZONTAL_LINE);
            }
            drawSymbol(model, 0, 2, 5 + 8 * j, 3, CORNER);
        }

        int secondLevelFights = fightsRemaining > 2 ? 2 : (fightsRemaining == 2 ? 1 : 0);
        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < secondLevelFights*2; ++i) {
                drawSymbol(model, 0, 3, 3 + i * 8, j, VERTICAL_LINE);
            }
        }

        for (int j = 0; j < secondLevelFights; ++j) {
            for (int i = 0; i < 7; ++i) {
                drawSymbol(model, 1, 3, i + j * 16, 3, HORIZONTAL_LINE);
            }
            drawSymbol(model, 1, 3, 7 + 16 * j, 3, CORNER);
        }

        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < 2; ++i) {
                drawSymbol(model, 0, 4, 7 + i * 16, j, VERTICAL_LINE);
            }
        }

        for (int i = 0; i < 15; ++i) {
            drawSymbol(model, 2, 4, i, 3, HORIZONTAL_LINE);
        }
        drawSymbol(model, 2, 4, 15, 3, CORNER);

        for (int j = 0; j < 4; ++j) {
            drawSymbol(model, 0, 5, 15, j, VERTICAL_LINE);
        }
    }

    private void drawSymbol(Model model, int x, int y, int xOff, int yOff, Sprite sprite) {
        Point start = convertToScreen(x, y);
        start.x += xOff;
        start.y += yOff;
        model.getScreenHandler().put(start.x, start.y, sprite);
    }

    private void drawFighters(Model model) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (matrix.getElementAt(x, y) != null) {
                    Sprite avatar = matrix.getElementAt(x, y).getAvatarSprite();
                    Point pos = makeFighterPos(x, y);
                    model.getScreenHandler().register(avatar.getName(), pos, avatar);
                    if (matrix.getSelectedPoint().x == x && matrix.getSelectedPoint().y == y) {
                        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
                        Point pos2 = new Point(pos);
                        pos2.y -= 4;
                        model.getScreenHandler().register(cursor.getName(), pos2, cursor, 2);
                    }
                }
            }
        }
    }

    private Point makeFighterPos(int x, int y) {
        Point pos = convertToScreen(x, y+1);
        //if (y % 2 == 1) {
        //    pos.x += 2;
        //}
        int dist = (int)Math.pow(2, y) - 1;
        pos.x += dist*4*x + dist*2;
        return pos;
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
        return "EVENT - TOURNAMENT";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }
}
