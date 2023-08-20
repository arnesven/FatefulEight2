package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.items.Equipment;
import view.sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class ChangeClassSubView extends TopMenuSubView {
    private final SteppingMatrix<GameCharacter> matrix;
    private final HashMap<GameCharacter, GameCharacter> charMap;
    private final CharacterClass targetClass;
    private boolean details = false;
    private Point cursorPos = new Point(1,1);

    public ChangeClassSubView(SteppingMatrix<GameCharacter> matrix, CharacterClass targetClass) {
        super(2, new int[]{X_OFFSET + 4, X_OFFSET+20});
        this.matrix = matrix;
        this.charMap = new HashMap<GameCharacter, GameCharacter>();
        this.targetClass = targetClass;
        for (GameCharacter gc : matrix.getElementList()) {
            GameCharacter wouldBe = gc.copy();
            wouldBe.setEquipment(new Equipment());
            wouldBe.setClass(targetClass);
            charMap.put(gc, wouldBe);
        }
    }

    @Override
    protected void drawInnerArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        if (details) {
            GameCharacter gc = matrix.getSelectedElement();
            if (gc != null) {
                model.getScreenHandler().put(X_OFFSET + 14, Y_OFFSET + 6, ArrowSprites.RIGHT);
                model.getScreenHandler().put(X_OFFSET + 15, Y_OFFSET + 6, ArrowSprites.RIGHT);
                model.getScreenHandler().put(X_OFFSET + 16, Y_OFFSET + 6, ArrowSprites.RIGHT);
                drawCharacterDetails(model, gc, X_OFFSET + 0, Y_OFFSET + 3);
                drawCharacterDetails(model, charMap.get(gc), X_OFFSET + 17, Y_OFFSET + 3);
            }
        } else {
            drawCandidates(model);
        }
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "CANDIDATES";
        }
        return "EXIT";
    }

    private void drawCharacterDetails(Model model, GameCharacter gc, int midX, int row) {
        drawCharacter(model, gc, midX + 3, row);
        row += 10;
        BorderFrame.drawString(model.getScreenHandler(), String.format("Max Health  %3d", gc.getMaxHP()), midX, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Speed       %3d", gc.getSpeed()), midX, row++, MyColors.WHITE, MyColors.BLUE);
        row++;
        for (Skill s : gc.getSkillSet()) {
            BorderFrame.drawString(model.getScreenHandler(), String.format("%-13s%2d", s.getName(),
                    gc.getRankForSkill(s)), midX, row++, MyColors.WHITE, MyColors.BLUE);
        }
        row++;
        BorderFrame.drawString(model.getScreenHandler(), "Armor Class ",
                midX+2, row++,  MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), (gc.getCharClass().canUseHeavyArmor() ? "HEAVY" : "LIGHT"),
                midX+5, row++,  MyColors.WHITE, MyColors.BLUE);
        if (!targetClass.canUseHeavyArmor() &&
                (gc.getEquipment().getClothing().isHeavy() ||
                        (gc.getEquipment().getAccessory() != null && gc.getEquipment().getAccessory().isHeavy()))) {
            BorderFrame.drawString(model.getScreenHandler(), "Some item(s) will be unequipped!", midX, Y_MAX-1,
                    MyColors.RED, MyColors.BLUE);
        }
    }

    @Override
    protected void drawCursor(Model model) {
        if (!details) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = new Point(matrix.getSelectedPoint());
            p.x = X_OFFSET + p.x * 10 + 4;
            p.y = Y_OFFSET + p.y * 11 + 1;
            model.getScreenHandler().register("changeclasscursor", p, cursor, 2);
            this.cursorPos = p;
        }
    }

    private void drawCandidates(Model model) {
        for (int col = 0; col < matrix.getColumns(); ++col) {
            for (int row = 0; row < matrix.getRows(); ++row) {
                GameCharacter gc = matrix.getElementAt(col, row);
                if (gc != null) {
                    int xPos = X_OFFSET + col*10 + 3;
                    int yPos = Y_OFFSET + row*11 + 5;
                    drawCharacter(model, gc, xPos, yPos);
                }
            }
        }
    }

    private void drawCharacter(Model model, GameCharacter gc, int xPos, int yPos) {
        gc.drawAppearance(model.getScreenHandler(), xPos, yPos);
        BorderFrame.drawString(model.getScreenHandler(), gc.getFirstName(), xPos, yPos+7, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), gc.getCharClass().getFullName(), xPos, yPos+8, MyColors.WHITE, MyColors.BLUE);
    }

    @Override
    protected String getUnderText(Model model) {
        if (matrix.getSelectedElement() != null) {
            GameCharacter gc = matrix.getSelectedElement();
            String text = String.format("%s, %s, %s %d", gc.getFullName(), gc.getRace().getName(),
                    gc.getCharClass().getShortName(), gc.getLevel());
            return text;
        }
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "CHANGE CLASS - " + targetClass.getFullName().toUpperCase();
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        if (!details) {
            return matrix.handleKeyEvent(keyEvent);
        }
        return false;
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow() && !details;
    }

    public void toggleDetails() {
        this.details = !details;
    }

    public Point getCursorPosition() {
        return cursorPos;
    }

    public GameCharacter getWouldBe(GameCharacter gc) {
        return charMap.get(gc);
    }
}
