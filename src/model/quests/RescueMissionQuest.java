package model.quests;

import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import view.MyColors;

import java.util.List;

public class RescueMissionQuest extends MainQuest {
    public static final String QUEST_NAME = "Rescue Mission";
    private static final String TEXT = "The party sets out to find and rescue Caid.";
    private static final String ENDING = "ENDING TEXT TODO";

    public RescueMissionQuest() {
        super(QUEST_NAME, "", QuestDifficulty.MEDIUM, 1, 35, 0, TEXT, ENDING);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Search for clues",
                List.of(new CollaborativeSkillCheckSubScene(1, 1, Skill.Search, 9,
                "Maybe there are clues in Caids quarters at the castle."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0))), "How to find Caid..."));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(getSuccessEndingNode());
        scenes.get(0).get(0).connectFail(getFailEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }
}
