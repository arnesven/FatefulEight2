package model.headquarters;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.GameState;
import util.MyLists;
import util.MyStrings;
import view.subviews.HeadquartersSubView;
import view.subviews.PortraitSubView;
import view.subviews.SetAssignmentsSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GiveAssignmentsHeadquartersAction extends HeadquartersAction {
    public static final int LEADERSHIP_RANKS_REQUIRED_FOR_SUBPARTY = 5;
    private final HeadquartersSubView oldSubView;

    public GiveAssignmentsHeadquartersAction(Model model, Point menuLocation, HeadquartersSubView subView) {
        super(model, "Give assignments");
        this.oldSubView = subView;
    }

    @Override
    public GameState run(Model model) {
        Headquarters hq = model.getParty().getHeadquarters();
        if (hq.getCharacters().isEmpty()) {
            println("There are no characters at headquarters to give assignments to.");
        }
        SetAssignmentsSubView assignmentSubView = new SetAssignmentsSubView(hq);
        model.setSubView(assignmentSubView);
        model.getTutorial().assignments(model);
        do {
            waitForReturnSilently();
            if (assignmentSubView.getTopIndex() == 2) {
                break;
            }
            if (assignmentSubView.getTopIndex() == 0) {
                setFoodLimit(model, hq);
                continue;
            } else if (assignmentSubView.getTopIndex() == 1) {
                if (canAssignSubParty(model)) {
                    setSubPartyTripLength(model, hq);
                }
                continue;
            }

            GameCharacter selected = assignmentSubView.getSelectedCharacter();
            if (hq.isAway(selected)) {
                println(selected.getName() + " is away adventuring and cannot be given a new assignment at the moment.");
                continue;
            }
            PortraitSubView portraitSubView = new PortraitSubView(assignmentSubView, selected.getAppearance(), selected.getName());
            model.setSubView(portraitSubView);
            portraitSubView.portraitSay(model, this, "Hey " + model.getParty().getLeader().getFirstName() + ", what do you need?");
            println("What assignment do you want to give to " + selected.getName() + "?");
            List<String> options = new ArrayList<>(List.of("R'n'R", "Town Work", "Shopping"));
            if (canAssignSubParty(model)) {
                options.add("Sub-party");
            }
            int index = multipleOptionArrowMenu(model, 24, 24, options);
            if (index == 0) {
                leaderSay("Don't leave headquarters OK? Just stay here and rest.");
                portraitSubView.portraitSay(model, this, "I can do that.");
                hq.assignRnR(selected);
            } else if (index == 1) {
                leaderSay("Do some work in town. Okay?");
                portraitSubView.portraitSay(model, this, "I'll do my best.");
                hq.assignTownWork(selected);
            } else if (index == 2) {
                leaderSay("Keep our headquarters stocked with food.");
                portraitSubView.portraitSay(model, this, "Sure, I'll go shopping if we're low on rations.");
                hq.assignShopping(selected);
            } else {
                leaderSay("I want you to go out adventuring in a sub-party.");
                portraitSubView.portraitSay(model, this, "How exciting! How long should we be away?");
                leaderSay("I think you should make a " +
                        Headquarters.getTripLengthString(hq.getTripLength()).toLowerCase() + " trip, " +
                        MyStrings.numberWord(Headquarters.getTripLengthInDays(hq.getTripLength())) + " days.");
                portraitSubView.portraitSay(model, this, "Understood. We'll keep doing trips like that.");
                hq.assignSubParty(selected);
            }
            model.getLog().waitForAnimationToFinish();
            model.setSubView(assignmentSubView);
        } while (true);

        model.setSubView(oldSubView);
        return null;
    }

    public static boolean canAssignSubParty(Model model) {
        return model.getParty().getLeader().getUnmodifiedRankForSkill(Skill.Leadership) >=
                LEADERSHIP_RANKS_REQUIRED_FOR_SUBPARTY;
    }

    private void setFoodLimit(Model model, Headquarters hq) {
        do {
            print("What would you like to set the lower food limit to? ");
            try {
                int amount = Integer.parseInt(lineInput());
                if (amount < 0) {
                    println("Please enter a non-negative value.");
                } else {
                    hq.setFoodLimit(amount);
                    break;
                }
            } catch (NumberFormatException nfe) {
                println("Please enter an integer value.");
            }
        } while (true);
    }


    private void setSubPartyTripLength(Model model, Headquarters hq) {
        println("What would you like the sub-party trip length to? ");
        List<String> options = MyLists.transform(List.of(1, 2, 3), Headquarters::getTripLengthString);
        int index = multipleOptionArrowMenu(model, 24, 24, options);
        hq.setTripLength(index + 1);
    }
}
