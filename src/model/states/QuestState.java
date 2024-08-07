package model.states;

import model.Model;
import model.SteppingMatrix;
import model.quests.Quest;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.combat.CombatTheme;
import view.subviews.QuestSubView;
import view.subviews.SnakeTransition;

import java.awt.*;

public class QuestState extends GameState {
    public static final int QUEST_MATRIX_ROWS = 9;
    public static final int QUEST_MATRIX_COLUMNS = 8;
    private final Quest quest;
    private final QuestSubView questSubView;
    private final SteppingMatrix<QuestNode> matrix;
    private QuestNode currentPosition;
    private boolean cursorEnabled;
    private int counter = 0;
    private long timeStarted = -1;

    public QuestState(Model model, Quest quest) {
        super(model);
        this.quest = quest;
        matrix = new SteppingMatrix<>(QUEST_MATRIX_COLUMNS, QUEST_MATRIX_ROWS);
        for (QuestNode node : quest.getAllNodes()) {
            matrix.addElement(node.getColumn(), node.getRow(), node);
        }
        matrix.setSelectedElement(quest.getStartNode());
        cursorEnabled = true;
        questSubView = new QuestSubView(this, quest, matrix);
    }

    @Override
    public GameState run(Model model) {
        currentPosition = quest.getStartNode();
        transitionToQuestView(model);
        println("The party sets out on a quest.");
        BackgroundMusic previous = ClientSoundManager.getCurrentBackgroundMusic();
        ClientSoundManager.playBackgroundMusic(quest.getMusic());
        println(quest.getText());
        print("Press enter to start quest.");
        model.getTutorial().quests(model);
        waitForReturn();
        timeStarted = System.currentTimeMillis();

        while (currentPosition != quest.getSuccessEndingNode() && currentPosition != quest.getFailEndingNode()) {
            QuestEdge edgeToFollow = currentPosition.run(model, this);
            if (model.getParty().isWipedOut()) {
                System.out.println("Party wiped out during quest.");
                return new GameOverState(model);
            }
            if (edgeToFollow == null) {
                System.out.println("Edge is null");
            }
            questSubView.animateMovement(model, new Point(currentPosition.getColumn(), currentPosition.getRow()),
                    edgeToFollow);
            currentPosition = edgeToFollow.getNode();
            if (quest.clockEnabled() && getClockTime() == 0 && quest.clockTimeOutFailsQuest()) {
                println("The time limit of the quest has been reached.");
                questSubView.animateMovement(model, new Point(currentPosition.getColumn(), currentPosition.getRow()),
                        new QuestEdge(quest.getFailEndingNode()));
                currentPosition = quest.getFailEndingNode();
                break;
            }
        }
        currentPosition.run(model, this); // Success or fail node run.
        ClientSoundManager.playBackgroundMusic(previous);
        return quest.endOfQuest(model, this, currentPosition == quest.getSuccessEndingNode());
    }

    public QuestNode getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(QuestNode newPos) { currentPosition = newPos; }

    public QuestSubView getSubView() {
        return questSubView;
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
        SnakeTransition.transition(model, questSubView);
    }

    public void setSelectedElement(QuestNode node) {
        matrix.setSelectedElement(node);
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

    public int getClockTime() {
        int limit = quest.getTimeLimitSeconds()*100;
        if (timeStarted == -1) {
            return limit;
        }
        int left = limit - (int)((System.currentTimeMillis() - timeStarted) / 10);
        if (left < 0) {
            return 0;
        }
        return left;
    }

    public Quest getQuest() {
        return quest;
    }
}
