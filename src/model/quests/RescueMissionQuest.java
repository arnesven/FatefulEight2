package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.journal.StoryPart;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.states.QuestState;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class RescueMissionQuest extends MainQuest {
    public static final String QUEST_NAME = "Rescue Mission";
    private static final String TEXT = "The party sets out to find and rescue Caid.";
    private static final String ENDING = "ENDING TEXT TODO";

    public RescueMissionQuest() {
        super(QUEST_NAME, "", QuestDifficulty.MEDIUM, 1, 35, 0, TEXT, ENDING);
    }

    @Override
    protected int getStoryTrack() {
        return StoryPart.TRACK_A;
    }

    @Override
    public String getPrerequisites(Model model) {
        if (model.getParty().size() < 2) {
            return "You must have at least two party members to accept this quest.";
        }
        return null;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Search for clues",
                List.of(new CollaborativeSkillCheckSubScene(6, 0, Skill.Security, 8,
                "Maybe there are clues in Caid's quarters at the castle. But his room is locked."),
                        new CollaborativeSkillCheckSubScene(5, 0, Skill.Search, 9, "Okay, we're inside. Let's snoop around for some clues."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        List<QuestJunction> juncs = new ArrayList<>();
        juncs.add(new RescueMissionStartingPoint(7, 0, new QuestEdge(scenes.get(0).get(0)),"How to find Caid..."));
        juncs.add(new StoryJunction(4, 0, new QuestEdge(getSuccessEndingNode())) {
            @Override
            protected void doAction(Model model, QuestState state) {
                GameCharacter gc = model.getParty().getRandomPartyMember();
                state.partyMemberSay(gc, "Bingo! A diary.");
                state.leaderSay("Hmm... Seems like Caid was tracking down a missing relative of " + getProvider() + ".");
                state.leaderSay("He was planning on going under cover as a gang member, in a robber group called 'the Vermin'.");
                state.partyMemberSay(gc, "Sounds fun. What did he do next?");
                state.leaderSay("I don't know. The journal ends here. That was three weeks ago.");
                state.partyMemberSay(gc, "...");
            }
        });
        juncs.add(new SimpleJunction(4,5, new QuestEdge(getFailEndingNode())));
        return juncs;
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1));
        scenes.get(0).get(0).connectFail(junctions.get(2));

        scenes.get(0).get(1).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(junctions.get(2));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    private static class RescueMissionStartingPoint extends QuestStartPointWithoutDecision {
        public RescueMissionStartingPoint(int col, int row, QuestEdge edge, String leaderTalk) {
            super(edge, leaderTalk);
            setColumn(col);
            setRow(row);
        }
    }
}
