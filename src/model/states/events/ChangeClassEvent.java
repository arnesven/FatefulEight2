package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.states.DailyEventState;
import view.subviews.ChangeClassSubView;

import java.util.List;

public class ChangeClassEvent extends DailyEventState {
    private final CharacterClass targetClasss;
    private final List<GameCharacter> candidates;
    private SteppingMatrix<GameCharacter> matrix;

    public ChangeClassEvent(Model model, CharacterClass targetClass) {
        super(model);
        this.targetClasss = targetClass;
        candidates = model.getParty().getMembersEligibleFor(targetClass);
        if (!candidates.isEmpty()) {
            matrix = new SteppingMatrix<>(3, 3);
            matrix.addElements(candidates);
        }
    }

    @Override
    protected void doEvent(Model model) {
        ChangeClassSubView subView = new ChangeClassSubView(matrix, targetClasss);
        model.setSubView(subView);
        do {
            print("Do you want the selected character to change class (C) or are you done (Q)? ");
            model.getTutorial().classes(model);
            char selectedAction = lineInput().toUpperCase().charAt(0);
            if (selectedAction == 'C') {
                subView.toggleDetails();
                GameCharacter gc = matrix.getSelectedElement();
                print("Are you sure you want to make " + gc.getName() + " a " + targetClasss.getFullName() + "? (Y/N) ");
                if (yesNoInput()) {
                    gc.setClass(targetClasss); // TODO: Potentially unequip heavy armor.
                    if (gc.getLevel() == 0) {
                        gc.setLevel(1);
                    }
                    println(gc.getName() + " is now a " + targetClasss.getFullName() + "!");
                    candidates.remove(gc);
                    matrix.remove(gc);
                    if (candidates.isEmpty()) {
                        break;
                    }
                }
                subView.toggleDetails();
            } else if (selectedAction == 'Q') {
                break;
            }
        } while (true);
    }

    public void areYouInterested(Model model) {
        if (candidates.size() > 0) {
            print("are you interested? (Y/N) ");
            if (yesNoInput()) {
                doEvent(model);
            }
        } else {
            println("but nobody in the party is suitable for that kind of training.");
        }
    }

    public int noOfCandidates() {
        return candidates.size();
    }
}
