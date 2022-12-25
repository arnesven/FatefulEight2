package model.states;

import model.Model;
import model.SteppingMatrix;
import model.quests.Quest;
import model.quests.QuestJunction;
import model.quests.QuestNode;
import view.subviews.CollapsingTransition;
import view.subviews.QuestSubView;
import view.subviews.SubView;

public class QuestState extends GameState {
    public static final int QUEST_MATRIX_ROWS = 9;
    public static final int QUEST_MATRIX_COLUMNS = 8;
    private final Quest quest;
    private final QuestSubView questSubView;
    private SteppingMatrix<QuestNode> matrix;
    private QuestNode currentPosition;
    private boolean cursorEnabled;

    public QuestState(Model model, Quest quest) {
        super(model);
        this.quest = quest;
        matrix = new SteppingMatrix<>(QUEST_MATRIX_COLUMNS, QUEST_MATRIX_ROWS);
        for (QuestNode node : quest.getAllNodes()) {
            matrix.addElement(node.getColumn(), node.getRow(), node);
        }
        matrix.setSelectedPoint(quest.getStartNode());
        cursorEnabled = true;
        questSubView = new QuestSubView(this, quest, matrix);
    }

    @Override
    public GameState run(Model model) {
        currentPosition = quest.getStartNode();
        transitionToQuestView(model);
        println("The party sets out on a quest.");
        println(quest.getText());
        print("Press enter to start quest.");
        waitForReturn();

        while (currentPosition != quest.getSuccessEndingNode() && currentPosition != quest.getFailEndingNode()) {
            currentPosition = currentPosition.run(model, this);
        }
        currentPosition.run(model, this); // Success or fail node run.
        print("Press enter to continue.");
        waitForReturn();
        setCurrentTerrainSubview(model);
        return new EveningState(model);
    }

    public QuestNode getCurrentPosition() {
        return currentPosition;
    }

    public QuestNode getSelectedElement() {
        return matrix.getSelectedElement();
    }

    public void setCursorEnabled(boolean b) {
        this.cursorEnabled = b;
    }

    public boolean isCursorEnabled() {
        return cursorEnabled;
    }

    public void transitionToQuestView(Model model) {
        CollapsingTransition.transition(model, questSubView);
    }
}
