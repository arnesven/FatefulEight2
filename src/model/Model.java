package model;

import control.FatefulEight;
import model.achievements.GameAchievements;
import model.actions.DailyAction;
import model.actions.StayInHexAction;
import model.characters.*;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.ItemDeck;
import model.items.spells.Spell;
import model.journal.MainStorySpawnLocation;
import model.log.GameLog;
import model.mainstory.MainStory;
import model.mainstory.MainStoryStep;
import model.map.*;
import model.map.objects.MapObject;
import model.map.objects.UnderworldEntrance;
import model.map.wars.WarHandler;
import model.races.Race;
import model.ruins.RuinsDungeon;
import model.states.*;
import model.tutorial.TutorialHandler;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import sound.SoundEffects;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.dev.SpritePreviewerView;
import view.sprites.AnimationManager;
import view.subviews.EmptySubView;
import view.subviews.PortraitSubView;
import view.subviews.SubView;
import view.GameView;
import view.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;

public class Model {
    public static final int REP_TO_WIN = 10;
    private static final String HALL_OF_FAME_PATH = "hall_of_fame.ff8";
    private FatefulEight frame;
    private GameData gameData;

    private World world = new World(WorldBuilder.buildWorld(WorldType.original),
                                        WorldBuilder.getWorldBounds(WorldBuilder.ORIGINAL),
                                WorldType.original);
    private World lastWorld = null;
    private CaveSystem caveSystem;
    private GameView gameView;
    private SubView subView;
    private ScreenHandler screenHandler;

    private boolean changes;
    private GameLog log;
    private GameState state;
    private SpellHandler spellHandler = new SpellHandler();
    private boolean gameExited;
    private boolean gameOver;
    private boolean gameStarted;
    private boolean inCombat;
    private boolean gameAbandoned;
    private String gameStartFileName = null;

    public Model(ScreenHandler screenHandler, FatefulEight frame) {
        this.screenHandler = screenHandler;
        this.frame = frame;
        initialize();
        log = new GameLog();
        subView = new EmptySubView();
        gameView = new IntroGameView();
        gameView.transitionedTo(this);
        state = new WaitForStartOfGameState(this);
    }

    public void initialize() {
        changes = true;
        spellHandler = new SpellHandler();
        gameExited = false;
        gameOver = false;
        gameStarted = false;
        inCombat = false;
        gameAbandoned = false;
    }

    public void prepareForStartGameFromSave(String fileName) {
        gameStarted = true;
        gameStartFileName = fileName;
    }

    public void startGameFromSave(String filename) {
        try {
            initialize();
            subView = new EmptySubView();
            gameData = readGameData(filename);
            caveSystem = new CaveSystem(world, gameData.caveSystemSeed);
            setWorldState(gameData.worldState);
            if (gameData.currentWorld != WorldType.original) {
                goBetweenWorlds(gameData.currentWorld, gameData.party.getPosition());
            }
            state = getCurrentHex().getDailyActionState(this);
            System.out.println("Loading from file, setting state to " + state);
            log.setContent(gameData.logContent);
            SoundEffects.gameLoaded();
        } catch (FileNotFoundException | CorruptSaveFileException ex) {
            ex.printStackTrace();
        }
        gameView = new MainGameView();
        gameView.transitionedTo(this);
        gameStartFileName = null;
        getParty().clearAnimations();
        GameStatistics.setModel(this);
    }

