package model.states.fatue;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.items.special.PieceOfStaffItem;
import model.items.special.StoryItem;
import model.ruins.DungeonMaker;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.states.ExploreRuinsState;
import model.states.GameState;
import model.states.NoLodgingState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import util.MyLists;
import util.MyStrings;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.FortressAtUtmostEdgeSubView;

import java.awt.*;

public class FortressAtUtmostEdgeState extends AdvancedDailyActionState {
    private static final Point STARTING_POINT = new Point(7, 8);
    private static final Point CASTLE_PROPER_POSITION = new Point(4, 5);
    private static final String FIRST_TIME_IN_CASTLE_PROPER = "firstTimeInCastleProper";

    public FortressAtUtmostEdgeState(Model model) {
        super(model);
        addNode(7, 8, new LeaveFatueNode());
        addNode(CASTLE_PROPER_POSITION.x, CASTLE_PROPER_POSITION.y, new EnterCastleProperNode());
        addNode(1, 6, new WestWingNode());
//        addNode(2, 8, new FatueDungeonNode("Enter Mines of Misery"));
//        addNode(5, 5, new FatueDungeonNode("Enter East Wing"));
//        addNode(6, 6, new FatueDungeonNode("Enter South Garden"));
//        addNode(3, 4, new FatueDungeonNode("Enter Courtyard Garden"));
//        addNode(1, 4, new FatueDungeonNode("Enter North Tower"));
//        addNode(5, 3, new FatueDungeonNode("Enter East Tower"));
//        addNode(4, 3, new FatueDungeonNode("Enter Keep"));
    }

    public static Point getCastleProperPosition() {
        return CASTLE_PROPER_POSITION;
    }

    @Override
    protected Point getStartingPosition() {
        return STARTING_POINT;
    }

    @Override
    public GameState run(Model model) {
        leaderSay("My gosh, there's a whole castle down here!");
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "Yes, this cavern is must be enormous.");
        }
        return super.run(model);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                             SteppingMatrix<DailyActionNode> matrix) {
        return new FortressAtUtmostEdgeSubView(this, matrix);
    }

    public int getNumberOfPiecesOfStaffFound(Model model) {
        return MyLists.filter(model.getParty().getInventory().getStoryItems(),
                (StoryItem st) -> st instanceof PieceOfStaffItem).size();
    }

    private abstract static class FatueDailyActionNode extends DailyActionNode {
        public FatueDailyActionNode(String name) {
            super(name);
        }

        @Override
        public Sprite getBackgroundSprite() {
            return null;
        }

        @Override
        public void drawYourself(Model model, Point p) { }
    }

    private static class LeaveFatueNode extends FatueDailyActionNode {
        public LeaveFatueNode() {
            super("Leave Fortress at the Ultimate Edge");
        }

        @Override
        public boolean exitsCurrentLocale() {
            return true;
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new NoLodgingState(model, false);
        }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            return true;
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { model.setTimeOfDay(TimeOfDay.EVENING); }
    }


    private class EnterCastleProperNode extends FatueDailyActionNode {
        public EnterCastleProperNode() {
            super("Enter Castle Proper");
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new GameState(model) {
                @Override
                public GameState run(Model model) {
                    if (firstTimeInCastleProper(model)) {
                        model.getSettings().getMiscFlags().put(FIRST_TIME_IN_CASTLE_PROPER, false);
                        println("You pass through a menacing portcullis and enter what must be the main building of " +
                                "the fortress. The roof of the structure must have caved at some point, because you can see all the way to the " +
                                "ceiling of the cavern. The once grand hall has a large mural depicting a battle. The color of the painting " +
                                "has long since faded, but you can clearly make out the magnificent details.");
                        leaderSay("This is a masterpiece of wonder...");
                        println("The painting has two armies clashing. Footmen and knights rushing toward each other from opposite " +
                                "sides. On one side of the painting a man wielding a staff can be seen standing atop a rocky outcropping.");
                        leaderSay("Interesting... what is this coming out from the staff here? Some form of magic, a ray " +
                                "of light or some other arcane power?");
                        println("There are several other open passageways out from the large room, but on the wall opposite the mural there is " +
                                "what appears to be a shut stone door. A beam of light is shining down from the cave ceiling, hitting " +
                                "the lower part of the door. A few feet in front of the door, there is a curious small hole in the floor.");
                        leaderSay("Whatever could this be...");
                        GameCharacter rando = model.getParty().getRandomPartyMember();
                        if (model.getParty().size() >= 2) {
                           rando = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                        }
                        partyMemberSay(rando, "If I could venture a guess... I would say that the door " +
                                "can only be opened by a special beam of light.");
                        leaderSay("Like that beam of light in the mural?");
                        if (rando != model.getParty().getLeader()) {
                            partyMemberSay(rando, "Yes... that's what I would suspect.");
                        }
                        partyMemberSay(rando, "Perhaps that staff is hidden somewhere in this fortress?");
                        leaderSay("Let's start looking for it... and whatever other secrets are hidden here.");
                    } else {
                        println("You are back in the grand hall with the mural and the curious stone door.");
                        int pieces = getNumberOfPiecesOfStaffFound(model);
                        if (pieces == 0) {
                            print("You have not found the staff");
                        } else {
                            print("You have found " + MyStrings.numberWord(pieces) + " pieces of the staff");
                        }
                        println(" you suspect is to be put in the hole in the floor.");
                    }
                    return null;
                }
            };
        }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            return true;
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
    }

    private boolean firstTimeInCastleProper(Model model) {
        return !model.getSettings().getMiscFlags().containsKey(FIRST_TIME_IN_CASTLE_PROPER);
    }


    private abstract class FatueDungeonNode extends FatueDailyActionNode {
        private final String name;

        public FatueDungeonNode(String name) {
            super("Enter " + name);
            this.name = name;
        }

        protected abstract RuinsDungeon makeDungeon(Model model);

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            return !getCurrentPosition().equals(STARTING_POINT);
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            RuinsDungeon dungeon;
            String key = "F.A.T.U.E - " + name;
            if (model.hasVisitedDungeon(key)) {
                dungeon = model.getDungeon(key, false);
            } else {
                dungeon = makeDungeon(model);
                model.storeDungeon(key, dungeon);
            }
            return new ExploreRuinsState(model, dungeon, key);
        }
    }

    private class WestWingNode extends FatueDungeonNode {
        public WestWingNode() {
            super("West Wing");
        }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            if (!super.canBeDoneRightNow(state, model)) {
                return false;
            }
            print("This passage leads to a dilapidated wing of the fortress. Would you like to explore it? (Y/N) ");
            return yesNoInput();
        }

        @Override
        protected RuinsDungeon makeDungeon(Model model) {
            RuinsDungeon dungeon = new RuinsDungeon(DungeonMaker.makeWestWingDungeon());
            FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels() - 1);
            finalLevel.setFinalRoom(new FatueStaffRoom());
            return dungeon;
        }
    }
}
