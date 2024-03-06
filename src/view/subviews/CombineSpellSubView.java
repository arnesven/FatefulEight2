package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.spells.CombineSpell;
import model.items.spells.Spell;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class CombineSpellSubView extends TopMenuSubView {
    private static final int COLUMN_WIDTH = 15;
    private final List<Spell> allSpells;
    private final SteppingMatrix<Spell> matrix;
    private final int maxSpells;
    private int combinedSpells = 0;

    public CombineSpellSubView(int maxSpells, List<Spell> availableSpells) {
        super(2, new int[]{X_OFFSET+7, X_OFFSET+20});
        this.maxSpells = maxSpells;
        this.allSpells = availableSpells;
        this.matrix = new SteppingMatrix<>(2, availableSpells.size());
        int row = 0;
        for (Spell sp : availableSpells) {
            matrix.addElement(0, row++, sp);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        Spell sp = matrix.getSelectedElement();
        return sp.getName() + ", " + sp.getSkill().getName() + " " + sp.getDifficulty() + ", HP " + sp.getHPCost();
    }

    @Override
    protected String getTitleText(Model model) {
        return "COMBINE SPELLS";
    }

    @Override
    protected void drawCursor(Model model) {
        if (matrix.getSelectedPoint() != null) {
            Sprite cursor = ArrowSprites.RIGHT;
            Point p = convertToScreen(matrix.getSelectedPoint());
            p.x -= 1;
            model.getScreenHandler().register("combinecursor", p, cursor, 2);
        }
    }

    @Override
    protected void drawInnerArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        drawLimit(model);
        drawSpells(model);
        drawInfo(model);
    }

    private void drawLimit(Model model) {
        MyColors fgColor = MyColors.WHITE;
        if (combinedSpells > maxSpells || combinedSpells == 0) {
            fgColor = MyColors.LIGHT_RED;
        }
        BorderFrame.drawString(model.getScreenHandler(), combinedSpells + "/" + maxSpells,
                X_OFFSET + COLUMN_WIDTH + 8, Y_OFFSET + 3, fgColor, MyColors.BLUE);
    }

    private void drawSpells(Model model) {
        for (Spell sp : matrix.getElementList()) {
            Point p = convertToScreen(sp);

            String name = sp.getName().substring(0, Math.min(COLUMN_WIDTH, sp.getName().length()));
            BorderFrame.drawString(model.getScreenHandler(), name, p.x, p.y, convertColor(sp.getColor()), MyColors.BLUE);
        }
    }

    private MyColors convertColor(MyColors color) {
        if (color == MyColors.BLUE) {
            return MyColors.LIGHT_BLUE;
        }
        if (color == MyColors.RED) {
            return MyColors.LIGHT_RED;
        }
        return color;
    }

    private void drawInfo(Model model) {
        Point p = convertToScreen(new Point(0, matrix.getRows()));
        p.y += 3;
        BorderFrame.drawString(model.getScreenHandler(), "Resulting spell: ", p.x, p.y,
                MyColors.WHITE, MyColors.BLUE);
        List<Spell> combined = getCombinedSpells();
        if (combined.size() > 1) {
            MyPair<Skill, Integer> skillAndInt = CombineSpell.getResultingSkill(combined);
            BorderFrame.drawString(model.getScreenHandler(),
                    skillAndInt.first.getName() + " " + skillAndInt.second, p.x + 1, ++p.y,
                    MyColors.WHITE, MyColors.BLUE);

            int hpCost = CombineSpell.getResultingHealthCost(combined);
            BorderFrame.drawString(model.getScreenHandler(),
                    "HP Cost: " + hpCost, p.x + 1, ++p.y,
                    MyColors.WHITE, MyColors.BLUE);

            if (combinedSpells > maxSpells) {
                ++p.y;
                BorderFrame.drawCentered(model.getScreenHandler(), "Your mastery level is too",
                        ++p.y, MyColors.RED, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(), "low to combine " + combinedSpells + " spells",
                        ++p.y, MyColors.RED, MyColors.BLUE);
            }
        } else {
            ++p.y;
            BorderFrame.drawCentered(model.getScreenHandler(), "At least 2 spells",
                    ++p.y, MyColors.RED, MyColors.BLUE);
            BorderFrame.drawCentered(model.getScreenHandler(), "must be combined",
                    ++p.y, MyColors.RED, MyColors.BLUE);
        }
    }

    public List<Spell> getCombinedSpells() {
        List<Spell> result = new ArrayList<>();
        for (int y = 0; y < matrix.getRows(); ++y) {
            if (matrix.getElementAt(1, y) != null) {
                result.add(matrix.getElementAt(1, y));
            }
        }
        return result;
    }

    private Point convertToScreen(Spell sp) {
        return convertToScreen(matrix.getPositionFor(sp));
    }

    private Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x * COLUMN_WIDTH + 1, Y_OFFSET + p.y + 4);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "DONE";
        }
        return "CANCEL";
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            toggleSelected();
        }
        return matrix.handleKeyEvent(keyEvent);
    }

    private void toggleSelected() {
        Spell sp = matrix.getSelectedElement();
        Point cursor = matrix.getSelectedPoint();
        for (int y = 0; y < matrix.getRows(); y++) {
            if (matrix.getElementAt(1 - cursor.x, y) == null) {
                matrix.remove(sp);
                matrix.addElement(1 - cursor.x, y, sp);
                break;
            }
        }
        if (cursor.x == 0) {
            combinedSpells++;
        } else {
            combinedSpells--;
        }
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow();
    }

    public boolean didCancel() {
        return getTopIndex() == 1;
    }

    public boolean selectionInvalid() {
        return combinedSpells > maxSpells || combinedSpells < 2;
    }
}
