package view.subviews;

import model.Model;
import model.Party;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.PartyAttitudesDialog;
import view.sprites.AttitudeSprite;
import view.sprites.CalloutSprite;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class OtherPartySubView extends TopMenuSubView {
    private static final Sprite ATTITUDE_COVER = new AttitudeSprite(0x21, MyColors.LIGHT_GRAY);
    private final SteppingMatrix<GameCharacter> matrix;
    private final GameCharacter leader;
    private final HashMap<GameCharacter, Integer> attitudes;
    private Set<GameCharacter> infoRevealed = new HashSet<>();
    private List<MyPair<GameCharacter, CalloutSprite>> callouts = new ArrayList<>();

    public OtherPartySubView(List<GameCharacter> characters,
                             GameCharacter leader,
                             HashMap<GameCharacter, Integer> attitudeMap) {
        super(2, new int[]{X_OFFSET, X_OFFSET+8, X_OFFSET+16, X_OFFSET+25});
        this.matrix = new SteppingMatrix<>(3, 3);
        this.leader = leader;
        this.attitudes = attitudeMap;
        for (int r = 0; r < matrix.getRows(); ++r) {
            for (int c = 0; c < matrix.getColumns(); ++c) {
                int index = r * matrix.getRows() + c;
                if (index < characters.size()) {
                    matrix.addElement(c, r, characters.get(index));
                }
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        GameCharacter selected = matrix.getSelectedElement();
        if (!infoRevealed.contains(selected)) {
            return "???";
        }
        return selected.getFullName() + ", " + selected.getRace().getName() + " " +
                selected.getCharClass().getShortName() + " Lvl " + selected.getLevel();
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - OTHER PARTY";
    }

    @Override
    protected void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = new Point(matrix.getSelectedPoint());
        p.x = X_OFFSET + p.x * 8 + 5;
        p.y = Y_OFFSET + p.y * 8 + 1;
        model.getScreenHandler().register("otherpartycursor", p, cursor, 2);
    }

    @Override
    protected void drawInnerArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        BorderFrame.drawString(model.getScreenHandler(), "TALK:", X_OFFSET + 13,
                Y_OFFSET + 2, MyColors.WHITE, MyColors.BLUE);
        for (int r = 0; r < matrix.getRows(); ++r) {
            for (int c = 0; c < matrix.getColumns(); ++c) {
                int xPos = X_OFFSET + c * 8 + 4;
                int yPos = Y_OFFSET + r * 8 + 4;
                if (matrix.getElementAt(c, r) != null) {
                    matrix.getElementAt(c, r).drawAppearance(model.getScreenHandler(),
                            xPos, yPos);
                    model.getScreenHandler().register("attitude", new Point(xPos, yPos),
                            PartyAttitudesDialog.getSymbolForAttitude(attitudes.get(matrix.getElementAt(c, r))));
                    model.getScreenHandler().register("attitudeCover", new Point(xPos, yPos), ATTITUDE_COVER);
                }
                if (matrix.getElementAt(c, r) == leader) {
                    String leaderIcon = new String(new char[]{0xC3, 0xC4, 0xC5, 0xC6});
                    BorderFrame.drawString(model.getScreenHandler(), leaderIcon, xPos+3,
                            yPos+6, MyColors.WHITE);
                }
            }
        }
        callouts.removeIf((MyPair<GameCharacter, CalloutSprite> pair) -> pair.second.isDone());
        for (MyPair<GameCharacter, CalloutSprite> pair : callouts) {
            if (matrix.getElementList().contains(pair.first)) {
                Point p = matrix.getPositionFor(pair.first);
                int xPos = X_OFFSET + p.x * 8 + 7;
                int yPos = Y_OFFSET + p.y * 8 + 3;
                model.getScreenHandler().register(pair.second.getName(), new Point(xPos, yPos), pair.second);
            }
        }
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        if (i == 0) {
            return MyColors.RED;
        }
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "ATTACK";
        }
        if (i == 1) {
            return "TRADE";
        }
        if (i == 2) {
            return "OFFER";
        }
        return "LEAVE";
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    @Override
    protected int getDefaultIndex() {
        return 3;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getMinimumRow() == matrix.getSelectedPoint().y;
    }

    public GameCharacter getSelectedCharacter() {
        return matrix.getSelectedElement();
    }

    public void revealInfo(GameCharacter who) {
        this.infoRevealed.add(who);
    }

    public void removeFromParty(GameCharacter who) {
        matrix.remove(who);
        this.infoRevealed.remove(who);
        attitudes.remove(who);
    }

    public String addCallout(GameCharacter gc, String s) {
        MyPair<Integer, String> pair = CalloutSprite.getSpriteNumForText(s);
        this.callouts.add(new MyPair<>(gc, new CalloutSprite(pair.first)));
        return pair.second;
    }
}
