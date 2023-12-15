package view;

import model.Model;
import model.characters.GameCharacter;
import util.MyRandom;

public class RandomPresetStartingCharacterView extends StartingCharacterView {
    public RandomPresetStartingCharacterView(Model model) {
        super(model, new GameCharacter[]{pickRandomPresetCharacter(model)});
    }

    private static GameCharacter pickRandomPresetCharacter(Model model) {
        GameCharacter gc = MyRandom.sample(model.getAllCharacters());
        gc.setRandomStartingClass();
        return gc;
    }

    @Override
    protected String getViewTitle() {
        return "PRESET CHARACTER";
    }
}
