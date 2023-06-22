package model.quests;

import model.classes.Skill;
import model.journal.StoryPart;
import model.quests.scenes.SoloSkillCheckSubScene;
import view.MyColors;

import java.util.List;

public class SpecialDeliveryQuest extends MainQuest {
    public static final String QUEST_NAME = "Special Delivery";
    private static final String INTRO_TEXT = "You note down the details about the witch's client. " +
            "Wary of the 'third party', you set out to deliver the special potion.";
    private static final String END_TEXT = "Having delivered the potion, you return to the witch. " +
            "In addition to the information you want she agrees to give you a portion of the payment of the potion.";

    public SpecialDeliveryQuest() {
        super("Special Delivery", "", QuestDifficulty.MEDIUM, 0, 35, 50, INTRO_TEXT, END_TEXT);
    }

    @Override
    protected int getStoryTrack() {
        return StoryPart.TRACK_B;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Spot ambushers", List.of(
                new SoloSkillCheckSubScene(1, 1, Skill.Perception, 9,
                        "One of us needs to keep a look out for this 'third party', whoever they may be.")
        )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "Let's do this job quickly, okay?"));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode());
        scenes.get(0).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }
}
