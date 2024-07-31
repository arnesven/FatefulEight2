package model.headquarters;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;
import view.subviews.HeadquartersSubView;
import view.subviews.PortraitSubView;
import view.subviews.SetAssignmentsSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class GiveAssignmentsHeadquartersAction extends HeadquartersAction {
    private final HeadquartersSubView oldSubView;

    public GiveAssignmentsHeadquartersAction(Model model, Point menuLocation, HeadquartersSubView subView) {
        super(model, "Give assignments");
        this.oldSubView = subView;
    }

    @Override
    public GameState run(Model model) {
        if (model.getParty().getHeadquarters().getCharacters().isEmpty()) {
            println("There are no characters at headquarters to give assignments to.");
        }
        SetAssignmentsSubView assignmentSubView = new SetAssignmentsSubView(model.getParty().getHeadquarters());
        model.setSubView(assignmentSubView);
        do {
            waitForReturnSilently();
            if (assignmentSubView.getTopIndex() == 0) {
                break;
            }
            GameCharacter selected = assignmentSubView.getSelectedCharacter();
            PortraitSubView portraitSubView = new PortraitSubView(assignmentSubView, selected.getAppearance(), selected.getName());
            model.setSubView(portraitSubView);
            portraitSubView.portraitSay(model, this, "Hey " + model.getParty().getLeader().getFirstName() + ", what do you need?");
            println("What assignment do you want to give to " + selected.getName() + "?");
            int index = multipleOptionArrowMenu(model, 24, 24, List.of("R'n'R", "Town Work"));
            if (index == 0) {
                leaderSay("Don't leave headquarters OK? Just stay here and rest.");
                portraitSubView.portraitSay(model, this, "I can do that.");
                model.getParty().getHeadquarters().assignRnR(selected);
            } else {
                leaderSay("Do some work in town. Okay?");
                portraitSubView.portraitSay(model, this, "I'll do my best.");
                model.getParty().getHeadquarters().assignTownWork(selected);
            }
            model.getLog().waitForAnimationToFinish();
            model.setSubView(assignmentSubView);
        } while (true);

        model.setSubView(oldSubView);
        return null;
    }
}
