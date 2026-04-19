package model;

import util.MyPair;

import java.util.ArrayList;
import java.util.List;

public class NewGamePrepper {

    private static int newGamePrepIndex = 0;
    private static int loadingPrepIndex = 0;

    private static final List<NewGamePrepStep> preLoadSpriteSteps = List.of(
            new PreLoadSpriteMapStep("horses.png"),
            new PreLoadSpriteMapStep("combat.png"),
            new PreLoadSpriteMapStep("weapons.png"),
            new PreLoadSpriteMapStep("hair.png"),
            new PreLoadSpriteMapStep("spells.png"),
            new PreLoadSpriteMapStep("book.png"),
            new PreLoadSpriteMapStep("map.png"),
            new PreLoadSpriteMapStep("silhouette.png"),
            new PreLoadSpriteMapStep("mouth.png"),
            new PreLoadSpriteMapStep("face.png"),
            new PreLoadSpriteMapStep("enemies.png"),
            new PreLoadSpriteMapStep("avatars.png"),
            new PreLoadSpriteMapStep("hats.png"),
            new PreLoadSpriteMapStep("races.png"),
            new PreLoadSpriteMapStep("dungeon.png"),
            new PreLoadSpriteMapStep("castle.png"),
            new PreLoadSpriteMapStep("warlocksdungeon.png"),
            new PreLoadSpriteMapStep("dungeon.png"),
            new PreLoadSpriteMapStep("castle.png"),
            new PreLoadSpriteMapStep("callouts.png"),       // Not essential at start up
            new PreLoadSpriteMapStep("arabella.png"),       // Not essential at start up
            new PreLoadSpriteMapStep("riding.png"),        // Not essential at start up
            new PreLoadSpriteMapStep("battle_symbols.png"),       // Not essential at start up
            new PreLoadSpriteMapStep("battle.png"),       // Not essential at start up
            new PreLoadSpriteMapStep("gauge.png"));

    private static final List<NewGamePrepStep> newGameSteps = makeNewGameSteps();

    private static final List<NewGamePrepStep> loadSaveSteps = makeLoadSaveSteps();

    private static List<NewGamePrepStep> makeNewGameSteps() {
        List<NewGamePrepStep> result = new ArrayList<>(preLoadSpriteSteps);
        result.addAll(List.of(
                new NewGamePrepStep() {
                    @Override
                    public void doPrep(Model model) {
                        model.initialize();
                        model.initializeSubViewAndLog();
                    }

                    @Override
                    public String getDescription() {
                        return "Initializing";
                    }
                },
                new NewGamePrepStep() {
                    @Override
                    public void doPrep(Model model) {
                        model.createInitialWorld();
                    }

                    @Override
                    public String getDescription() {
                        return "Creating World";
                    }
                },
                new NewGamePrepStep() {
                    @Override
                    public void doPrep(Model model) {
                        model.setInitialState();
                    }

                    @Override
                    public String getDescription() {
                        return "Setting initial game state";
                    }
                },
                new NewGamePrepStep() {
                    @Override
                    public void doPrep(Model model) {
                        model.createCaveSystem();
                    }

                    @Override
                    public String getDescription() {
                        return "Creating Cave System";
                    }
                },
                new NewGamePrepStep() {
                    @Override
                    public void doPrep(Model model) {
                        model.startGameNoLoad();
                    }

                    @Override
                    public String getDescription() {
                        return "Starting Game";
                    }
                }
        ));
        return result;
    }

    private static List<NewGamePrepStep> makeLoadSaveSteps() {
        List<NewGamePrepStep> result = new ArrayList<>(preLoadSpriteSteps);
        return result;
    }

    public static MyPair<String, Double> prepForNewGame(Model model) {
        if (newGamePrepIndex == newGameSteps.size()) {
            return new MyPair<>(newGameSteps.getLast().getDescription(), 1.0);
        }
        int stepToDo = newGamePrepIndex++;
        newGameSteps.get( stepToDo).doPrep(model);
        return new MyPair<>(newGamePrepIndex >= newGameSteps.size() ? "Done!" : newGameSteps.get(newGamePrepIndex).getDescription(),
                stepToDo / (double)newGameSteps.size());
    }

    public static MyPair<String, Double> prepForLoad(Model model) {
        if (loadingPrepIndex == loadSaveSteps.size()) {
            return new MyPair<>(loadSaveSteps.getLast().getDescription(), 1.0);
        }
        int stepToDo = loadingPrepIndex++;
        loadSaveSteps.get( stepToDo).doPrep(model);
        return new MyPair<>(loadingPrepIndex >= loadSaveSteps.size() ? "Loading saved data" : loadSaveSteps.get(loadingPrepIndex).getDescription(),
                stepToDo / (double)loadSaveSteps.size());
    }

    public static void resetLoadPrep() {
        loadingPrepIndex = 0;
    }
}
