package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SilhouetteAppearance;
import model.items.Item;
import model.states.events.TournamentOdds;
import view.sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TournamentSubView extends TopMenuSubView {
    private static final Sprite VERTICAL_LINE = CharSprite.make(0xAC, MyColors.WHITE, MyColors.BROWN, MyColors.BLUE);
    private static final Sprite HORIZONTAL_LINE = CharSprite.make(0xAD, MyColors.WHITE, MyColors.BROWN, MyColors.BLUE);
    private static final Sprite CORNER = CharSprite.make(0xAE, MyColors.WHITE, MyColors.BROWN, MyColors.BLUE);
    private static final CharacterAppearance SIL_APPEARANCE = new SilhouetteAppearance();
    private final SteppingMatrix<GameCharacter> matrix;
    private final Map<GameCharacter, TournamentOdds> odds;
    private Set<GameCharacter> knownFighters = new HashSet<>();
    private int timeLeft = -1;

    public TournamentSubView(List<GameCharacter> fighters, Map<GameCharacter, TournamentOdds> odds) {
        super(2, new int[]{X_OFFSET+3, X_OFFSET+16});
        this.matrix = new SteppingMatrix<>(8, 5);
        this.odds = odds;
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
                    fighters.get(fighters.size() - (i - fightersInBottom) - 1));
        }
    }

    public TournamentSubView(List<GameCharacter> fighters) {
        this(fighters, null);
    }

    private void drawCharacterCard(Model model) {
        Point pos = new Point(X_OFFSET + 4, Y_MAX-10);
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, pos.y, Y_MAX);
        GameCharacter fighter = matrix.getSelectedElement();
        BorderFrame.drawString(model.getScreenHandler(), fighter.getName(), pos.x, pos.y, MyColors.LIGHT_GRAY);
        String raceAndClassString = fighter.getRace().getName() + " " + fighter.getCharClass().getShortName() + " Lvl " + fighter.getLevel();
        BorderFrame.drawString(model.getScreenHandler(), raceAndClassString, pos.x, pos.y+1, MyColors.LIGHT_GRAY);
        if (knownFighters.contains(fighter) || model.getParty().getPartyMembers().contains(fighter)) {
            fighter.getAppearance().drawYourself(model.getScreenHandler(), pos.x, pos.y + 3);
            fighter.getEquipment().drawYourself(model.getScreenHandler(), pos.x, pos.y);
        } else {
            SIL_APPEARANCE.drawYourself(model.getScreenHandler(), pos.x, pos.y + 3);
            model.getScreenHandler().put(pos.x + 8, pos.y + 6, Item.EMPTY_ITEM_SPRITE);
            model.getScreenHandler().put(pos.x + 13, pos.y + 6, Item.EMPTY_ITEM_SPRITE);
            model.getScreenHandler().put(pos.x + 18, pos.y + 6, Item.EMPTY_ITEM_SPRITE);
        }
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
                    AvatarSprite avatar = matrix.getElementAt(x, y).getAvatarSprite();
                    Point pos = makeFighterPos(x, y);
                    avatar.synch();
                    model.getScreenHandler().register(avatar.getName(), pos, avatar);
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
        String extraText = "";
        if (odds != null) {
            extraText = ", Current odds: " + odds.get(matrix.getSelectedElement()).getOddsString();
        }
        return matrix.getSelectedElement().getName() + extraText;
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - TOURNAMENT";
    }

    @Override
    protected void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = matrix.getSelectedPoint();
        Point pos2 = new Point(makeFighterPos(p.x, p.y));
        pos2.y -= 4;
        model.getScreenHandler().register(cursor.getName(), pos2, cursor, 2);
    }

    @Override
    protected void drawInnerArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        if (timeLeft != -1) {
            BorderFrame.drawCentered(model.getScreenHandler(), "Time Left: " + timeLeft + " min", Y_OFFSET + 2,
                    MyColors.WHITE, MyColors.BLUE);
        }
        drawTree(model, matrix.getElementList().size()-1);
        drawFighters(model);
        drawCharacterCard(model);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "Wait 5 Min";
        }
        return "Next Fight";
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    @Override
    protected int getDefaultIndex() {
        return 1;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow();
    }

    public void setSelectedFighterKnown() {
        knownFighters.add(matrix.getSelectedElement());
    }

    public void setFightersAsKnown(List<GameCharacter> fighters) {
        for (GameCharacter fighter : fighters) {
            knownFighters.add(fighter);
        }
    }

    public Point getCursorPosition() {
        Point selected = matrix.getSelectedPoint();
        return convertToScreen(selected.x, selected.y);
    }

    public GameCharacter getSelectedFighter() {
        return matrix.getSelectedElement();
    }

    public boolean isFighterKnown(GameCharacter fighter) {
        return knownFighters.contains(fighter);
    }

    public void setTimeLeft(int i) {
        this.timeLeft = i;
    }
}
