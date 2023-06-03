package model;

import model.actions.DailyAction;
import model.actions.StayInHexAction;
import model.characters.*;
import model.items.spells.Spell;
import model.log.GameLog;
import model.map.CaveSystem;
import model.map.World;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.races.Race;
import model.ruins.RuinsDungeon;
import model.states.*;
import model.tutorial.TutorialHandler;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import sound.SoundEffects;
import util.MyPair;
import view.sprites.AnimationManager;
import view.subviews.EmptySubView;
import view.subviews.SubView;
import view.GameView;
import view.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final String HALL_OF_FAME_PATH = "hall_of_fame.ff8";

    private GameData gameData;

    private World world = new World(WorldBuilder.buildWorld());
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

    public Model(ScreenHandler screenHandler) {
        this.screenHandler = screenHandler;
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
    }

    public void startGameFromSave(String filename) throws FileNotFoundException, CorruptSaveFileException {
        try {
            initialize();
            subView = new EmptySubView();
            gameData = readGameData(filename);
            caveSystem = new CaveSystem(world, gameData.caveSystemSeed);
            state = getCurrentHex().getDailyActionState(this);
            System.out.println("Loading from file, setting state to " + state);
            log.setContent(gameData.logContent);
            SoundEffects.gameLoaded();
        } catch (FileNotFoundException | CorruptSaveFileException ex) {
            throw ex;
        }
        gameStarted = true;
        playMainSong();
    }

    public static GameData readGameData(String filename) throws CorruptSaveFileException, FileNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            return (GameData) ois.readObject();
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException e) {
            throw new CorruptSaveFileException();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startGameNoLoad() {
        initialize();
        subView = new EmptySubView();
        log = new GameLog();
        gameData = new GameData();
        state = new ChooseStartingCharacterState(this);
        caveSystem = new CaveSystem(world, gameData.caveSystemSeed);
        gameStarted = true;
        playMainSong();
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
        return "(" + position.x + "," + position.y + ") " +
                getWorld().getHex(position).getDescription();
    }

    public void handleKeyEvent(KeyEvent event) {
        getView().handleKeyEvent(event, this);
    }

    public List<DailyAction> getDailyActions() {
        if (gameData.mustStayInHex) {
            gameData.mustStayInHex = false;
            return List.of(new StayInHexAction(this));
        }
        return getParty().getDailyAction(this);
    }

    public boolean gameExited() {
        return this.gameExited;
    }

    public void setExitGame(boolean b) {
        this.gameExited = b;
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
        }
    }

    private void handleCastSpells() {
        while (!spellHandler.isEmpty()) {
            MyPair<Spell, GameCharacter> spellAndChar = spellHandler.getCastSpell();
            spellAndChar.first.castYourself(this, state, spellAndChar.second);
        }
    }

    private boolean endOfGameReached() {
        return getParty().getReputation() >= 6 || gameData.day > 100;
    }

    public void playMainSong() {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mainSong);
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

    public List<? extends GameCharacter> getAllCharacters() {
        return gameData.allCharacters;
    }

    public void transitionToDialog(GameView dialog) {
        this.gameView = dialog;
        dialog.transitionedTo(this);
    }

    public List<GameCharacter> getAvailableCharactersOfRace(Race race) {
        List<GameCharacter> list = new ArrayList<>();
        for (GameCharacter gc : getAllCharacters()) {
            if (((gc.getRace().id() == race.id() || race.id() == Race.ALL.id()) &&
                    !getParty().getPartyMembers().contains(gc))) {
                list.add(gc);
            }
        }
        return list;
    }

    public List<GameCharacter> getAvailableCharactersByGender(boolean gender) {
        List<GameCharacter> list = new ArrayList<>();
        for (GameCharacter gc : getAllCharacters()) {
            if (gc.getGender() == gender && !getParty().getPartyMembers().contains(gc)) {
                list.add(gc);
            }
        }
        return list;
    }

    public void incrementDay() {
        gameData.day++;
        gameData.timeOfDay = TimeOfDay.MORNING;
        log.addAnimated("\n- DAY " + gameData.day + " -\n");
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
        HallOfFameData hfData = null;
        if (new File(HALL_OF_FAME_PATH).exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HALL_OF_FAME_PATH));
                hfData = (HallOfFameData) ois.readObject();
                ois.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            hfData = new HallOfFameData();
        }
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
                e.printStackTrace();
            }
        }
        return null;
    }

    public void enterCaveSystem() {
        gameData.inUnderworld = true;
    }

    public void exitCaveSystem() {
        gameData.inUnderworld = false;
    }

    public CaveSystem getCaveSystem() {
        return caveSystem;
    }

    public boolean isInCaveSystem() {
        return gameData.inUnderworld;
    }

    public RuinsDungeon getDungeon(String ruinsName) {
        if (!gameData.dungeons.containsKey(ruinsName)) {
            gameData.dungeons.put(ruinsName, new RuinsDungeon());
        }
        return gameData.dungeons.get(ruinsName);
    }

    public void showHallOfFame() {
        gameView = new HallOfFameView(this);
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
}
