package model.map;

import model.Model;
import model.TimeOfDay;
import model.actions.*;
import model.items.puzzletube.DwarvenPuzzleTube;
import model.states.dailyaction.WildernessDailyAction;
import model.tasks.AlchemyTask;
import model.tasks.WorkbenchTask;
import model.tasks.DestinationTask;
import util.MyLists;
import model.states.events.SaberfishEvent;
import model.states.*;
import model.states.events.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyPair;
import util.MyRandom;
import view.sprites.Sprite;
import view.combat.CombatTheme;
import view.subviews.DailyActionMenu;
import view.combat.GrassCombatTheme;
import view.subviews.SubView;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.HexSprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class WorldHex {

    private final int state;
    private MyColors color;
    private Sprite upperLeft;
    private Sprite upperRight;
    private Sprite lowerLeft;
    private Sprite lowerRight;
    private int roads;
    private int rivers;
    private HexLocation hexLocation;
    private BackgroundMusic music;
    private List<WaterPath> waterPaths = new ArrayList<>();
    private MyPair<DailyEventState, Integer> preparedEvent;


    public WorldHex(MyColors color, int roads, int rivers, HexLocation location, int worldState) {
        this.color = color;
        this.roads = roads;
        this.rivers = rivers;
        setHexSprites();
        this.hexLocation = location;
        this.state = worldState;
    }

    public void setColor(MyColors col) {
        color = col;
        setHexSprites();
    }

    public abstract String getTerrainName();

    public DailyEventState generateEvent(Model model) {
        DailyEventState tutorialEvent = model.getTutorial().getTutorialEvent(model);
        if (tutorialEvent != null) {
            return tutorialEvent;
        }

        List<DailyEventState> conditionals = new ArrayList<>();
        MyLists.nonNullAdd(conditionals, popPreparedEvent(model));
        MyLists.nonNullAdd(conditionals, conditionalEvent(model));

        if (!conditionals.isEmpty()) {
            return MyRandom.sample(conditionals);
        }

        DailyEventState eventToReturn;
        if (hexLocation != null && !hexLocation.isDecoration()) {
            eventToReturn = hexLocation.generateEvent(model);
        } else if (model.getParty().isOnRoad()) {
            eventToReturn = generateOnRoadEvent(model);
        } else {
            eventToReturn = generateTerrainEventWithGuide(model);
        }
        if (eventToReturn instanceof NoEventState) {
            eventToReturn = generatePartyEvent(model);
        }
        return eventToReturn;
    }

    public DailyEventState generateEventFromDistance(Model model) {
        List<DailyEventState> events = new ArrayList<>();
        if (hasRoad()) {
            events.add(generateOnRoadEvent(model));
        }
        events.add(generateTerrainSpecificEvent(model));
        return MyRandom.sample(events);
    }

    private DailyEventState generateTerrainEventWithGuide(Model model) {
        GuideGivesChoiceState guideGivesChoiceState = new GuideGivesChoiceState(model) {
            @Override
            protected DailyEventState generateEvent(Model model) {
                return generateTerrainSpecificEvent(model);
            }
        };
        guideGivesChoiceState.run(model);
        return guideGivesChoiceState.getSelectedEvent();
    }

    protected abstract DailyEventState generateTerrainSpecificEvent(Model model);


    private DailyEventState conditionalEvent(Model model) {
        List<DailyEventState> conditionalEvents = new ArrayList<>();
        MyLists.nonNullAdd(conditionalEvents, LetterFromMotherEvent.generateEvent(model));
        MyLists.nonNullAdd(conditionalEvents, Loan.eventDependentOnLoan(model, this));
        MyLists.nonNullAdd(conditionalEvents, GeneralInteractionEvent.eventDependentOnNotoriety(model, this));
        MyLists.nonNullAdd(conditionalEvents, CaveSpelunkerEvent.generateEvent(model));
        MyLists.nonNullAdd(conditionalEvents, DwarvenPuzzleTube.generateEvent(model));
        MyLists.nonNullAdd(conditionalEvents, CrimsonAssassinsInvitationEvent.eventDependentOnMurders(model));
        MyLists.nonNullAdd(conditionalEvents, HomeTownEvent.eventDependentOnHomeTown(model));
        if (model.getTutorial().isTutorialEnabled()) {
            MyLists.nonNullAdd(conditionalEvents, WorkbenchTask.makeFirstTimeAtCraftingBenchEvent(model));
            MyLists.nonNullAdd(conditionalEvents, AlchemyTask.makeAlchemySpellGottenEvent(model));
        }
        if (conditionalEvents.isEmpty()) {
            return null;
        }
        return MyRandom.sample(conditionalEvents);
    }

    private void setHexSprites() {
        this.upperLeft = getUpperLeftSprite(color, roads, rivers);
        this.upperRight = getUpperRightSprite(color, roads, rivers);
        this.lowerLeft = getLowerLeftSprite(color, roads, rivers);
        this.lowerRight = getLowerRightSprite(color, roads, rivers);
    }

    protected Sprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ul", 0x00 + (roads % 4)*4 + rivers % 4, color);
    }

    protected Sprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ur", 0x20  + ((roads / 4) % 4)*4 + (rivers / 4) % 4, color);
    }

    protected Sprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ll", 0x10 + (roads / 64)*4 + (rivers / 64), color);
    }

    protected Sprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return  new HexSprite(getTerrainName()+"lr", 0x30 + ((roads / 16) % 4) * 4 + (rivers / 16) % 4, color);
    }

    public void drawYourself(ScreenHandler screenHandler, int x, int y, int flag) {
        drawUpperHalf(screenHandler, x, y, flag);
        drawLowerHalf(screenHandler, x, y);
    }

    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y, int flag) {
        screenHandler.put(x, y, upperLeft);
        screenHandler.put(x+2, y, upperRight);
        if (hexLocation != null) {
            hexLocation.drawUpperHalf(screenHandler, x, y, flag);
        }
    }

    public void drawLowerHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.put(x, y+2, lowerLeft);
        screenHandler.put(x+2, y+2, lowerRight);
        if (hexLocation != null) {
            hexLocation.drawLowerHalf(screenHandler, x, y);
        }
    }

    public String getDescription() {
        StringBuilder bldr = new StringBuilder();
        if (hexLocation != null && !hexLocation.isDecoration()) {
            bldr.append(hexLocation.getName() + ", ");
        }
        if (rivers != 0) {
            bldr.append("coastal ");
        }
        bldr.append(getTerrainName());
        if (roads != 0) {
            bldr.append(" with roads");
        }
        return bldr.toString();
    }

    public int getRivers() {
        return rivers;
    }

    public void setRivers(int i) {
        this.rivers = i;
        this.setHexSprites();
    }

    public int getRoads() {
        return roads;
    }

    public void setRoads(int i) {
        this.roads = i;
        this.setHexSprites();
    }

    public List<DailyAction> getDailyActions(Model model) {
        List<DailyAction> actions;
        actions = new ArrayList<>();
        if (model.getTimeOfDay() == TimeOfDay.MORNING) {
            actions.add(new DailyAction("Travel", new TravelState(model)));
            if (canFly(model)) {
                actions.add(new DailyAction("Fly on Dragon", new FlyWithDragonState(model)));
            }
            for (DestinationTask dt : model.getParty().getDestinationTasks()) {
                if (dt.givesDailyAction(model)) {
                    actions.add(dt.getDailyAction(model));
                }
            }
        }
        actions.add(new StayInHexAction(model));
        if (hexLocation != null && hexLocation.hasDailyActions()) {
            actions.addAll(hexLocation.getDailyActions(model));
        }
        if (model.getMainStory().isStarted()) {
            actions.addAll(model.getMainStory().getDailyActionsForHex(model, this));
        }
        WildernessDailyAction.addActionsIfApplicable(model, actions);
        if (model.getParty().isOnRoad()) {
            actions.add(new GetOffRoadAction(model));
        } else if (hasRoad()) {
            actions.add(new GetOnRoadAction(model));
        }
        return actions;
    }

    private boolean canFly(Model model) {
        int numberOfTamedDragons = model.getParty().getTamedDragons().values().size();
        return numberOfTamedDragons * 4 >= model.getParty().size();
    }

    public String getPlaceName() {
        if (hexLocation != null && !hexLocation.isDecoration()) {
            return "at the " + hexLocation.getName();
        }
        return "in the " + getDescription();
    }

    public boolean hasLodging() {
        if (hexLocation != null && hexLocation.hasLodging()) {
            return true;
        }
        return false;
    }

    public SubView getImageSubView() {
        if (hexLocation != null && !hexLocation.isDecoration()) {
            return hexLocation.getImageSubView();
        }
        return getSubView();
    }

    public boolean hasRoad() {
        return roads != 0;
    }

    protected abstract SubView getSubView();

    private DailyEventState generateOnRoadEvent(Model model) {
        int dieRoll = MyRandom.rollD10();
        if (5 <= dieRoll && dieRoll <= 8) {
            List<DailyEventState> events = new ArrayList<>();
            events.add(new WagonTravelEvent(model));
            events.add(new WagonTravelEvent(model));
            events.add(new MerchantEvent(model));
            events.add(new BanditEvent(model));
            events.add(new MagicianEvent(model));
            events.add(new ArtisanEvent(model));
            events.add(new PriestEvent(model));
            events.add(new CourierEvent(model));
            events.add(new MonumentEvent(model));
            events.add(new CompanyEvent(model));
            events.add(new NoblemanEvent(model));
            events.add(new FriendEvent(model));
            events.add(new MageEvent(model));
            events.add(new BrokenWagonEvent(model));
            events.add(new RunAwayHorseEvent(model));
            events.add(new OtherPartyEvent(model));
            events.add(new MountedPatrolEvent(model));
            events.add(new PilgrimEvent(model));
            events.add(new MonkEvent(model));
            events.add(new LottoHouseEvent(model));
            events.add(new DogEvent(model));
            events.add(new CampSiteEvent(model));
            events.add(new CampSiteEvent(model));
            return MyRandom.sample(events);
        } else if (dieRoll >= 9) {
            return generateTerrainSpecificEvent(model);
        }
        return new NoEventState(model);
    }

    public void travelTo(Model model) {
        if (hexLocation != null) {
            hexLocation.travelTo(model);
        }
        if (hasMusic()) {
            ClientSoundManager.playBackgroundMusic(music);
        }
    }

    private boolean hasMusic() {
        return this.music != null;
    }

    public void travelFrom(Model model) {
        if (hexLocation != null) {
            hexLocation.travelFrom(model);
        }
    }

    protected void setMusic(BackgroundMusic bg) {
        this.music = bg;
    }

    public boolean hasLord() {
        if (hexLocation != null) {
            return hexLocation instanceof TownLocation ||
                    hexLocation instanceof CastleLocation;
        }
        return false;
    }

    public HexLocation getLocation() {
        return hexLocation;
    }

    public boolean canTravelTo(Model model) {
        return true;
    }

    public boolean givesQuests() {
        return hexLocation != null && hexLocation.givesQuests();
    }

    private boolean getRoadOrRiverInDirection(int direction, int roadOrRiver) {
        return (roadOrRiver & direction) != 0;
    }

    public boolean getRiversInDirection(int direction) {
        return getRoadOrRiverInDirection(direction, rivers);
    }

    public boolean getRoadInDirection(int direction) {
        return getRoadOrRiverInDirection(direction, roads);
    }

    public RiverEvent generateRiverEvent(Model model) {
        GuideGivesChoiceState choice = new GuideGivesChoiceState(model) {
            @Override
            protected DailyEventState generateEvent(Model model) {
                return makeRandomRiverEvent(model);
            }
        };
        choice.run(model);
        return (RiverEvent) choice.getSelectedEvent();
    }

    public RiverEvent makeRandomRiverEvent(Model model) {
        return MyRandom.sample(List.of(
                new ShallowsEvent(model),
                new ShallowsEvent(model),
                new ShallowsEvent(model),
                new ShallowsEvent(model),
                new FerryEvent(model),
                new BlackKnightEvent(model),
                new RopeBridgeEvent(model),
                new DeadBodyEvent(model),
                new UndertowEvent(model),
                new RapidsEvent(model),
                new SaberfishEvent(model),
                new BoatsEvent(model),
                new BoatsEvent(model),
                new BoatsEvent(model),
                new RaftOnRiverEvent(model),
                new FishermanEvent(model),
                new RiverTrollsEvent(model),
                new NoRiverCrossingEvent(model),
                new NoRiverCrossingEvent(model)
        ));
    }

    public GameState getDailyActionState(Model model) {
        if (hexLocation != null && !hexLocation.isDecoration()) {
            return hexLocation.getDailyActionState(model);
        }
        return new DailyActionState(model);
    }

    public GameState getEveningState(Model model, boolean freeLodging, boolean freeRations) {
        EveningState initialLeadsEveningState = model.getMainStory().generateInitialLeadsEveningState(model, freeLodging, freeRations);
        if (initialLeadsEveningState != null) {
            return initialLeadsEveningState;
        }
        if (hexLocation != null && !hexLocation.isDecoration()) {
            return hexLocation.getEveningState(model, freeLodging, freeRations);
        }
        return new EveningState(model, freeLodging, freeRations, true);
    }

    public void addWaterPath(WaterPath p) {
        waterPaths.add(p);
    }

    public List<WaterPath> getWaterPaths() {
        return waterPaths;
    }

    public CombatTheme getCombatTheme() {
        if (getLocation() != null) {
            if (getLocation() instanceof UrbanLocation) {
                return ((UrbanLocation)getLocation()).getCombatTheme();
            }
        }
        return new GrassCombatTheme();
    }

    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        if (hexLocation != null && hexLocation.getDailyActionMenuAnchor() != null) {
            return hexLocation.getDailyActionMenuAnchor();
        }
        return DailyActionMenu.UPPER_LEFT_CORNER;
    }

    public boolean inhibitOnRoadSubview() {
        if (hexLocation != null) {
            return hexLocation.inhibitOnRoadSubview();
        }
        return false;
    }

    public static DailyEventState generatePartyEvent(Model model) {
        return MyRandom.sample(List.of(
                new RationsGoneBadEvent(model),
                new PartyMemberArgument(model),
                new PartyEntertainmentEvent(model),
                new PartyCookingEvent(model),
                new PartyJokeEvent(model),
                new PartyLowOnCashEvent(model),
                new PartySalaryEvent(model),
                new PartyMemberWantsToLeaveEvent(model),
                new PartyMemberWantsToLeaveEvent(model),
                new CheckForVampireEvent(model),
                new PersonalityEvent(model)
        ));
    }

    public int getState() {
        return state;
    }

    public abstract String getTerrainDescription();

    public DailyEventState generateDogEvent(Model model) {
        return null;
    }

    public DailyEventState getNightTimeAmbushEvent(Model model) {
        return null;
    }

    public abstract ResourcePrevalence getResourcePrevalences();

    public boolean isHighGround() {
        return hexLocation instanceof MountainLocation || hexLocation instanceof HillsLocation;
    }

    public void pushPreparedEvent(DailyEventState event, int day) {
        this.preparedEvent = new MyPair<>(event, day);
    }

    private DailyEventState popPreparedEvent(Model model) {
        DailyEventState result = null;
        if (preparedEvent != null) {
            if (preparedEvent.second == model.getDay()) {
                result = preparedEvent.first;
            }
            preparedEvent = null;
        }
        return result;
    }

    public DailyEventState getPreparedEvent(int day) {
        if (preparedEvent == null || preparedEvent.second != day) {
            return null;
        }
        return preparedEvent.first;
    }
}
