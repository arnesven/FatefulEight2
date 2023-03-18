package model.states;

import model.Model;
import model.characters.GameCharacter;
import util.MyRandom;
import view.ArrowMenuGameView;
import view.GameView;
import view.SelectStaringCharacterView;
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
            char[] choice = new char[1];
            String choices = "SRC";
            model.setSubView(new ArrowMenuSubView(model.getSubView(),
                    List.of("Choose character",
                            "Random character",
                            "Create custom character"),
                    26, 16, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    choice[0] = choices.charAt(cursorPos);
                    model.setSubView(getPrevious());
                }
            });
            waitForReturnSilently();
            char selection = choice[0];
            if (selection == 'C') {
                CharacterCreationView charCreation = new CharacterCreationView(model.getView());
                model.transitionToDialog(charCreation);
                print(" ");
                model.getLog().waitForAnimationToFinish();
                gc = charCreation.getFinishedCharacter();
                if (gc != null) {
                    break;
                }
            } else if (selection == 'S') {
                SelectStaringCharacterView selectChar = new SelectStaringCharacterView(model);
                model.transitionToDialog(selectChar);
                print(" ");
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
        println("");
        print("You have selected your starting character: ");
        model.getParty().add(gc);
        model.getParty().addToGold(gc.getCharClass().getStartingGold());
        println(gc.getFullName() + " the " + gc.getRace().getName() + " " + gc.getCharClass().getFullName() + ".");
        return model.getCurrentHex().getDailyActionState(model);
    }
}