    public static GameData readGameData(String filename) throws CorruptSaveFileException, FileNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            return (GameData) ois.readObject();
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException | ClassNotFoundException e) {
            throw new CorruptSaveFileException();
        }
    }

    public void startGameNoLoad() {
        initialize();
        subView = new EmptySubView();
        log = new GameLog();
        gameData = new GameData();
        state = new ChooseStartingCharacterState(this);
        System.out.println("Set state to ChooseStartingCharacterState");
        caveSystem = new CaveSystem(world, gameData.caveSystemSeed);
        gameStarted = true;
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mainSong);
        if (FatefulEight.TEST_MODE) {
            //MainStoryTest.testSuit(this);
        }
        GameStatistics.setModel(this);
    }

    public void startGameWithState(GameState state) {
        initialize();
        subView = new EmptySubView();
        log = new GameLog();
        gameData = new GameData();
        this.state = state;
        caveSystem = new CaveSystem(world, gameData.caveSystemSeed);
        GameStatistics.setModel(this);
        System.out.println("Setting state with startGameWithState");
        gameStarted = true;
    }

    public void progressMainStoryForTesting(MainStoryStep part, MainStorySpawnLocation spawnData) {
        gameData.mainStory.progressStoryForTesting(this, part, spawnData);
    }

    public boolean gameStarted() {
        return gameStarted;
    }

    public void update(long timeSinceLast) {
        if (!gameView.pausesGame()) {
            log.update(timeSinceLast, this);
            AnimationManager.stepPausableAnimations(timeSinceLast, this);
        }
        if (this.changes || gameView.hasChanges()) {
            gameView.update(this);
            if (gameView.timeToTransition()) {
                switchView();
            }
            changes = false;
        }
    }

    public Party getParty() {
        return gameData.party;
    }

    public World getWorld() {
        return world;
    }

    public ScreenHandler getScreenHandler() {
        return screenHandler;
    }

    public void switchView() {
        GameView nextView = gameView.getNextView(this);
        gameView.transitionedFrom(this);
        nextView.transitionedTo(this);
        gameView = nextView;
    }

    public GameView getView() {
        return gameView;
    }

    public void madeChanges() {
        this.changes = true;
    }

    public int getDay() {
        return gameData.day;
    }

    public GameLog getLog() {
        return log;
    }

    public WorldHex getCurrentHex() {
        if (gameData.inUnderworld) {
            return caveSystem.getHex(getParty().getPosition());
        }
        return getWorld().getHex(getParty().getPosition());
    }

    public String getPartyHexInfo() {
        return getHexInfo(getParty().getPosition());
    }

    public String getHexInfo(Point position) {
        List<MapObject> mapObjects = getMapObjects(position);
        StringBuilder bldr = new StringBuilder();
        for (MapObject mobj : mapObjects) {
            bldr.append(", ");
            bldr.append(mobj.getDescription());
        }
        String mapObjectExtra = bldr.toString();
        return "(" + position.x + "," + position.y + ") " +
                getWorld().getHex(position).getDescription() + gameData.mainStory.getHexInfo(position) + mapObjectExtra;
    }

    public void handleKeyEvent(KeyEvent event) {
        getView().handleKeyEvent(event, this);
    }

    public List<DailyAction> getDailyActions() {
        if (gameData.mustStayInHex) {
            gameData.mustStayInHex = false;
            return List.of(new StayInHexAction(this));
        }
        List<DailyAction> result = getParty().getDailyAction(this);
        for (MapObject obj : gameData.mapObjects) {
            if (obj.getPosition().equals(getParty().getPosition())) {
                result.addAll(obj.getDailyActions(this));
            }
        }
        return result;
    }

    public boolean gameExited() {
        return this.gameExited;
    }

    public void setExitGame(boolean b) {
        this.gameExited = b;
        if (gameExited) {
            log.flushContent(this);
            ClientSoundManager.stopPlayingBackgroundSound();
        }
    }

    public SubView getSubView() {
        return subView;
    }

    public void setSubView(SubView subView) {
        this.subView = subView;
    }

    public void runGameScript() {
        while (!gameExited()) {
            GameState nextState = state.run(this);
            handleCastSpells();
            if (nextState != null) {
                System.out.println("GameScript: next state is " + nextState);
                state = nextState;
                if (endOfGameReached() && !gameData.freePlay) {
                    log.waitForAnimationToFinish();
                    transitionToDialog(new EndOfGameDialog(getView()));
                }
            }
            if (gameStartFileName != null) {
                startGameFromSave(gameStartFileName);
            }
            if (FatefulEight.inDebugMode()) {
                AnimationManager.printAnimations();
            }
        }
    }

    public void handleCastSpells() {
        while (!spellHandler.isEmpty()) {
            MyPair<Spell, GameCharacter> spellAndChar = spellHandler.getCastSpell();
            spellAndChar.first.castYourself(this, state, spellAndChar.second);
            state.print("If you want to cast another non-combat spell, do so from the spell or inventory view. " +
                    "Press enter to continue. ");
            state.waitForReturn();
        }
    }

    private boolean endOfGameReached() {
        return getParty().getReputation() >= REP_TO_WIN || gameData.day > 100;
    }

    public void setGameOver(boolean wipedOut) {
        gameOver = wipedOut;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public ItemDeck getItemDeck() {
        return gameData.itemDeck;
    }

    public List<GameCharacter> getAllCharacters() {
        return gameData.allCharacters;
    }

    public void transitionToDialog(GameView dialog) {
        this.gameView = dialog;
        dialog.transitionedTo(this);
    }

    public List<GameCharacter> getAvailableCharactersOfRace(Race race) {
        return MyLists.filter(getAllCharacters(), (GameCharacter gc) ->
                (gc.getRace().id() == race.id() || race.id() == Race.ALL.id()) &&
                        !getParty().getPartyMembers().contains(gc));
    }

    public List<GameCharacter> getAvailableCharactersByGender(boolean gender) {
        return MyLists.filter(getAllCharacters(), (GameCharacter gc) ->
            gc.getGender() == gender && !getParty().getPartyMembers().contains(gc));
    }

    public void incrementDay() {
        gameData.party.updateHeadquarters(this);
        gameData.day++;
        gameData.timeOfDay = TimeOfDay.MORNING;
        log.addAnimated("\n" + LogView.CYAN_COLOR + "- DAY " + gameData.day + " -" + LogView.DEFAULT_COLOR + "\n" );
        gameData.party.startOfDayUpdate(this);
        gameData.itemDeck.setStandardItemTier((int)GameState.calculateAverageLevel(this));
        gameData.settings.getMiscFlags().put("innworkdone", false);
        gameData.warHandler.updateWars(this);
        if (!gameData.lingeringRecruitables.isEmpty()) {
            if (gameData.lingeringRecruitables.size() > 5) {
                gameData.lingeringRecruitables.remove(0);
            }
            if (gameData.lingeringRecruitables.size() > 1 || MyRandom.flipCoin()) {
                gameData.lingeringRecruitables.remove(0);
            }
        }
    }

    public void setDay(int day) {
        gameData.day = day;
    }

    public void saveToFile(String filename) {
        try {
            gameData.logContent = log.getContents();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename + "_save.ff8"));
            oos.writeObject(gameData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setInCombat(boolean b) {
        inCombat = b;
    }

    public QuestDeck getQuestDeck() {
        return gameData.questDeck;
    }

    public SpellHandler getSpellHandler() {
        return spellHandler;
    }

    public void mustStayInHex(boolean b) {
        gameData.mustStayInHex = b;
    }

    public void setTimeOfDay(TimeOfDay time) {
        gameData.timeOfDay = time;
    }

    public TimeOfDay getTimeOfDay() {
        return gameData.timeOfDay;
    }

    public TutorialHandler getTutorial() {
        return gameData.tutorial;
    }

    public void setFreePlay(boolean b) {
        gameData.freePlay = b;
    }

    public void recordInHallOfFame() {
        screenHandler.clearAll();
        HallOfFameData hfData = loadHallOfFameData();
        hfData.append(getParty(), GameScore.calculate(this));

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HALL_OF_FAME_PATH));
            oos.writeObject(hfData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showHallOfFame();
        gameStarted = false;
        this.state = new WaitForStartOfGameState(this);
    }

    public HallOfFameData loadHallOfFameData() {
        if (new File(HALL_OF_FAME_PATH).exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HALL_OF_FAME_PATH));
                HallOfFameData hfData = (HallOfFameData) ois.readObject();
                return hfData;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Hall of Fame data corrupt or bad version!");
            }
        }
        return new HallOfFameData();
    }

    private void addUnderworldEntranceAtCurrentPosition() {
        gameData.mapObjects.removeIf((MapObject mo) -> mo instanceof UnderworldEntrance &&
                mo.getPosition().equals(getParty().getPosition()));
        gameData.mapObjects.add(new UnderworldEntrance(new Point(getParty().getPosition())));
    }

    public void enterCaveSystem(boolean markOnMap) {
        gameData.inUnderworld = true;
        gameData.party.setOnRoad(false);
        if (markOnMap) {
            addUnderworldEntranceAtCurrentPosition();
        }
        CaveSystem.visitPosition(this, getParty().getPosition());
    }

    public void exitCaveSystem(boolean markOnMap) {
        gameData.inUnderworld = false;
        if (markOnMap) {
            addUnderworldEntranceAtCurrentPosition();
        }
    }

    public CaveSystem getCaveSystem() {
        return caveSystem;
    }

    public boolean isInCaveSystem() {
        return gameData.inUnderworld;
    }

    public RuinsDungeon getDungeon(String ruinsName, boolean isRuins) {
        if (!hasVisitedDungeon(ruinsName)) {
            storeDungeon(ruinsName, new RuinsDungeon(this, isRuins));
        }
        return gameData.dungeons.get(ruinsName);
    }

    public boolean hasVisitedDungeon(String ruinsName) {
        return gameData.dungeons.containsKey(ruinsName);
    }

    public void storeDungeon(String dungeonName, RuinsDungeon dungeon) {
        gameData.dungeons.put(dungeonName, dungeon);
    }

    public void visitDungeon(String ruinsName, RuinsDungeon dungeon) {
        gameData.dungeons.put(ruinsName, dungeon);
    }

    public void showHallOfFame() {
        gameView = new HallOfFameView(this);
    }

    public void showCredits() {
        gameView = new CreditsView();
    }

    public void setGameStarted(boolean b) {
        gameStarted = b;
    }

    public boolean isInQuest() {
        return state instanceof QuestState;
    }

    public boolean isInDungeon() {
        return state instanceof ExploreRuinsState;
    }

    public SettingsManager getSettings() {
        return gameData.settings;
    }

    public MainStory getMainStory() {
        return gameData.mainStory;
    }

    public CharacterAppearance getLordPortrait(UrbanLocation location) {
        if (!gameData.lordPortraits.containsKey(location.getLordName())) {
            CharacterAppearance lordAppearance = PortraitSubView.makeRandomPortrait(Classes.NOB, Race.ALL, location.getLordGender());
            gameData.lordPortraits.put(location.getLordName(), lordAppearance);
        }
        return gameData.lordPortraits.get(location.getLordName());
    }

    public void cycleWorldState() {
        int currState = this.world.getCurrentState();
        if (currState == 0xF) {
            currState = 0;
        } else {
            currState++;
        }
        this.world.setCurrentState(currState);
        this.caveSystem.setCurrentState(currState);
    }

    public void setWorldState(int worldState) {
        gameData.worldState = worldState;
        this.world.setCurrentState(gameData.worldState);
        this.caveSystem.setCurrentState(gameData.worldState);
    }

    public void resetMainStory() {
        this.gameData.mainStory = new MainStory();
    }

    public void toggleFullScreen() {
        frame.toggleFullScreen();
    }

    public void showSpritePreviewer() {
        this.gameView = new SpritePreviewerView(gameView);
    }

    public List<MapObject> getMapObjects(Point position) {
        return MyLists.filter(gameData.mapObjects, (MapObject mo) -> mo.getPosition().equals(position));
    }

    public boolean isGameDataAvailable() {
        return gameData != null;
    }

    public WarHandler getWarHandler() {
        return gameData.warHandler;
    }

    public boolean gameAbandoned() {
        return gameAbandoned;
    }

    public void setGameAbandoned(boolean b) {
        this.gameAbandoned = b;
    }

    public boolean partyIsInOverworldPosition(Point position) {
        return gameData.currentWorld == WorldType.original &&
                !gameData.inUnderworld && position.equals(getParty().getPosition());
    }

    public void setStartingPosition(Point position) {
        gameData.party.setStartingPosition(position);
    }

    public List<GameCharacter> getLingeringRecruitables() {
        return gameData.lingeringRecruitables;
    }

    public void goBetweenWorlds(WorldType worldType, Point position) {
        gameData.currentWorld = worldType;
        if (worldType != WorldType.original) {
            lastWorld = world;
            world = gameData.mainStory.buildPastWorld();
        } else {
            world = lastWorld;
        }
        gameData.party.setPosition(position);
        if (!getCurrentHex().hasRoad()) {
            gameData.party.setOnRoad(false);
        }
    }

    public boolean isInOriginalWorld() {
        return gameData.currentWorld == WorldType.original;
    }

    public World getOriginalWorld() {
        if (isInOriginalWorld()) {
            return world;
        }
        return lastWorld;
    }

    public GameAchievements getAchievements() {
        return gameData.achievements;
    }
}
