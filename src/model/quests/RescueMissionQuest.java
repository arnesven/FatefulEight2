package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.SoldierEnemy;
import model.journal.StoryPart;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;
import view.subviews.PortraitSubView;
import view.subviews.QuestSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RescueMissionQuest extends MainQuest {
    public static final String QUEST_NAME = "Rescue Mission";
    private static final String TEXT = "The party sets out to find Caid.";
    private static final String ENDING = "You return to the castle to receive your payment.";
    private final AdvancedAppearance caidAppearance;
    private SplitPartyJunction split;
    private QuestNode otherGroupCurrent = null;
    private QuestEdge otherGroupNext = null;

    public RescueMissionQuest() {
        super(QUEST_NAME, "", QuestDifficulty.MEDIUM,
                1, 35, 0, TEXT, ENDING);
        this.caidAppearance = PortraitSubView.makeRandomPortrait(Classes.CAP, Race.ALL, false);
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
        if (questWasSuccess) {
            new CaidEndingEvent(model).doEvent(model);
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Search for clues",
                List.of(new CollaborativeSkillCheckSubScene(5, 0, Skill.Security, 8, // 8
                "Maybe there is something in Caid's quarters at the castle which can tell us his whereabouts. But his room is locked."),
                        new CollaborativeSkillCheckSubScene(4, 0, Skill.Search, 9,
                                "Okay, we're inside. Let's snoop around for some clues."), // 9
                        new CollaborativeSkillCheckSubScene(1, 0, Skill.SeekInfo, 8,
                                "Let's ask around town about 'the Vermin'."))), // 8
                new QuestScene("Come Up With A Plan", List.of(
                        new CollaborativeSkillCheckSubScene(1, 3, Skill.Logic, 8,
                                "Hmm. Forty bandits is more than we can handle. We need to come up with a plan.")  // 8
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
        juncs.add(new RescueMissionStartingPoint(7, 0, new QuestEdge(scenes.get(0).get(0)),
                "Let's start by gathering some clues here in town."));
        juncs.add(new StoryJunction(2, 0, new QuestEdge(scenes.get(0).get(2))) {
            @Override
            protected void doAction(Model model, QuestState state) {
                GameCharacter gc = model.getParty().getRandomPartyMember();
                state.partyMemberSay(gc, "Bingo! A diary.");
                state.leaderSay("Hmm... Seems like Caid was tracking down a missing relative of " + getProvider() + ", a sister.");
                state.leaderSay("He was planning on going under cover as a gang member, in a robber group called 'the Vermin'.");
                state.partyMemberSay(gc, "Sounds lovely. What did he do next?");
                state.leaderSay("I don't know. The journal ends here. That was four weeks ago.");
                state.partyMemberSay(gc, "...");
            }
        });
        juncs.add(new SimpleJunction(0,3, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL)));
        juncs.add(new SimpleJunction(0,1, new QuestEdge(juncs.get(2))));
        this.split = new SplitPartyJunction(2, 4,
                new QuestEdge(scenes.get(2).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL),
                "I have it! We'll split up. One group (A) will approach the fort and convince the bandits that we're traveling minstrels. " +
                        "We'll put on a show for them as a diversion. The other group (B) will sneak " +
                        "into the fort and spring Caid out of jail.");
        juncs.add(split);
        juncs.add(new MergeJunction(6, 6, new QuestEdge(getSuccessEndingNode())));
        juncs.add(new StoryJunction(1, 2, new QuestEdge(scenes.get(1).get(0))) {
            @Override
            protected void doAction(Model model, QuestState state) {
                SentryEvent event = new SentryEvent(model);
                event.doEvent(model);
            }
        });
        return juncs;
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1));
        scenes.get(0).get(0).connectFail(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(0).get(1).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(0).get(2).connectFail(junctions.get(3));
        scenes.get(0).get(2).connectSuccess(junctions.get(6));

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
        return split.groupADead() || split.groupBDead();
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

    private class SentryEvent extends DailyEventState {

        private CharacterAppearance portrait = PortraitSubView.makeRandomPortrait(Classes.MAR, Race.ALL, true);

        public SentryEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            leaderSay("Aha, 'the Vermin' have a hideout outside of town, in an old fort. Let's go there.");
            println("The party travels to the fort, which is not very far away. Upon approaching the fort you " +
                    "spot a young woman with a bow.");
            leaderSay("A sentry. Let's get her!");
            println("You quickly overpower the girl and pin her to the ground.");
            leaderSay("Don't struggle, we only want information. What do you know about the fort over there?");
            showExplicitPortrait(model, portrait, "Bandit Girl");
            portraitSay("Nothing! I'm just a peasant.");
            leaderSay("Yeah right. You're dressed as a bandit and you were keeping a lookout. Tell us the " +
                    "truth, you are a bandit belonging to 'the Vermin' gang, right?");
            portraitSay("Me, a vermin? Hah! We wiped those losers out weeks ago.");
            leaderSay("So you are a bandit?");
            println("The bandit girl seems embarrassed.");
            portraitSay("Uhm yes...");
            leaderSay("But not a vermin. You wiped them out, you said. Did you happen to see a tall handsome fellow " +
                    "with a headband and a sword on his back?");
            portraitSay("Hmm, there was a tall handsome guy in one of the dungeon cells when we took over the fort. " +
                    "He was going on about how he was a royal something-or-other... He didn't have a sword obviously, but I think he did " +
                    "have a headband.");
            leaderSay("Could be him. Okay, we'll tie you up now, but don't worry, we'll be back in a few hours after " +
                    "we've rescued our friend from the fort.");
            portraitSay("What!? My gang will slaughter you, there's forty of us. You'll die and I'll lie here and " +
                    "starve to death.");
            leaderSay("Don't worry, we'll come up with a plan. Now turn around.");
            portraitSay("Ahh, rats!");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        }
    }

    private class CaidEndingEvent extends DailyEventState {
        public CaidEndingEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            showExplicitPortrait(model, model.getMainStory().getCaidCharacter().getAppearance(), "Caid");
            portraitSay("Thanks for getting me out of there. Who are you by the way?");
            leaderSay("We've were hired by your lord to find you.");
            portraitSay("I'm glad somebody noticed I was missing. Even if it was just my employer.");
            leaderSay("What happened to you?");
            portraitSay("Well, I've been trying to track down my lord's sister, and...");
            leaderSay("You went undercover with a gang called 'the Vermine'?");
            portraitSay("Yes, exactly! How did you know that?");
            leaderSay("Uhm... lucky guess... please go on.");
            portraitSay("Well, the gang had a connection to the person I was trying to find, who seem to be very elusive by the way. " +
                    "I had just about earned the gang's trust when, when an old exiled member of the court shows up and exposes me. " +
                    "Then they threw me into their dungeon.");
            leaderSay("You couldn't handle a few gangsters?");
            portraitSay("Maybe, but they were my only good lead. I thought maybe I could turn the situation around again.");
            leaderSay("I guess you couldn't.");
            portraitSay("Well, they were probably talking about how to best ransom me back to my employer when the fort was " +
                    "stormed by a rival gang, completely wiping out 'the vermin'.");
            portraitSay("The new tenants of the fort didn't believe me when I told them who I was and seemed to have no intentions of " +
                    "letting me loose. So there I was stuck.");
            leaderSay("Until now. So what's the plan now?");
            portraitSay("I still have a job to do. Thanks again for rescuing me. Please tell my employer " +
                    "that I'll return when I have news of the missing relative.");
            leaderSay("Will do. So long.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        }
    }
}
