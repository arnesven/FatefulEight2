package model.quests;

import model.classes.Skill;
import model.journal.StoryPart;
import model.quests.scenes.SoloSkillCheckSubScene;
import view.MyColors;

import java.util.List;

public class HelpWillisQuest extends MainQuest {
    public static final String QUEST_NAME = "Help Willis";
    private static final String INTRO_TEXT = "Willis needs help in the library. You must find and recruit suitable candidates. " +
            "But not everybody is cut out to be a library assistant.";
    private static final String ENDING_TEXT = "Willis is pleased with your help.";

    public HelpWillisQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD, 1, 25, 0, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    protected int getStoryTrack() {
        return StoryPart.TRACK_A;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Interview candidates", List.of(
                new SoloSkillCheckSubScene(1, 1, Skill.Perception, 11, "Let's see what these guys are about.")
        )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "First we need some find some candidates for work in the library."));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode());
        scenes.get(0).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }
}
