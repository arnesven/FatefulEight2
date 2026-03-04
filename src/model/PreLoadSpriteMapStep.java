package model;

import view.SpriteMapManager;

public class PreLoadSpriteMapStep extends NewGamePrepStep {
    private final String fileName;

    public PreLoadSpriteMapStep(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void doPrep(Model model) {
        SpriteMapManager.preLoadMap(fileName);
    }

    @Override
    public String getDescription() {
        return "Loading " + fileName;
    }
}
