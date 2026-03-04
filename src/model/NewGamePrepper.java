package model;

import util.MyPair;

import java.util.List;

public class NewGamePrepper {

    private static int prepStepIndex = 0;

    private static final List<NewGamePrepStep> prepSteps = List.of(
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
            new PreLoadSpriteMapStep("gauge.png"),       // Not essential at start up
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
    );


    public static MyPair<String, Double> prepStep(Model model) {
        if (prepStepIndex == prepSteps.size()) {
            return new MyPair<>(prepSteps.getLast().getDescription(), 1.0);
        }
        int stepToDo = prepStepIndex++;
        prepSteps.get( stepToDo).doPrep(model);
        return new MyPair<>(prepStepIndex >= prepSteps.size() ? "Done!" : prepSteps.get(prepStepIndex).getDescription(),
                stepToDo / (double)prepSteps.size());
    }
}
