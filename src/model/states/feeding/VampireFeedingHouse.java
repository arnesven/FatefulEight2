package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.quests.*;
import model.states.GameState;
import model.states.events.GeneralInteractionEvent;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class VampireFeedingHouse {
    private final int stories;
    private final int dwellers;
    private final int sleeping;
    private final int lockDifficulty;

    private List<FeedingSubScene> subScenes;
    private List<FeedingJunction> junctions;
    private QuestFailNode failNode;
    private QuestSuccessfulNode successfulNode;
    private final GameCharacter vampire;

    public VampireFeedingHouse(GameCharacter vampire) {
        this.stories = MyRandom.randInt(1, 3);
        this.dwellers = MyRandom.randInt(1, 4);
        this.sleeping = MyRandom.randInt(dwellers);
        this.lockDifficulty = MyRandom.randInt(6, 8);
        this.vampire = vampire;
        makeNodes();
    }

    private void makeNodes() {
        this.junctions = new ArrayList<>();
        this.subScenes = new ArrayList<>();

        subScenes.add(new StakeOutSubScene(0, 3, vampire));
        subScenes.add(new UnlockDoorSubScene(2, 3, lockDifficulty, vampire));  // TODO: If multi-story house, choice of acrobatics.
        subScenes.add(new SneakingSubScene(4, 3, dwellers - sleeping, vampire));
        subScenes.add(new FeedingNode(6, 3, dwellers, sleeping, vampire));

        junctions.add(new FeedingStartNode(subScenes.get(0)));
        this.failNode = new QuestFailNode();
        GoToNextHouseNode goToNextHouse = new GoToNextHouseNode();
        junctions.add(new ChooseToEnter(1, 3, List.of(new QuestEdge(subScenes.get(1)), new QuestEdge(goToNextHouse))));
        junctions.add(goToNextHouse);

        this.successfulNode = new QuestSuccessfulNode(new Reward(0, 0,0 ), "");
        successfulNode.move(7, 3);

        subScenes.get(0).connectSuccess(junctions.get(1));
        subScenes.get(1).connectSuccess(subScenes.get(2));
        subScenes.get(1).connectFail(goToNextHouse, QuestEdge.VERTICAL);
        subScenes.get(2).connectSuccess(subScenes.get(3));
        subScenes.get(2).connectFail(failNode, QuestEdge.VERTICAL);
        subScenes.get(3).connectSuccess(successfulNode);
        subScenes.get(3).connectFail(goToNextHouse, QuestEdge.VERTICAL);
    }

    public int getStories() {
        return stories;
    }

    public int getDwellers() {
        return dwellers;
    }

    public int getSleeping() {
        return sleeping;
    }

    public int getLockDifficulty() {
        return lockDifficulty;
    }

    public List<? extends QuestJunction> getJunctions() {
        return junctions;
    }

    public List<? extends QuestSubScene> getSubScenes() {
        return subScenes;
    }

    public List<QuestNode> getNodes() {
        List<QuestNode> nodes = new ArrayList<>(subScenes);
        nodes.addAll(junctions);
        nodes.add(failNode);
        nodes.add(successfulNode);
        return nodes;
    }

    private class FeedingStartNode extends FeedingJunction {
        public FeedingStartNode(QuestNode nextNdde) {
            super(0, 0, List.of(new QuestEdge(nextNdde, QuestEdge.VERTICAL)));
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state) {
            state.println(state.heOrSheCap(vampire.getGender()) + " approaches a house, it's a " +
                    MyStrings.numberWord(getStories()) + " story building.");
            model.getLog().waitForAnimationToFinish();

            return getConnection(0);
        }
    }

    private class StakeOutSubScene extends FeedingSubScene {
        public StakeOutSubScene(int col, int row, GameCharacter vampire) {
            super(col, row, vampire);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            state.println(vampire.getFirstName() + " stakes out the house for a few minutes...");
            SkillCheckResult result = vampire.testSkill(model, Skill.Perception);
            state.println("Perception " + result.asString() + ".");
            if (result.getModifiedRoll() < 5) {
                state.println("But " + heOrShe(vampire.getGender()) + " discerns nothing.");
            } else {
                String dwellers = "are " + MyStrings.numberWord(getDwellers()) + " people";
                if (getDwellers() == 1) {
                    dwellers = "is only one person";
                }
                state.print("And " + hisOrHer(vampire.getGender()) + " vampiric senses tell " +
                        himOrHer(vampire.getGender()) + " that there " + dwellers + " living there");
                if (result.getModifiedRoll() >= 9) {
                    if (getDwellers() == 1) {
                        if (getSleeping() == 1) {
                            state.println(", and he or she is asleep.");
                        } else {
                            state.println(", and he or she is awake.");
                        }
                    } else {
                        state.println(", " + MyStrings.numberWord(getSleeping()) + " of them " +
                                (getSleeping()==1?"is":"are") + " asleep.");
                    }
                } else {
                    state.println(".");
                }
            }
            return super.getSuccessEdge();
        }
    }

    private static class ChooseToEnter extends FeedingJunction {
        public ChooseToEnter(int col, int row, List<QuestEdge> questEdges) {
            super(col, row, questEdges);
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state) {
            state.print("Do you want to try to enter this house? (Y/N) ");
            if (state.yesNoInput()) {
                return getConnection(0);
            }
            return getConnection(1);
        }
    }

    private static class UnlockDoorSubScene extends FeedingSubScene {
        private final int difficulty;

        public UnlockDoorSubScene(int col, int row, int difficulty, GameCharacter vampire) {
            super(col, row, vampire);
            this.difficulty = difficulty;
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            state.println("The house is locked.");
            boolean success = model.getParty().doSoloLockpickCheck(model, state, difficulty);
            if (!success) {
                state.println("The door to the house remains firmly locked.");
                return getFailEdge();
            }
            state.println(vampire.getFirstName() + " quietly enters the house through the front door.");
            return getSuccessEdge();
        }
    }

    private static class SneakingSubScene extends FeedingSubScene {

        private final int peopleAwake;

        public SneakingSubScene(int col, int row, int peopleAwake, GameCharacter vampire) {
            super(col, row, vampire);
            this.peopleAwake = peopleAwake;
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            if (peopleAwake > 0) {
                state.println(vampire.getFirstName() + " must sneak past the inhabitants who are still awake.");
                SkillCheckResult result = vampire.testSkill(model, Skill.Sneak, 5 + peopleAwake*2);
                if (result.isFailure()) {
                    state.println("You have been spotted!");
                    state.printQuote(GameState.manOrWomanCap(MyRandom.flipCoin()), "HEY! Get out of here you creep!");
                    GeneralInteractionEvent.addToNotoriety(model, state, VampireFeedingState.NOTORIETY_FOR_BEING_SPOTTED);
                    state.println(vampire.getFirstName() + " flees the house with haste before the constables arrive.");
                    return getFailEdge();
                }
                state.printQuote(GameState.manOrWomanCap(MyRandom.flipCoin()),
                        MyRandom.sample(List.of("Did I hear something? It's probably just the wind.",
                                "What was that? Hmm... naw, it was nothing.",
                                "Huh, someone there? No... just my mind playing tricks on me.")));
            }
            return getSuccessEdge();
        }
    }

    private static class FeedingNode extends FeedingSubScene {
        private final int awake;
        private final int sleeping;

        public FeedingNode(int col, int row, int dwellers, int sleeping, GameCharacter vampire) {
            super(col, row, vampire);
            this.awake = dwellers - sleeping;
            this.sleeping = sleeping;
        }

        @Override
        protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
            if (sleeping == 0) {
                state.println("There is nobody sleeping in this house."); // TODO: Add mesmerize ability
                state.println(vampire.getFirstName() + " quietly leaves the house.");
                return getFailEdge();
            }
            for (int i = 0; i < sleeping; ++i) {
                AdvancedAppearance victim = PortraitSubView.makeRandomPortrait(Classes.None);
                state.println(vampire.getFirstName() + " approaches the bed of a " + victim.getRace().getName() + ".");
                state.print("Do you wish to feed on this victim? (Y/N) ");
                if (state.yesNoInput()) {
                    state.println(vampire.getFirstName() + " descends upon the " + victim.getRace().getName() +
                            " and sinks " + hisOrHer(vampire.getGender()) + " teeth into " + hisOrHer(victim.getGender()) + ".");
                    state.println("The " + victim.getRace().getName() + " gasps and for a moment it seems " + heOrShe(victim.getGender()) +
                            " is about to wake up, but then it appears the dark aura of the vampire lulls " + himOrHer(victim.getGender()) +
                            " back into a lethargic state. At last, " + vampire.getFirstName() + " can drink " +
                            hisOrHer(vampire.getGender()) + " fill.");
                    vampire.addToSP(9999);
                    state.println(vampire.getFullName() + " Stamina has fully recovered.");
                    return getSuccessEdge();
                } else {
                    state.println(vampire.getFirstName() + " steps away from the bed.");
                }
            }
            state.println("Unable to find a suitable victim, " + vampire.getFirstName() + " quietly leaves the house.");
            return getFailEdge();
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }
    }
}
