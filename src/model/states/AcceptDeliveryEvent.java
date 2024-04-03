package model.states;

import control.FatefulEight;
import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.parcels.Parcel;
import model.items.weapons.Club;
import model.journal.JournalEntry;
import model.map.*;
import model.races.Race;
import model.states.events.DarkDeedsEvent;
import model.states.events.NoEventState;
import model.tasks.DeliverParcelTask;
import model.tasks.DestinationTask;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AcceptDeliveryEvent extends DarkDeedsEvent {

    private static class Destination {
        Point position;
        String longDescription;
        String shortDescription;
        public Destination(Point p, String longD, String shortD) {
            this.position = p;
            this.longDescription = longD;
            this.shortDescription = shortD;
        }
    }

    private static final List<MyPair<String, Boolean>> recipiants = List.of(
            new MyPair<>("father", false),
            new MyPair<>("mother", true),
            new MyPair<>("grandfather", false),
            new MyPair<>("grandmother", true),
            new MyPair<>("sister", true),
            new MyPair<>("brother", false),
            new MyPair<>("aunt", true),
            new MyPair<>("uncle", false),
            new MyPair<>("cousin", true),
            new MyPair<>("colleague", false),
            new MyPair<>("friend", true),
            new MyPair<>("acquaintance", false)
    );

    private GameCharacter commoner;
    private Parcel parcel;
    private Destination destination;
    private MyPair<String, Boolean> recipient;
    private int promisedGold;
    private final String sender;
    private final boolean isTrueEvent;

    public AcceptDeliveryEvent(Model model) {
        super(model, "Talk to", 5);
        this.sender = "Somebody";
        this.isTrueEvent = true;
    }

    public AcceptDeliveryEvent(Model model, String sender) {
        super(model, "UNUSED", 0);
        this.sender = sender;
        this.isTrueEvent = false;
        setUpDeliveryData(model);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        if (model.getParty().getReputation() < 1 && !FatefulEight.inDebugMode()) {
            new NoEventState(model).doTheEvent(model);
            return false;
        }
        println("A commoner approaches you.");
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.None);
        this.commoner = new GameCharacter("Commoner", "", app.getRace(), Classes.None, app,
                                            Classes.NO_OTHER_CLASSES, new Equipment(new Club()));
        setUpDeliveryData(model);
        showExplicitPortrait(model, commoner.getAppearance(), commoner.getName());
        return true;
    }

    private void setUpDeliveryData(Model model) {
        this.parcel = Parcel.makeRandomParcel();
        this.destination = makeRandomDestination(model);
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), true);
        List<Point> pathToDestination = model.getWorld().shortestPathToPoint(destination.position);
        this.promisedGold = (int)(pathToDestination.size() * parcel.getDeliveryGoldMultiplier());
        this.recipient = MyRandom.sample(recipiants);
    }

    private void senderSpeak(String text) {
        if (isTrueEvent) {
            portraitSay(text);
        } else {
            printQuote(sender, text);
        }
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        senderSpeak("Hey you! I've heard about you! You're " +
                (model.getParty().size()>1?"those adventurers":"that adventurer") + " people are talking about.");
        return offerDeliveryTask(model);
    }

    public boolean offerDeliveryTask(Model model) {
        senderSpeak("Are you getting on the road soon? I have a " + parcel.getName().toLowerCase() + " I would like you " +
                "to deliver. I would do it myself, but the destination is kind of remote, " +
                "and I'm not sure I'm up for the trek into the wilds. You would be compensated of course. Are you interested?");
        leaderSay("Where do you want it delivered?");
        senderSpeak("It's to my " + recipient.first + ". " + heOrSheCap(recipient.second) +
                " lives in " + destination.longDescription + ". You can't miss it. " + heOrSheCap(recipient.second) + " will pay you " +
                promisedGold + " gold for it.");
        print("Do you accept to make the delivery? (Y/N) ");
        if (yesNoInput()) {
            senderSpeak(MyRandom.sample(List.of("Great", "Fantastic", "Good", "Perfect")) + "! Here's the " +
                    parcel.getName().toLowerCase() + ". Give my regards to my " + recipient.first + " when you see " +
                    himOrHer(recipient.second) + ".");
            model.getParty().getInventory().add(parcel);
            println("You received a " + parcel.getName().toLowerCase() + ".");
            model.getParty().addDestinationTask(new DeliverParcelTask(sender, parcel, destination.position,
                    destination.longDescription, destination.shortDescription, recipient.first, getSenderRace(),
                    recipient.second, promisedGold));
            JournalEntry.printJournalUpdateMessage(model);
            return false;
        }
        if (model.getParty().getLeader().hasPersonality(PersonalityTrait.snobby)) {
            leaderSay("What do you take " + meOrUs() + " for? Some kind of errand boy" +
                    (model.getParty().size() > 1 ? "s" : "") + "?");
        } else if (model.getParty().getLeader().hasPersonality(PersonalityTrait.rude)) {
            leaderSay("Forget about it. I'm not a mailman.");
        } else {
            leaderSay(MyRandom.sample(List.of(
                    "I'm sorry. It's kind of out of " + myOrOur() + " way.",
                    "I'm afraid we don't have time for that.",
                    "You'll have to find someone else to deliver it.")));
        }
        senderSpeak(MyRandom.sample(List.of(
                "That's too bad, " + heOrShe(recipient.second) + " will be so disappointed.",
                "Okay, I'm sure I'll find somebody else.",
                "And I heard you were reliable...",
                "Aww... come on...")));
        return true;
    }

    private Race getSenderRace() {
        if (isTrueEvent) {
            return commoner.getRace();
        }
        return Race.randomRace();
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return commoner;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }


    private Destination makeRandomDestination(Model model) {
        System.out.println("Making random destination!");
        Point position = randomPositionWithoutLocation(model);
        System.out.println("Position: (" + position.x + ", " + position.y + ")");
        String dwelling = MyRandom.sample(List.of(
                "hut", "house", "cottage", "lodge", "tower",
                "shack", "villa", "cave", "tent", "cabin"));

        WorldHex hex = model.getWorld().getHex(position);
        StringBuilder description = new StringBuilder("a " + dwelling);
        String shortDescription;
        if (hex.hasRoad()) {
            description.append(" on the road");
            shortDescription = "a " + dwelling + " by the side of the road";
        } else {
            String inThe = " in the ";
            if (hex instanceof TundraHex || hex instanceof PlainsHex) {
                inThe = " on the ";
            }
            description.append(inThe).append(hex.getTerrainName());
            shortDescription = description.toString();
        }

        boolean foundLandMark = false;
        for (Point dxdy : Direction.getDxDyDirections(position)) {
            Point newPoint = new Point(position.x + dxdy.x, position.y + dxdy.y);
            HexLocation loc = model.getWorld().getHex(newPoint).getLocation();
            if (loc instanceof TempleLocation || loc instanceof RuinsLocation || loc instanceof UrbanLocation ||
                loc instanceof InnLocation) {
                int direction = Direction.opposite(Direction.getDirectionForDxDy(position, dxdy));
                String directionName = Direction.longNameForDirection(direction);
                description.append(" just ").append(directionName).append(" of the ").append(loc.getName());
                foundLandMark = true;
            }
        }

        if (!foundLandMark) {
            model.getWorld().dijkstrasByLand(position, true);
            List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
            Point dxdy = new Point(path.get(1).x - path.get(0).x, path.get(1).y - path.get(0).y);
            int direction = Direction.opposite(Direction.getDirectionForDxDy(position, dxdy));
            String directionName = Direction.longNameForDirection(direction);
            HexLocation loc = model.getWorld().getHex(path.get(path.size()-1)).getLocation();
            description.append(" ").append(directionName).append(" of the ").append(loc.getName());
        }

        System.out.println("Position: (" + position.x + ", " + position.y + ")");
        System.out.println("Long: " + description.toString());
        System.out.println("Short: " + shortDescription);
        return new Destination(position, description.toString(), shortDescription);
    }

    public static Point randomPositionWithoutLocation(Model model) {
        Point position;
        do {
            position = model.getWorld().getRandomPositionWithinBounds();
            WorldHex hex = model.getWorld().getHex(position);
            HexLocation loc = hex.getLocation();
            if (!(hex instanceof SeaHex) && (loc == null || loc.isDecoration())) {
                break;
            }
        } while (true);
        return position;
    }

}
