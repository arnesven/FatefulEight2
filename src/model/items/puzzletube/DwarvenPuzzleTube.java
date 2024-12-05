package model.items.puzzletube;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.ReadableItem;
import model.items.WordPuzzleFactory;
import model.journal.JournalEntry;
import model.map.TempleLocation;
import model.map.WorldBuilder;
import model.states.DailyEventState;
import model.states.events.FindPuzzleTubeEvent;
import model.tasks.Destination;
import util.MyRandom;
import view.GameView;
import view.InventoryView;
import view.MyColors;
import view.PuzzleTubeView;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.Map;

public class DwarvenPuzzleTube extends ReadableItem {

    private static final Map<Point, Point> LOCATION_MAP =
            Map.of(WorldBuilder.TEMPLE_CRYSTAL, new Point(15, 17),
                   new Point(15, 17),     WorldBuilder.TEMPLE_PLAINS,
                   WorldBuilder.TEMPLE_PLAINS,  new Point(14, 26),
                   new Point(14, 26),     WorldBuilder.TEMPLE_SURF,
                   WorldBuilder.TEMPLE_SURF,    new Point(33, 27),
                   new Point(33, 27),     WorldBuilder.TEMPLE_PEAKS,
                   WorldBuilder.TEMPLE_PEAKS,   new Point(31, 14),
                   new Point(31, 14),     WorldBuilder.TEMPLE_CRYSTAL);

    private static int counter = 0;
    private static final Sprite SPRITE = new ItemSprite(6, 16, MyColors.WHITE, MyColors.RED);
    private final WordPuzzle puzzle;
    private final Destination nextDestination;

    public DwarvenPuzzleTube(Model model, WordPuzzle puzzle, Point nextLocation) {
        super("Puzzle Tube #" + (++counter), 0);
        this.puzzle = puzzle;
        if (model.getWorld().getHex(nextLocation).getLocation() instanceof TempleLocation) {
            TempleLocation temple = (TempleLocation) model.getWorld().getHex(nextLocation).getLocation();
            String description = " at the " + temple.getName();
            String shortDescription = "a display case";
            this.nextDestination = new Destination(nextLocation, shortDescription + description, shortDescription, "in");
        } else {
            this.nextDestination = Destination.generateNaturalLandmarkDestinationAtPosition(model, nextLocation);
        }
    }

    public static DailyEventState generateEvent(Model model) {
        if (MyRandom.rollD10() == 10) {
            if (LOCATION_MAP.containsKey(model.getParty().getPosition()) ||
                    LOCATION_MAP.containsValue(model.getParty().getPosition())) {
                return new FindPuzzleTubeEvent(model);
            }
        }
        return null;
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
        DwarvenPuzzleTube tube = new DwarvenPuzzleTube(model, WordPuzzleFactory.makeRandomPuzzle(), LOCATION_MAP.get(from));
        tube.addYourself(model.getParty().getInventory());
    }

    public JournalEntry getTask(Model model) {
        return new SolvePuzzleTubeTask(this, puzzle);
    }

    public void addDestinationTask(Model model) {
        model.getParty().addDestinationTask(new FindPuzzleDestinationTask(nextDestination));
    }

    public String getNextDescription() {
        return nextDestination.getPreposition() + " " + nextDestination.getLongDescription();
    }
}
