package model.quests;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;

import java.util.List;

public class FrogmenProblemQuest extends Quest {
    private static final String TEXT = "You've been contracted by a town to take care of a rampart population of " +
            "frogmen. The frogmen have their settlement nearby. You could just wipe them all out, but is there " +
            "something more to this job than meets the eye?";
    private static final String END_TEXT = "You return to town to report your success. You are rewarded for your service.";
    private AdvancedAppearance portrait;

    public FrogmenProblemQuest() {
        super("Frogmen Problem", "Uncle", QuestDifficulty.EASY, 0, 25, 25, TEXT, END_TEXT);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return portrait;
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Gather Clues",
                List.of(new CollaborativeSkillCheckSubScene(1, 1, Skill.SeekInfo, 8,
                        "Maybe we can ask around town. Somebody must know something."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision qsp = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "First we need to find out where these frogmen are located.");
        return List.of(qsp);
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

    public void setPortrait(AdvancedAppearance unclePortrait) {
        this.portrait = unclePortrait;
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (questWasSuccess) {
            model.getMainStory().increaseStep();
        } else {
            state.println("However, this quest can be accepted again.");
            model.getQuestDeck().unsetFailureIn(model.getCurrentHex().getLocation());
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }
}
