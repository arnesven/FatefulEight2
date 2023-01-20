package model;

import model.actions.DailyAction;
import model.actions.StayInHexAction;
import model.characters.*;
import model.items.accessories.LeatherCap;
import model.items.clothing.FullPlateArmor;
import model.items.clothing.StuddedJerkin;
import model.items.potions.HealthPotion;
import model.items.spells.HarmonizeSpell;
import model.items.spells.LevitateSpell;
import model.items.spells.TurnUndeadSpell;
import model.items.weapons.BastardSword;
import model.log.GameLog;
import model.map.World;
import model.map.WorldHex;
import model.races.Race;
import model.states.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
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


    private static class GameData implements Serializable {
        public Party party = new Party();
        public ItemDeck itemDeck = new ItemDeck();
        public QuestDeck questDeck = new QuestDeck();
        public int day = 1;
        public CharacterCollection allCharacters = new CharacterCollection();
        public List<String> logContent;
        public boolean mustStayInHex = false;
        public TimeOfDay timeOfDay = TimeOfDay.MORNING;
    }

    private GameData gameData = new GameData();

    private World world = new World();
    private GameView gameView;
    private SubView subView;
    private ScreenHandler screenHandler;

    private boolean changes;
    private GameLog log;
    private GameState state;
    private SpellHandler spellHandler = new SpellHandler();
    private boolean gameExited = false;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private boolean inCombat = false;

    public Model(ScreenHandler screenHandler) {
        changes = true;
        this.screenHandler = screenHandler;
        log = new GameLog();
        subView = new EmptySubView();
        gameView = new IntroGameView();

        gameView.transitionedTo(this);
        state = new WaitForStartOfGameState(this);

        GameCharacter gc = getAllCharacters().get(1);
        gameData.party.add(gc);
//        gc.addToHP(-3);
        gameData.party.add(getAllCharacters().get(0));
//        gameData.party.getInventory().add(new HealthPotion());
//        gameData.party.getInventory().add(new TurnUndeadSpell());
        state = new QuestState(this, gameData.questDeck.getRandomQuest());
    }

    public void startGameFromSave(String filename) throws FileNotFoundException, CorruptSaveFileException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            gameData = (GameData) ois.readObject();
            state = getCurrentHex().getDailyActionState(this);
            log.setContent(gameData.logContent);
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException e) {
            throw new CorruptSaveFileException();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        gameStarted = true;
    }

    public void startGameNoLoad() {
        state = new ChooseStartingCharacterState(this);
        gameStarted = true;
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
        playMainSong();
        while (!gameExited()) {
            GameState nextState = state.run(this);
            if (nextState != null) {
                state = nextState;
            }
            System.out.println("Main loop!");
        }
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
}
