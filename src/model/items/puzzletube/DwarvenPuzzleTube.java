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
import java.util.HashMap;
import java.util.Map;

public class DwarvenPuzzleTube extends ReadableItem {

    private static final Map<Point, Point> LOCATION_MAP = makeLocationMap();

    private static int counter = 0;
    private static final Sprite SPRITE = new ItemSprite(6, 16, MyColors.WHITE, MyColors.RED);
    private final WordPuzzle puzzle;
    private final Destination nextDestination;

    public DwarvenPuzzleTube(Model model, WordPuzzle puzzle, Point thisLocation) {
        super("Puzzle Tube #" + (++counter), 0);
        this.puzzle = puzzle;
        Point nextLocation = LOCATION_MAP.get(thisLocation);
        HexLocation loc = model.getWorld().getHex(nextLocation).getLocation();
        if (loc instanceof TempleLocation) {
            TempleLocation temple = (TempleLocation)loc;
            String description = " at the " + temple.getName();
            String shortDescription = "a display case";
            this.nextDestination = new Destination(nextLocation, shortDescription + description, shortDescription, "in");
        } else if (loc instanceof UrbanLocation) {
            UrbanLocation urban = (UrbanLocation)loc;
            String description = " in " + ((UrbanLocation) loc).getPlaceName();
            String shortDescription = "the " + urban.getLordTitle() + "'s residence";
            this.nextDestination = new Destination(nextLocation, shortDescription + description, shortDescription, "in");
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

    private static final Point WOODS_NEAR_EBONSHIRE = new Point(19, 19);
    private static final Point SW_DESERT_HILLS = new Point(14, 26);
    private static final Point SW_DESERT_MOUNTAIN = new Point(20, 23);
    private static final Point SE_MOUNTAIN = new Point(33, 27);
    private static final Point E_DESERT_HILLS = new Point(34, 21);
    private static final Point FIELDS_NEAR_ASHTONSHIRE = new Point(33, 18);
    private static final Point PLAINS_NEAR_URNTOWN = new Point(31, 14);
    private static final Point N_TUNDRA_HILLS = new Point(32, 10);
    private static final Point NW_TUNDRA = new Point(18, 10);
    private static final Point NW_SWAMP = new Point(14, 12);

    private static Map<Point, Point> makeLocationMap() {
        Map<Point, Point> result = new HashMap<>();
        result.put(WorldBuilder.TEMPLE_CRYSTAL,   NW_SWAMP);
        result.put(NW_SWAMP,                      WorldBuilder.TOWN_ROUKON);
        result.put(WorldBuilder.TOWN_ROUKON,      WOODS_NEAR_EBONSHIRE);
        result.put(WOODS_NEAR_EBONSHIRE,          WorldBuilder.TEMPLE_PLAINS);
        result.put(WorldBuilder.TEMPLE_PLAINS,    SW_DESERT_HILLS);
        result.put(SW_DESERT_HILLS,               SW_DESERT_MOUNTAIN);
        result.put(SW_DESERT_MOUNTAIN,            WorldBuilder.TEMPLE_SURF);
        result.put(WorldBuilder.TEMPLE_SURF,      WorldBuilder.TOWN_SHEFFIELD);
        result.put(WorldBuilder.TOWN_SHEFFIELD,   SE_MOUNTAIN);
        result.put(SE_MOUNTAIN,                   WorldBuilder.TOWN_SAINT_QUELLIN);
        result.put(WorldBuilder.TOWN_SAINT_QUELLIN, E_DESERT_HILLS);
        result.put(E_DESERT_HILLS,                WorldBuilder.TOWN_UPPER_THELN);
        result.put(WorldBuilder.TOWN_UPPER_THELN, WorldBuilder.TOWN_LOWER_THELN);
        result.put(WorldBuilder.TOWN_LOWER_THELN, FIELDS_NEAR_ASHTONSHIRE);
        result.put(FIELDS_NEAR_ASHTONSHIRE,       WorldBuilder.TEMPLE_PEAKS);
        result.put(WorldBuilder.TEMPLE_PEAKS,     PLAINS_NEAR_URNTOWN);
        result.put(PLAINS_NEAR_URNTOWN,           N_TUNDRA_HILLS);
        result.put(N_TUNDRA_HILLS,                WorldBuilder.TOWN_CAPE_PAXTON);
        result.put(WorldBuilder.TOWN_CAPE_PAXTON, NW_TUNDRA);
        result.put(NW_TUNDRA,                     WorldBuilder.TEMPLE_CRYSTAL);
        return result;
    }
}
