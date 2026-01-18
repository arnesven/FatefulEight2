package model.mainstory.jungletribe;

import model.Model;
import model.SteppingMatrix;
import view.MyColors;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;
import view.sprites.Sprite64x32;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SudoqPuzzleSubView extends PyramidPuzzleSubView {

    private static Sprite REGION_LEFT = makeRegionSprite(false);
    private static Sprite REGION_RIGHT = makeRegionSprite(false);
    private final SteppingMatrix<SudoqSymbol> matrix;
    private SudoqSymbol cursorSymbol = null;
    private int blinkCount = 0;

    public SudoqPuzzleSubView(SteppingMatrix<SudoqSymbol> matrix) {
        this.matrix = matrix;
    }


    @Override
    protected void specificDrawArea(Model model) {
        blinkCount++;
        drawRegions(model);
        drawSymbols(model);
    }

    private void drawRegions(Model model) {
        for (int y = 0; y < 3; ++y) {
            Point p = convertToScreen(2, 3 + y);
            model.getScreenHandler().register(REGION_LEFT.getName(), p, REGION_LEFT);
            p = convertToScreen(4, 3 + y);
            p.x -= 2;
            model.getScreenHandler().register(REGION_RIGHT.getName(), p, REGION_RIGHT);
        }
    }

    private void drawSymbols(Model model) {
        for (int row = 0; row < matrix.getRows(); ++row) {
            for (int col = 0; col < matrix.getColumns(); ++col) {
                Point p = convertToScreen(col, row);
                if (row != SudoqPuzzleEvent.PUZZLE_SIZE) {
                    p.x = p.x / 2 + 21;
                    p.y = p.y / 2 + 14;
                } else {
                    p.x += 3;
                    p.y += 3;
                }
                matrix.getElementAt(col, row).drawYourself(model.getScreenHandler(), p);
                if (matrix.getSelectedPoint().x == col && matrix.getSelectedPoint().y == row) {
                    Point p2 = new Point(p);
                    if (cursorSymbol != null && matrix.getSelectedPoint().y < SudoqPuzzleEvent.PUZZLE_SIZE) {
                        if ((blinkCount / 10) % 2 == 0) {
                            cursorSymbol.drawYourself(model.getScreenHandler(), p2);
                        }
                    } else {
                        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
                        p2.y -= 3;
                        p2.x -= 1;
                        model.getScreenHandler().register(cursor.getName(), p2, cursor);
                    }
                }
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        if (matrix.getSelectedPoint().y == matrix.getRows()-1) {
            if (cursorSymbol != null && matrix.getSelectedElement().getValue() == cursorSymbol.getValue()) {
                return "Deselect " + matrix.getSelectedElement().getColor();
            }
            return "Select " + matrix.getSelectedElement().getColor();
        }
        if (cursorSymbol == null) {
            if (!matrix.getSelectedElement().isPreset() && matrix.getSelectedElement().getValue() != 0) {
                return "Remove";
            }
            return "";
        }
        return "Place " + cursorSymbol.getColor();
    }

    @Override
    protected String getTitleText(Model model) {
        return "PUZZLE SUDOQ";
    }

    private static Sprite makeRegionSprite(boolean flipped) {
        Sprite s = new Sprite64x32("sudoqregion", "quest.png", 0xF3,
                MyColors.LIGHT_GRAY, MyColors.BLUE, MyColors.BLUE, MyColors.BLUE);
        if (flipped) {
            s.setRotation(180.0);
        }
        return s;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (matrix.handleKeyEvent(keyEvent)) {
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public void setSelectedSymbol(SudoqSymbol currentSymbol) {
        this.cursorSymbol = currentSymbol;
    }
}
