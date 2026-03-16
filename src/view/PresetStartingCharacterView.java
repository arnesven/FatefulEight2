package view;

import model.Model;
import model.characters.GameCharacter;
import model.characters.preset.PresetCharacter;
import util.MyRandom;

public class PresetStartingCharacterView extends StartingCharacterView {
    public PresetStartingCharacterView(Model model) {
        super(model, model.getAllCharacters().toArray(new GameCharacter[0]), true);
    }

    @Override
    protected String getViewTitle() {
        return "PRESET CHARACTER";
    }
}
