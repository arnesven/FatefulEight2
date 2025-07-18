package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.map.WorldHex;
import model.quests.*;
import model.states.QuestState;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.sprites.*;
import view.widget.QuestBackground;

import java.awt.*;
import java.awt.event.KeyEvent;

public class QuestSubView extends AvatarSubView {
    private final QuestState state;
    private final Quest quest;
    private MyColors backgroundColor;
    private Sprite bgSprite;
    private final SteppingMatrix<QuestNode> matrix;
    private static final LoopingSprite WHITE_CURSOR = new QuestCursorSprite(MyColors.WHITE);
    private static final LoopingSprite BLACK_CURSOR = new QuestCursorSprite(MyColors.BLACK);
    private LoopingSprite questCursor;
    private boolean avatarEnabled;
    private boolean edgesEnabled = true;
    private boolean subScenesEnabled = true;

    public QuestSubView(QuestState state, Quest quest, SteppingMatrix<QuestNode> matrix) {
        this.state = state;
        this.quest = quest;
        this.matrix = matrix;
        questCursor = WHITE_CURSOR;
        avatarEnabled = true;
        backgroundColor = quest.getBackgroundColor();
        bgSprite = new FilledBlockSprite(backgroundColor);
    }

    @Override
    protected void specificDrawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        drawBackground(model);
        if (edgesEnabled) {
            drawEdges(model);
        }
        if (subScenesEnabled) {
            drawSubScenes(model, matrix);
        }
        if (quest.clockEnabled()) {
            drawClock(model);
        }
    }

    private void drawClock(Model model) {
        int time = state.getClockTime();
        int min = time / (60*100);
        int sec = (time - min*60*100) / 100;
        int hund = time - min*60*100 - sec*100;
        if (time == 0 && ((System.currentTimeMillis() / 500) % 2) == 0) {
            return;
        }
        BorderFrame.drawString(model.getScreenHandler(), String.format("Time: %02d:%02d:%02d", min, sec, hund), X_OFFSET+8, Y_OFFSET, MyColors.WHITE, MyColors.BLACK);
    }

    private void drawEdges(Model model) {
        for (QuestJunction j : quest.getJunctions()) {
            for (QuestEdge edge : j.getConnections()) {
                edge.drawYourself(model.getScreenHandler(), j, X_OFFSET, Y_OFFSET);
            }
        }
        for (QuestScene qs : quest.getScenes()) {
            for (QuestSubScene qss : qs) {
                if (qss.getSuccessEdge() != null) {
                    qss.getSuccessEdge().drawYourself(model.getScreenHandler(), qss, X_OFFSET, Y_OFFSET);
                }
                if (qss.getFailEdge() != null) {
                    qss.getFailEdge().drawYourself(model.getScreenHandler(), qss, X_OFFSET, Y_OFFSET);
                }
            }
        }
    }

    private void drawBackground(Model model) {
        for (int y = Y_OFFSET; y < Y_MAX; ++y) {
            for (int x = X_OFFSET; x < X_MAX; ++x) {
                model.getScreenHandler().put(x, y, bgSprite);
            }
        }
        for (QuestBackground pair : quest.getBackgroundSprites()) {
            Point converted = convertToScreen(pair.position);
            if (pair.shifted) {
                converted.y -= 2;
            }
            model.getScreenHandler().clearSpace(converted.x, converted.x+4,
                    converted.y, converted.y+4);
            model.getScreenHandler().put(converted.x, converted.y, pair.sprite);
        }
        if (quest.drawTownOrCastleInBackground()) {
            Point converted = convertToScreen(new Point(0, 0));
            converted.y -= 2;
            model.getScreenHandler().clearSpace(converted.x, converted.x+4,
                    converted.y, converted.y+4);
            WorldHex hex = model.getWorld().getHex(state.getStartingLocation());
            model.getScreenHandler().put(converted.x, converted.y, hex.getLocation().getTownOrCastleSprite());
        }

        for (QuestBackground qb : quest.getDecorations()) {
            Point converted = convertToScreen(qb.position);
            if (qb.shifted) {
                converted.y -= 2;
            }
            model.getScreenHandler().register(qb.sprite.getName(), converted, qb.sprite);
        }
    }

    private void drawSubScenes(Model model, SteppingMatrix<QuestNode> matrix) {
        for (int row = 0; row < matrix.getRows(); ++row) {
            for (int col = 0; col < matrix.getColumns(); ++col) {
                if (matrix.getElementAt(col, row) != null) {
                    Point conv = convertToScreen(new Point(col, row));
                    int xPos = conv.x;
                    int yPos = conv.y;
                    matrix.getElementAt(col, row).drawYourself(model, xPos, yPos);

                    if (state.getCurrentPosition() == matrix.getElementAt(col, row) && avatarEnabled) {
                        model.getScreenHandler().register("questAvatar", new Point(xPos, yPos),
                                model.getParty().getLeader().getAvatarSprite(), 2);
                    }

                    if (matrix.getSelectedElement() == matrix.getElementAt(col, row) && state.isCursorEnabled()) {
                        model.getScreenHandler().register("questcursor", new Point(xPos, yPos),
                                questCursor, 3);
                    }
                }
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getDescription();
    }

    @Override
    protected String getTitleText(Model model) {
        return "QUEST - " + quest.getName().toUpperCase();
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (state.isCursorEnabled()) {
            return matrix.handleKeyEvent(keyEvent);
        }
        return false;
    }

    public void setDrawAvatarEnabled(boolean b) {
        avatarEnabled = b;
    }

    public static void animateAvatarAlongEdge(AvatarSubView subView, Point from, QuestEdge edge, Sprite avatarSprite) {
        Point to = edge.getNode().getPosition();
        int dx = to.x - from.x;
        int dy = to.y - from.y;
        if (dx == 0 || dy == 0) {
            subView.addMovementAnimation(avatarSprite, convertToScreen(from), convertToScreen(to));
            subView.waitForAnimation();
            subView.removeMovementAnimation();
        } else {
            Point mid;
            if (edge.getAlignment()) {
                mid = new Point(to.x, from.y);
            } else {
                mid = new Point(from.x, to.y);
            }
            subView.addMovementAnimation(avatarSprite, convertToScreen(from), convertToScreen(mid));
            subView.waitForAnimation();
            subView.removeMovementAnimation();
            subView.addMovementAnimation(avatarSprite, convertToScreen(mid), convertToScreen(to));
            subView.waitForAnimation();
            subView.removeMovementAnimation();
        }
    }
    
    public static void animateAvatarAlongEdge(QuestState state, Point from, QuestEdge edge, Sprite avatarSprite) {
        animateAvatarAlongEdge(state.getSubView(), from, edge, avatarSprite);
    }

    public void animateMovement(Model model, Point from, QuestEdge edge) {
        setDrawAvatarEnabled(false);
        animateAvatarAlongEdge(state, from, edge, model.getParty().getLeader().getAvatarSprite());
        setDrawAvatarEnabled(true);
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    public void setEdgesEnabled(boolean edgesEnabled) {
        this.edgesEnabled = edgesEnabled;
    }

    public void setSubScenesEnabled(boolean subScenesEnabled) {
        this.subScenesEnabled = subScenesEnabled;
    }

    public synchronized void addSpecialEffect(Point position, RunOnceAnimationSprite sprite) {
        addOngoingEffect(new MyPair<>(convertToScreen(position), sprite));
    }

    public void setCursorBlack(boolean enabled) {
        if (enabled) {
            questCursor = BLACK_CURSOR;
        } else {
            questCursor = WHITE_CURSOR;
        }
    }
}
