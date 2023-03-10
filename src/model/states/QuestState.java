package model.states;

import model.Model;
import model.SteppingMatrix;
import model.quests.Quest;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.subviews.CollapsingTransition;
import view.subviews.CombatTheme;
import view.subviews.QuestSubView;

import java.awt.*;

public class QuestState extends GameState {
    public static final int QUEST_MATRIX_ROWS = 9;
    public static final int QUEST_MATRIX_COLUMNS = 8;
    private final Quest quest;
    private final QuestSubView questSubView;
    private SteppingMatrix<QuestNode> matrix;
    private QuestNode currentPosition;
    private boolean cursorEnabled;
    private int counter = 0;

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
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mysticSong);
        println(quest.getText());
        print("Press enter to start quest.");
        model.getTutorial().quests(model);
        waitForReturn();


        while (currentPosition != quest.getSuccessEndingNode() && currentPosition != quest.getFailEndingNode()) {
            QuestEdge edgeToFollow = currentPosition.run(model, this);
            if (model.getParty().isWipedOut()) {
                return new GameOverState(model);
            }
            questSubView.animateMovement(model, new Point(currentPosition.getColumn(), currentPosition.getRow()),
                    edgeToFollow);
            currentPosition = edgeToFollow.getNode();
        }
        currentPosition.run(model, this); // Success or fail node run.
        print("Press enter to continue.");
        waitForReturn();
        setCurrentTerrainSubview(model);
        return model.getCurrentHex().getEveningState(model, false, false);
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

    public void setSelectedElement(QuestNode node) {
        matrix.setSelectedPoint(node);
    }

    public CombatTheme getCombatTheme() {
        return quest.getCombatTheme();
    }

    public void increaseQuestCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
