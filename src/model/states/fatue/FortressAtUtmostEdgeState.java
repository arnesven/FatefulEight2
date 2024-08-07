package model.states.fatue;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.characters.special.WitchKingCharacter;
import model.items.special.FatueKeyItem;
import model.items.special.PieceOfStaffItem;
import model.items.special.StoryItem;
import model.items.weapons.StaffOfDeimosItem;
import model.items.weapons.Weapon;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.events.ConfrontUltimateAdversaryEvent;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import util.MyStrings;
import view.MyColors;
import view.subviews.DailyActionSubView;
import view.subviews.FortressAtUtmostEdgeSubView;

import java.awt.*;
import java.util.List;

public class FortressAtUtmostEdgeState extends AdvancedDailyActionState {
    public static final int MAX_KEYS = 6;
    private static final Point STARTING_POINT = new Point(7, 8);
    private static final Point CASTLE_PROPER_POSITION = new Point(4, 5);
    private static final String FIRST_TIME_IN_CASTLE_PROPER = "firstTimeInCastleProper";
    public static final int NUMBER_OF_PIECES = 7;
    private static final String CLEARED_KEY = "FATUE_CLEARED";

    public FortressAtUtmostEdgeState(Model model) {
        super(model);
        addNode(7, 8, new LeaveFatueNode());
        addNode(CASTLE_PROPER_POSITION.x, CASTLE_PROPER_POSITION.y, new EnterCastleProperNode());

        //Keys: Gold, Red (Dark red), Jade (Dark green), Silver (Gray), Bronze (Brown), Azure (Blue)

        addNode(1, 6, new WestWingNode(MyColors.GOLD));                           // Staff Piece, Gold Key, Puzzle
        addNode(2, 8, new MinesOfMiseryNode(MyColors.DARK_RED));                  // Staff Piece, Red Key, Materials?
        addNode(6, 6, new SouthGardenNode(MyColors.DARK_GREEN));                  // Jade Key, puzzle, Ingredients?
        addNode(5, 5, new EastWingNode(MyColors.DARK_RED, MyColors.GRAY));        // Requires Red Key, Staff Piece, Silver Key, Puzzle
        addNode(3, 4, new CourtyardGardenNode(MyColors.GOLD, MyColors.BROWN));    // Requires Gold Key, Staff Piece, Puzzle, Bronze Key
        addNode(5, 3, new EastTowerNode(MyColors.DARK_GREEN, MyColors.BLUE));     // Requires Jade Key, Staff Piece, Azure Key, Puzzle, Special Item
        addNode(4, 3, new FatueKeepNode(MyColors.GRAY));                          // Requires Silver Key, Staff Piece, Puzzle, Special Item
        addNode(1, 4, new NorthTowerNode(MyColors.BROWN, MyColors.BLUE));         // Requires Bronze and Azure Key, Staff Piece, Special Item
    }

    public static Point getCastleProperPosition() {
        return CASTLE_PROPER_POSITION;
    }

    protected static Point getStartingPoint() {
        return STARTING_POINT;
    }

    public static boolean isCleared(Model model) {
        return model.getSettings().getMiscFlags().containsKey(CLEARED_KEY);
    }

    public static void setFatueCleared(Model model) {
        model.getSettings().getMiscFlags().put(CLEARED_KEY, true);
    }

    @Override
    protected Point getStartingPosition() {
        return getStartingPoint();
    }

