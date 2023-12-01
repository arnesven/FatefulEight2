package model.states;

import model.Model;
import model.characters.GameCharacter;
import util.MyRandom;
import view.*;
import view.help.TutorialStartDialog;
import view.party.CharacterCreationView;
import view.subviews.ArrowMenuSubView;

import java.util.List;

public class ChooseStartingCharacterState extends GameState {

    public ChooseStartingCharacterState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        print("How would you like to select your starting character? ");
        model.getLog().waitForAnimationToFinish();
        GameCharacter gc;
        while (true) {
            int choice = multipleOptionArrowMenu(model, 30, 16, List.of("Choose Preset",
                    "Random Preset",
                    "Generate",
                    "Create Custom"));

            if (choice == 3) {
                CharacterCreationView charCreation = new CharacterCreationView(model.getView());
                model.transitionToDialog(charCreation);
                print(" ");
                model.getLog().waitForAnimationToFinish();
                gc = charCreation.getFinishedCharacter();
                if (gc != null) {
                    break;
                }
            } else if (choice == 0) {
                StartingCharacterView selectChar = new SelectStaringCharacterView(model);
                model.transitionToDialog(selectChar);
                print(" ");
                model.getLog().waitForAnimationToFinish();
                gc = selectChar.getFinalCharacter();
                if (gc != null) {
                    break;
                }
            } else if (choice == 1) {
                gc = MyRandom.sample(model.getAllCharacters());
                gc.setRandomStartingClass();
                model.getAllCharacters().remove(gc);
                break;
            } else { // generate
                GenerateStartingCharacterView genChar = new GenerateStartingCharacterView(model);
                model.transitionToDialog(genChar);
                print(" ");
                model.getLog().waitForAnimationToFinish();
                gc = genChar.getFinalCharacter();
                if (gc != null) {
                    break;
                }
            }
        }
        println("");
        print("You have selected your starting character: ");
        model.getParty().add(gc);
        model.getParty().addToGold(gc.getCharClass().getStartingGold());
        println(gc.getFullName() + " the " + gc.getRace().getName() + " " + gc.getCharClass().getFullName() + ".");
        return model.getCurrentHex().getDailyActionState(model);
    }
}
