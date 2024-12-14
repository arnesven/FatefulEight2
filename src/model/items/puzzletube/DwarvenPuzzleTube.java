package model.items.puzzletube;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.ReadableItem;
import model.items.WordPuzzleFactory;
import model.journal.JournalEntry;
import model.map.*;
import model.states.DailyEventState;
import model.states.events.FindPuzzleTubeEvent;
import model.tasks.Destination;
import util.MyLists;
import util.MyRandom;
import view.GameView;
import view.InventoryView;
import view.MyColors;
import view.PuzzleTubeView;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.awt.*;

public class DwarvenPuzzleTube extends ReadableItem implements DwarvenPuzzleConstants {

    private static int counter = 0;
    private static final Sprite SPRITE = new ItemSprite(6, 16, MyColors.WHITE, MyColors.RED);
    private final WordPuzzle puzzle;
    private final Destination nextDestination;

    private DwarvenPuzzleTube(Model model, WordPuzzle puzzle, Point thisLocation) {
        super("Puzzle Tube #" + (++counter), 0);
        this.puzzle = puzzle;
        Point nextLocation = LOCATION_MAP.get(thisLocation);
        HexLocation loc = model.getWorld().getHex(nextLocation).getLocation();
        if (loc instanceof TempleLocation || loc instanceof UrbanLocation) {
            this.nextDestination = makeTempleOrTownDestination(nextLocation, loc);
        } else {
            this.nextDestination = Destination.generateNaturalLandmarkDestinationAtPosition(model, nextLocation);
        }
    }

    public static DailyEventState generateEvent(Model model) {
        if (MyRandom.rollD10() == 10) {
            HexLocation loc = model.getCurrentHex().getLocation();
            if (loc == null || loc.isDecoration()) {
                if (locationHasPuzzleTube(model.getParty().getPosition())) {
                    return new FindPuzzleTubeEvent(model);
                }
            }
        }
        return null;
    }

    public static boolean locationHasPuzzleTube(Point position) {
        return LOCATION_MAP.containsKey(position) || LOCATION_MAP.containsValue(position);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    @Override
    public String getShoppingDetails() {
        if (puzzle.isParchmentRemoved()) {
            return ", Solved. Word: " + puzzle.getSolutionWord();
        }
        return "";
    }

    @Override
    public Item copy() {
        throw new IllegalStateException();
        //return new DwarvenPuzzleTube();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        return gc.getFirstName() + " inspects the puzzle tube...";
    }

    @Override
    public boolean opensViewFromInventoryMenu() {
        return true;
    }

    @Override
    public String getUsageVerb() {
        return "Inspect";
    }

    @Override
    public GameView getViewFromInventoryMenu(Model model, InventoryView inventoryView, GameCharacter user) {
        return new PuzzleTubeView(inventoryView, puzzle, user, this);
    }

    public static void giveNewTubeToParty(Model model, Point from) {
        DwarvenPuzzleTube tube = new DwarvenPuzzleTube(model, WordPuzzleFactory.makeRandomPuzzle(), from);
        tube.addYourself(model.getParty().getInventory());
        if (!MyLists.any(model.getParty().getDestinationTasks(),
                dt -> dt instanceof MysteryOfTheTubesDestinationTask)) {
            model.getParty().addDestinationTask(
                    new MysteryOfTheTubesDestinationTask(model,
                            MyRandom.sample(DwarvenPuzzleConstants.WORKSHOP_POSITIONS)));
        }
    }

    public static Destination makeTempleOrTownDestination(Point nextLocation, HexLocation loc) {
        if (loc instanceof UrbanLocation) {
            UrbanLocation urban = (UrbanLocation) loc;
            String description = " in " + ((UrbanLocation) loc).getPlaceName();
            String shortDescription = "the " + urban.getLordTitle() + "'s residence";
            return new Destination(nextLocation, shortDescription + description, shortDescription, "in");
        }
        TempleLocation temple = (TempleLocation)loc;
        String description = " at the " + temple.getName();
        String shortDescription = "a display case";
        return new Destination(nextLocation, shortDescription + description, shortDescription, "in");
    }

    public JournalEntry getTask(Model model) {
        return new SolvePuzzleTubeTask(this, puzzle);
    }

    public void addDestinationTask(Model model) {
        if (!MyLists.any(model.getParty().getDestinationTasks(),
                dt -> dt.getPosition() == nextDestination.getPosition())) {
            model.getParty().addDestinationTask(new FindPuzzleDestinationTask(nextDestination));
        }
    }

    public String getNextDescription() {
        return nextDestination.getPreposition() + " " + nextDestination.getLongDescription();
    }
}
