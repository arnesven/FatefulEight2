package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.headquarters.Headquarters;
import model.horses.Horse;
import view.BorderFrame;
import view.MyColors;
import view.sprites.*;
import view.widget.TopText;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.List;

public class HeadquartersSubView extends SubView {

    private static final Sprite SKY_SPRITE = new Sprite32x32("hqsky", "world_foreground.png", 0x72, MyColors.LIGHT_BLUE,
            MyColors.GRAY, MyColors.WHITE, MyColors.CYAN);
    public static final Sprite BLOCKED_SPRITE = new Sprite16x16("blocksprite", "arrows.png", 0x22, MyColors.RED);
    private static final int MATRIX_COLUMNS = 2;
    private static final int MATRIX_ROWS = 6;
    private final SteppingMatrix<GameCharacter> characterMatrix;
    private boolean cursorEnabled = false;
    private Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;

    public HeadquartersSubView(Model model) {
        this.characterMatrix = new SteppingMatrix<>(MATRIX_COLUMNS, MATRIX_ROWS);
        characterMatrix.addElements(model.getParty().getHeadquarters().getCharacters());
    }

    @Override
    protected void drawArea(Model model) {
        drawBackground(model);
        drawResources(model);
        drawCharacters(model);
        drawHorses(model);
    }

    private Point convertToScreen(Point point) {
        return new Point(X_OFFSET + point.x * 4, Y_OFFSET + point.y * 4);
    }

    private void drawHorses(Model model) {
        List<Horse> horses = model.getParty().getHeadquarters().getHorses();

        Point origin = convertToScreen(new Point(4, 2));
        origin.y += 2;
        origin.x += 2;
        Point p = new Point(origin);
        for (Horse h : horses) {
            Sprite mini = h.getMiniSprite();
            model.getScreenHandler().register(mini.getName(), p, mini);
            if (p.x == 0) {
                p = new Point(origin.x, p.y + 2);
            } else {
                p = new Point(p.x - 2, p.y);
            }
        }
    }

    private void drawCharacters(Model model) {
        int row = 3;
        int colStart = 6;
        for (int y = 0; y < characterMatrix.getRows(); ++y) {
            for (int x = 0; x < characterMatrix.getColumns(); ++x) {
                GameCharacter gc = characterMatrix.getElementAt(x, y);
                if (gc != null) {
                    Point p = convertToScreen(new Point(colStart + x, row+y));
                    AvatarSprite avatar = gc.getAvatarSprite();
                    avatar.synch();
                    p.y += 2;
                    model.getScreenHandler().register(avatar.getName(), p, avatar);

                    if (model.getParty().getHeadquarters().isAway(gc)) {
                        Point p2 = new Point(p);
                        p2.x += 1;
                        p2.y += 2;
                        model.getScreenHandler().register(BLOCKED_SPRITE.getName(), p2, BLOCKED_SPRITE, 2);
                    }

                    if (cursorEnabled && gc == characterMatrix.getSelectedElement()) {
                        Point p2 = new Point(p);
                        p2.y -= 4;
                        model.getScreenHandler().register(cursor.getName(), p2, cursor, 3);
                    }
                }
            }
        }
    }

    private void drawResources(Model model) {
        Headquarters hq = model.getParty().getHeadquarters();
        Point p = convertToScreen(new Point(0, 0));
        int fieldWidth = 6;
        int extra = 1;
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth+ "d", hq.getGold()), p.x+extra+1, p.y, MyColors.LIGHT_YELLOW);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.GOLD_ICON_SPRITE);

        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth + "d", hq.getFood()), p.x+extra+1, p.y, MyColors.PEACH);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.FOOD_ICON_SPRITE);

        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth + "d", hq.getIngredients()), p.x+extra+1, p.y, MyColors.LIGHT_GREEN);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.INGREDIENTS_ICON_SPRITE);

        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth + "d", hq.getMaterials()), p.x+extra+1, p.y, MyColors.GRAY);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.MATERIALS_ICON_SPRITE);

        p.y += 1;
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("Horses: %2d/%d         Items: %3d",
                        hq.getHorses().size(),
                        hq.getMaxHorses(),
                        hq.getItems().size()), p.x, p.y, MyColors.WHITE);

        Point p2 = convertToScreen(new Point(5, 3));
        p2.y++;
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("Chars: %2d/%d",
                        hq.getCharacters().size(),
                        hq.getMaxCharacters()), p2.x, p2.y, MyColors.WHITE);
    }

    private void drawBackground(Model model) {
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            VampireFeedingSubView.drawSkyNight(model, new Random(1234));
            VampireFeedingSubView.drawGroundNight(model);
        } else {
            for (int x = 0; x < 8; ++x) {
                Point p = VampireFeedingSubView.convertToScreen(new Point(x, 0));
                model.getScreenHandler().put(p.x, p.y, SKY_SPRITE);
                p.y += 4;
                model.getScreenHandler().put(p.x, p.y, SKY_SPRITE);
            }
            VampireFeedingSubView.drawGroundDay(model);
        }
        Point p = convertToScreen(new Point(6, 2));
        p.x -= 2;
        model.getParty().getHeadquarters().drawYourself(model, p);
    }

    @Override
    protected String getUnderText(Model model) {
        if (cursorEnabled) {
            GameCharacter chara = getSelectedCharacter();
            return chara.getName() + ", " + chara.getRace().getName() + " " +
                    chara.getCharClass().getShortName() + " Lvl " + chara.getLevel();
        }
        return "The headquarters of your party";
    }

    @Override
    protected String getTitleText(Model model) {
        return "HEADQUARTERS";
    }

    public void updateCharacters(Model model) {
        characterMatrix.clear();
        characterMatrix.addElements(model.getParty().getHeadquarters().getCharacters());
    }

    public void selectCharacterEnabled(boolean b) {
        cursorEnabled = b;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (cursorEnabled) {
            return characterMatrix.handleKeyEvent(keyEvent);
        }
        return false;
    }

    public GameCharacter getSelectedCharacter() {
        return characterMatrix.getSelectedElement();
    }
}
