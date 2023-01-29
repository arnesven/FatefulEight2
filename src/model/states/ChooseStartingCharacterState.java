package model.states;

import model.Model;
import model.characters.GameCharacter;
import util.MyRandom;
import view.SelectStaringCharacterView;
import view.help.TutorialStartDialog;
import view.party.CharacterCreationView;

public class ChooseStartingCharacterState extends GameState {

    public ChooseStartingCharacterState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        print("How would you like to select your starting character? ");
        GameCharacter gc;
        while (true) {
            print("Select (S) character, Random (R) character, Custom (C) character: ");
            char selection = lineInput().toUpperCase().charAt(0);
            if (selection == 'C') {
                CharacterCreationView charCreation = new CharacterCreationView(model.getView());
                model.transitionToDialog(charCreation);
                print("Custom Character. ");
                model.getLog().waitForAnimationToFinish();
                gc = charCreation.getFinishedCharacter();
                if (gc != null) {
                    break;
                }
            } else if (selection == 'S') {
                SelectStaringCharacterView selectChar = new SelectStaringCharacterView(model);
                model.transitionToDialog(selectChar);
                print("Select Character. ");
                model.getLog().waitForAnimationToFinish();
                gc = selectChar.getFinalCharacter();
                if (gc != null) {
                    break;
                }
            } else if (selection == 'R') {
                gc = MyRandom.sample(model.getAllCharacters());
                gc.setRandomStartingClass();
                model.getAllCharacters().remove(gc);
                break;
            }
        }
        print("You have selected your starting character: ");
        model.getParty().add(gc);
        model.getParty().addToGold(gc.getCharClass().getStartingGold());
        println(gc.getFullName() + " the " + gc.getRace().getName() + " " + gc.getCharClass().getFullName() + ".");
        return model.getCurrentHex().getDailyActionState(model);
    }
}
