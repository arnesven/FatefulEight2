package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.states.DailyEventState;
import model.states.RecruitState;
import view.subviews.ArrowMenuSubView;
import view.subviews.ChangeClassSubView;
import view.subviews.ChangeClassTransitionSubView;

import java.awt.*;
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
            print("Do you want to change the class of any of the characters? ");
            model.getTutorial().classes(model);
            waitForReturn();

            int topAction = subView.getTopIndex();
            if (topAction == 1) { // Exit
                break;
            } else if (topAction == -1) { // Cursor in matrix
                subView.toggleDetails();
                GameCharacter gc = matrix.getSelectedElement();
                print("Are you sure you want to make " + gc.getName() + " a " + targetClasss.getFullName() + "? (Y/N) ");
                if (yesNoInput()) {
                    ChangeClassTransitionSubView.transition(model, subView, gc, subView.getWouldBe(gc));
                    gc.setClass(targetClasss);
                    if (gc.getLevel() == 0) {
                        gc.setLevel(Math.max(1, (int)RecruitState.calculateAverageLevel(model)));
                        println("From the experience of being in the party, " + gc.getName() + " advances to level " + gc.getLevel() + "!");
                    }
                    println(gc.getName() + " is now a " + targetClasss.getFullName() + "!");
                    candidates.remove(gc);
                    matrix.remove(gc);
                    if (candidates.isEmpty()) {
                        break;
                    }
                }
                subView.toggleDetails();
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