    @Override
    public GameState run(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.dungeonSong);
        leaderSay("My gosh, there's a whole castle down here!");
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "Yes, this cavern is must be enormous.");
        }
        if (WitchKingCharacter.isInParty(model)) {
            partyMemberSay(WitchKingCharacter.getFromParty(model), "This place looks terribly familiar...");
        }
        ClientSoundManager.playPreviousBackgroundMusic();
        return super.run(model);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                             SteppingMatrix<DailyActionNode> matrix) {
        return new FortressAtUtmostEdgeSubView(this, matrix);
    }

    public static int getNumberOfPiecesOfStaffFound(Model model) {
        if (hasStaff(model)) {
            return NUMBER_OF_PIECES;
        }
        return MyLists.filter(model.getParty().getInventory().getStoryItems(),
                (StoryItem st) -> st instanceof PieceOfStaffItem).size();
    }

    public List<MyColors> getKeysCollected(Model model) {
        return MyLists.transform(MyLists.filter(model.getParty().getInventory().getStoryItems(),
                (StoryItem st) -> st instanceof FatueKeyItem), (StoryItem st) -> ((FatueKeyItem)st).getColor());
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
                        discoverMural(model);
                    } else {
                        println("You are back in the grand hall with the mural and the curious stone door.");
                        if (hasStaff(model)) {
                            putStaffInFloor(model);
                        } else {
                            int pieces = getNumberOfPiecesOfStaffFound(model);
                            if (pieces == 0) {
                                print("You have not found the staff");
                            } else if (pieces == NUMBER_OF_PIECES) {
                                print("You have gathered all the pieces of staff do you want to assemble them? (Y/N) ");
                                if (yesNoInput()) {
                                    model.getParty().getInventory().getStoryItems().removeIf((StoryItem si) -> si instanceof PieceOfStaffItem);
                                    model.getParty().getInventory().add(new StaffOfDeimosItem());
                                    println("You assembled the pieces of staff into the Staff of Deimos!");
                                    putStaffInFloor(model);
                                }
                            } else {
                                print("You have found " + MyStrings.numberWord(pieces) + " pieces of the staff");
                            }
                            println(" you suspect is to be put in the hole in the floor.");
                        }
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

    public static boolean hasStaff(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getEquipment().getWeapon() instanceof StaffOfDeimosItem) {
                return true;
            }
        }
        return MyLists.any(model.getParty().getInventory().getWeapons(), (Weapon w) -> w instanceof StaffOfDeimosItem);
    }

    private void putStaffInFloor(Model model) {
        if (isCleared(model)) {
            println("This is where you defeated the Ultimate Adversary. Now nothing remains here.");
        } else {
            print("Would you like to insert the staff into the hole in the floor? (Y/N) ");
            if (yesNoInput()) {
                new ConfrontUltimateAdversaryEvent(model).run(model);
            }
        }
    }

    private void discoverMural(Model model) {
        model.getSettings().getMiscFlags().put(FIRST_TIME_IN_CASTLE_PROPER, false);
        println("You pass through a menacing portcullis and enter what must be the main building of " +
                "the fortress. The roof of the structure must have caved at some point, because you can see all the way to the " +
                "ceiling of the cavern. The once grand hall has a large mural depicting a battle. The color of the painting " +
                "has long since faded, but you can clearly make out the magnificent details.");
        leaderSay("This is a masterpiece of wonder...");
        println("The painting has two armies clashing. Footmen and knights rushing toward each other from opposite " +
                "sides. On one side of the painting a man wielding a staff can be seen standing atop a rocky outcropping.");
        if (WitchKingCharacter.isInParty(model)) {
            GameCharacter witchKing = WitchKingCharacter.getFromParty(model);
            partyMemberSay(witchKing, "This painting... I know it! I know where we are now!");
            partyMemberSay(witchKing, "This is the grand fortress of the Witch Kingdom. It was the undeniable " +
                    "seat of power for a thousand generations. And it was to be mine, before my brother robbed me of it.");
            if (model.getParty().getLeader() != witchKing) {
                leaderSay("Why was it built under ground?");
                partyMemberSay(witchKing, "Oh, it never was. As I recall it, it sat on a high plain overlooking a valley. " +
                        "I have no idea why it has been transported to these caverns.");
                leaderSay("Your brother ruled over from this castle. Do you think he could be lurking around here somewhere?");
                partyMemberSay(witchKing, "It's been a long time... but I guess anything is possible.");
            }
        } else {
            leaderSay("Interesting... what is this coming out from the staff here? Some form of magic, a ray " +
                    "of light or some other arcane power?");
        }
        println("There are several other open passageways out from the large room, but on the wall opposite the mural there is " +
                "what appears to be a shut stone door. A beam of light is shining down from the cave ceiling, hitting " +
                "the lower part of the door. A few feet in front of the door, there is a curious small hole in the floor.");
        leaderSay("Whatever could this be...");
        if (WitchKingCharacter.isInParty(model)) {
            GameCharacter witchKing = WitchKingCharacter.getFromParty(model);
            partyMemberSay(witchKing, "It's a socket where the Staff of Deimos shall be placed. Beyond that door " +
                    "is the inner sanctum. This beam of light comes down from the ceiling, passes through the gem in the staff's head, " +
                    "and the resulting ray travels into this little slot in the door. Behind it lies the most heavily " +
                    "guarded secrets in all the Witch Kingdom. Or so I was told as a child.");
            if (model.getParty().getLeader() != witchKing) {
                leaderSay("Okay... so where do we find this 'Staff of Deimos'?");
                partyMemberSay(witchKing, "My father, the old king, always said it was broken into pieces and " +
                        "scattered about this fortress. To prevent it, and our secrets to fall into the wrong hands, " +
                        "should the fortress every be lost to our enemies.");
                leaderSay("Better start looking for it then.");
                partyMemberSay(witchKing, "Indeed. The staff itself is a powerful artifact. And I am more than a little curious " +
                        "to what is hidden behind that door.");
            }
        } else {
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
        }
    }

    public static boolean firstTimeInCastleProper(Model model) {
        return !model.getSettings().getMiscFlags().containsKey(FIRST_TIME_IN_CASTLE_PROPER);
    }


}
