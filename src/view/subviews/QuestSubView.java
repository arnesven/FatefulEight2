package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.quests.Quest;
import model.quests.QuestNode;
import model.quests.QuestScene;
import model.states.QuestState;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.LoopingSprite;
import view.sprites.QuestCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class QuestSubView extends SubView {
    private final QuestState state;
    private final Quest quest;
    private static final MyColors BACKGROUND_COLOR = MyColors.GRAY;
    private static final Sprite bgSprite = new FilledBlockSprite(BACKGROUND_COLOR);
    private final SteppingMatrix<QuestNode> matrix;
    private static final LoopingSprite questCursor = new QuestCursorSprite();

    public QuestSubView(QuestState state, Quest quest, SteppingMatrix<QuestNode> matrix) {
        this.state = state;
        this.quest = quest;
        this.matrix = matrix;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        drawBackground(model);
        drawSubScenes(model, matrix);
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

                    if (state.getCurrentPosition() == matrix.getElementAt(col, row)) {
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
}
