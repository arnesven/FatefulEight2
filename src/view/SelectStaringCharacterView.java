package view;

import model.Model;
import model.characters.GameCharacter;

public class SelectStaringCharacterView extends StartingCharacterView {

    public SelectStaringCharacterView(Model model) {
        super(model, model.getAllCharacters().toArray(new GameCharacter[0]));
    }

    @Override
    protected String getViewTitle() {
        return "SELECT CHARACTER";
    }
}
