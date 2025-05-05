package model.quests;

import sound.BackgroundMusic;
import view.MyColors;

import java.util.List;

public class NightAtTheTheaterQuest extends RemotePeopleQuest {
    public static final String QUEST_NAME = "Night at the Theater";
    private static final String INTRO_TEXT = "TODO: intro";
    private static final String END_TEXT = "TODO: outro";

    public NightAtTheTheaterQuest() {
        super(QUEST_NAME, "Lord Shingen", QuestDifficulty.VERY_HARD, new Reward(1, 200, 0),
                0, INTRO_TEXT, END_TEXT);
    }

    @Override
    public MainQuest copy() {
        return new NightAtTheTheaterQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of();
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPoint(List.of(new QuestEdge(getSuccessEndingNode()),
                new QuestEdge(getFailEndingNode())), "Blabla"));
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
        return BackgroundMusic.darkQuestSong;
    }
}
