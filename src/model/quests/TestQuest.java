package model.quests;

import sound.BackgroundMusic;
import view.MyColors;

import java.util.List;

public class TestQuest extends Quest {
    private static final String INTRO_TEXT = "This is the intro text which describes the premise of the quest.";
    private static final String ENDING_TEXT = "This is the ending text which describes the quests resolution when successfully completed.";


    public TestQuest() {
        super("Testing", "Provider", QuestDifficulty.EASY, new Reward(10, 20, 30), 3, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of();
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new QuestStartPoint(List.of(
                new QuestEdge(getFailEndingNode()),
                new QuestEdge(getSuccessEndingNode())),
                "TODO Leader talk");
        return List.of(qsp);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }
}
