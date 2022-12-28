package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.quests.*;
import model.states.QuestState;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.LoopingSprite;
import view.sprites.QuestCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class QuestSubView extends AvatarSubView {
    private final QuestState state;
    private final Quest quest;
    private static final MyColors BACKGROUND_COLOR = MyColors.GRAY;
    private static final Sprite bgSprite = new FilledBlockSprite(BACKGROUND_COLOR);
    private final SteppingMatrix<QuestNode> matrix;
    private static final LoopingSprite questCursor = new QuestCursorSprite();
    private boolean avatarEnabled;

    public QuestSubView(QuestState state, Quest quest, SteppingMatrix<QuestNode> matrix) {
        this.state = state;
        this.quest = quest;
        this.matrix = matrix;
        avatarEnabled = true;
    }

    @Override
    protected void specificDrawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        drawBackground(model);
        drawEdges(model);
        drawSubScenes(model, matrix);
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
    }

    private void drawSubScenes(Model model, SteppingMatrix<QuestNode> matrix) {
        for (int row = 0; row < matrix.getRows(); ++row) {
            for (int col = 0; col < matrix.getColumns(); ++col) {
                if (matrix.getElementAt(col, row) != null) {
                    int xPos = X_OFFSET + col*4;
                    int yPos = Y_OFFSET + row*4;
                    matrix.getElementAt(col, row).drawYourself(model, xPos, yPos);

                    if (state.getCurrentPosition() == matrix.getElementAt(col, row) && avatarEnabled) {
                        model.getScreenHandler().register("questAvatar", new Point(xPos, yPos),
                                model.getParty().getLeader().getAvatarSprite());
                    }

                    if (matrix.getSelectedElement() == matrix.getElementAt(col, row) && state.isCursorEnabled()) {
                        model.getScreenHandler().register("questcursor", new Point(xPos, yPos),
                                questCursor);
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
        return matrix.handleKeyEvent(keyEvent);
    }

    public void setDrawAvatarEnabled(boolean b) {
        avatarEnabled = b;
    }

    public void animateMovement(Model model, Point from, QuestEdge edge) {
        setDrawAvatarEnabled(false);
        Point to = edge.getNode().getPosition();
        int dx = to.x - from.x;
        int dy = to.y - from.y;
        Sprite avatarSprite = model.getParty().getLeader().getAvatarSprite();
        if (dx == 0 || dy == 0) {
            addMovementAnimation(avatarSprite, convertToScreen(from), convertToScreen(to));
            waitForAnimation();
            removeMovementAnimation();
        } else {
            Point mid;
            if (edge.getAlignment()) {
                mid = new Point(to.x, from.y);
            } else {
                mid = new Point(from.x, to.y);
            }
            addMovementAnimation(avatarSprite, convertToScreen(from), convertToScreen(mid));
            waitForAnimation();
            removeMovementAnimation();
            addMovementAnimation(avatarSprite, convertToScreen(mid), convertToScreen(to));
            waitForAnimation();
            removeMovementAnimation();
        }
        setDrawAvatarEnabled(true);
    }

    private void waitForAnimation() {
        while (true) {
            if (super.movementAnimationIsDone()) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4);
    }
}
