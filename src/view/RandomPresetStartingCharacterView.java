package view;

import model.Model;
import model.characters.GameCharacter;
import model.characters.preset.PresetCharacter;
import util.MyRandom;

public class RandomPresetStartingCharacterView extends StartingCharacterView {
    public RandomPresetStartingCharacterView(Model model) {
        super(model, new GameCharacter[]{pickRandomPresetCharacter(model)});
    }

    private static PresetCharacter pickRandomPresetCharacter(Model model) {
        PresetCharacter gc = MyRandom.sample(model.getAllCharacters());
        gc.setRandomStartingClass();
        return gc;
    }

    @Override
    protected String getViewTitle() {
        return "PRESET CHARACTER";
    }
}
