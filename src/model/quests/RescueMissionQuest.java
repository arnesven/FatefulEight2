package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.SoldierEnemy;
import model.journal.StoryPart;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;
import view.subviews.QuestSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RescueMissionQuest extends MainQuest {
    public static final String QUEST_NAME = "Rescue Mission";
    private static final String TEXT = "The party sets out to find and rescue Caid.";
    private static final String ENDING = "ENDING TEXT TODO";
    private SplitPartyJunction split;
    private QuestNode otherGroupCurrent = null;
    private QuestEdge otherGroupNext = null;

    public RescueMissionQuest() {
        super(QUEST_NAME, "", QuestDifficulty.MEDIUM,
                1, 35, 0, TEXT, ENDING);
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

    private GameCharacter getSecondaryLeader(Model model) {
        return split.getNonLeaderGroup(model.getParty().getLeader()).get(0);
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        model.getParty().unbenchAll();
        return super.endOfQuest(model, state, questWasSuccess);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Search for clues",
                List.of(new CollaborativeSkillCheckSubScene(5, 0, Skill.Security, 8, // 8
                "Maybe there are clues in Caid's quarters at the castle. But his room is locked."),
                        new CollaborativeSkillCheckSubScene(4, 0, Skill.Search, 9, "Okay, we're inside. Let's snoop around for some clues."), // 9
                        new CollaborativeSkillCheckSubScene(1, 0, Skill.SeekInfo, 8, "Let's ask around town about 'the Vermin'."))), // 8
                new QuestScene("Other Town", List.of(
                        new CollaborativeSkillCheckSubScene(1, 3, Skill.SeekInfo, 8, "Let's ask around town if anybody's seen Caid.") // 8
                )),
                new QuestScene("Put on a show", List.of(
                        new BenchOtherGroupSubScene(false, new CollaborativeSkillCheckSubScene(3, 3, Skill.Persuade, 8, // 8
                                "First we need to persuade as many people to come to the show as possible.")),
                        new BenchOtherGroupSubScene(false, new CollectiveSkillCheckSubScene(5, 3, Skill.Entertain, 6, // 6
                                "Now we all have to make a convincing performance!"))
                )),
                new QuestScene("Spring Caid out of prison", List.of(
                        new BenchOtherGroupSubScene(true, new CollectiveSkillCheckSubScene(3, 5, Skill.Sneak, 6, // 6
                                "Be very careful when sneaking into the dungeon.")),
                        new BenchOtherGroupSubScene(true, new CombatGuardSubScene(4, 6)),
                        new BenchOtherGroupSubScene(true, new CollaborativeSkillCheckSubScene(5, 6, Skill.Security, 10, // 10
                                "Just pick the lock and we'll get Caid out of there."))
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        List<QuestJunction> juncs = new ArrayList<>();
        juncs.add(new RescueMissionStartingPoint(7, 0, new QuestEdge(scenes.get(0).get(0)),"How to find Caid..."));
        juncs.add(new StoryJunction(2, 0, new QuestEdge(scenes.get(0).get(2))) {
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
        juncs.add(new SimpleJunction(0,3, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL)));
        juncs.add(new SimpleJunction(0,1, new QuestEdge(juncs.get(2))));
        this.split = new SplitPartyJunction(2, 4,
                new QuestEdge(scenes.get(2).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL),
                "Okay, let's split up. One group (A) will put on a show in town as a diversion, the other group (B) will sneak " +
                        "into the dungeon and spring Caid out of jail.");
        juncs.add(split);
        juncs.add(new MergeJunction(6, 6, new QuestEdge(getSuccessEndingNode())));
        return juncs;
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1));
        scenes.get(0).get(0).connectFail(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(0).get(1).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(0).get(2).connectFail(junctions.get(3));
        scenes.get(0).get(2).connectSuccess(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(junctions.get(2));
        scenes.get(1).get(0).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectFail(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(2).get(0).connectSuccess(scenes.get(2).get(1));

        scenes.get(2).get(1).connectFail(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectSuccess(junctions.get(5));

        scenes.get(3).get(0).connectFail(scenes.get(3).get(1), QuestEdge.VERTICAL);
        scenes.get(3).get(0).connectSuccess(scenes.get(3).get(2));

        scenes.get(3).get(1).connectSuccess(scenes.get(3).get(2));
        scenes.get(3).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);

        scenes.get(3).get(2).connectSuccess(junctions.get(5));
        scenes.get(3).get(2).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
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

    private void merge(Model model) {
        otherGroupCurrent = null;
        model.getParty().unbenchAll();
    }

    private boolean runOtherGroup(Model model, QuestState state) {
        state.print("Switching groups. Press enter to continue.");
        state.waitForReturn();
        model.getParty().unbenchAll();
        model.getParty().benchPartyMembers(split.getLeaderGroup(model.getParty().getLeader()));
        GameCharacter otherLeader = getSecondaryLeader(model);

        QuestNode from = otherGroupCurrent;
        QuestEdge target = otherGroupNext;
        split.setAvatarEnabled(false);
        otherGroupCurrent = null;
        do {
            QuestSubView.animateAvatarAlongEdge(state, from.getPosition(), target, otherLeader.getAvatarSprite());
            otherGroupCurrent = target.getNode();
            if (otherGroupCurrent == getFailEndingNode()) {
                merge(model);
                return false;
            } else if (otherGroupCurrent instanceof SimpleJunction &&
                    !(otherGroupCurrent instanceof MergeJunction)) {
                from = otherGroupCurrent;
                target = ((SimpleJunction) otherGroupCurrent).getConnection(0);
                otherGroupCurrent = target.getNode();
            } else {
                break;
            }
        } while (true);
        otherGroupNext = otherGroupCurrent.run(model, state);
        state.print("Switching groups. Press enter to continue.");
        state.waitForReturn();
        model.getParty().unbenchAll();
        model.getParty().benchPartyMembers(split.getNonLeaderGroup(model.getParty().getLeader()));
        return true;
    }

    private boolean eitherGroupDead(Model model) {
        for (GameCharacter gc : split.getGroupA()) {
            if (!gc.isDead()) {
                return false;
            }
        }
        for (GameCharacter gc : split.getGroupB()) {
            if (!gc.isDead()) {
                return false;
            }
        }
        return true;
    }

    private static class CombatGuardSubScene extends CombatSubScene {
        public CombatGuardSubScene(int col, int row) {
            super(col, row, List.of(new SoldierEnemy('A'), new SoldierEnemy('A')), true); // Two of them
        }

        @Override
        protected String getCombatDetails() {
            return "a pair of Guards";
        }
    }

    private class BenchOtherGroupSubScene extends QuestSubScene {
        private final QuestSubScene inner;
        private final boolean isPathB;

        public BenchOtherGroupSubScene(boolean pathB, QuestSubScene inner) {
            super(inner.getColumn(), inner.getRow());
            this.inner = inner;
            this.isPathB = pathB;
        }

        @Override
        public void connectSuccess(QuestNode questNode, boolean align) {
            super.connectSuccess(questNode, align);
            inner.connectSuccess(questNode, align);
        }

        @Override
        public void connectFail(QuestNode questNode, boolean align) {
            super.connectFail(questNode, align);
            inner.connectFail(questNode, align);
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            inner.drawYourself(model, xPos, yPos);
            if (isSecondaryPath()) {
                GameCharacter otherLeader = getSecondaryLeader(model);
                model.getScreenHandler().register(otherLeader.getAvatarSprite().getName(), new Point(xPos, yPos),
                        otherLeader.getAvatarSprite(), 3);
            }
        }

        private boolean isSecondaryPath() {
            return otherGroupCurrent == this;
        }

        @Override
        public String getDescription() {
            return inner.getDescription();
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (isSecondaryPath()) {
                return inner.run(model, state);
            }

            if (otherGroupCurrent == null) {
                otherGroupCurrent = split;
                if (isPathB) {
                    otherGroupNext = split.getConnection(0);
                } else {
                    otherGroupNext = split.getConnection(1);
                }
            }

            // Remove secondary group
            model.getParty().unbenchAll();
            model.getParty().benchPartyMembers(split.getNonLeaderGroup(model.getParty().getLeader()));
            QuestEdge toReturn = inner.run(model, state);

            if (!runOtherGroup(model, state) || eitherGroupDead(model)) {
                return new QuestEdge(getFailEndingNode());
            }
            return toReturn;
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return inner.getSuccessEdgeColor();
        }

        @Override
        public String getDetailedDescription() {
            return inner.getDetailedDescription();
        }
    }

    private class MergeJunction extends SimpleJunction {
        public MergeJunction(int col, int row, QuestEdge questEdge) {
            super(col, row, questEdge);
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            super.drawYourself(model, xPos, yPos);
            if (otherGroupCurrent == this) {
                GameCharacter otherLeader = getSecondaryLeader(model);
                model.getScreenHandler().register(otherLeader.getAvatarSprite().getName(), new Point(xPos, yPos),
                        otherLeader.getAvatarSprite(), 3);
            }
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (state.getCurrentPosition() != this) {
                state.println("Group is waiting for the rest of the party...");
                return new QuestEdge(this);
            }

            while (otherGroupCurrent != this && otherGroupCurrent != null) {
                if (!runOtherGroup(model, state) || eitherGroupDead(model)) {
                    merge(model);
                    return new QuestEdge(getFailEndingNode());
                }
            }
            merge(model);
            return super.run(model, state);
        }
    }
}
