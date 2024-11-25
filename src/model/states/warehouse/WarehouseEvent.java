package model.states.warehouse;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.states.DailyEventState;
import model.states.events.GuideData;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.PortraitSubView;
import view.subviews.WarehouseSubView;

import java.util.List;

public class WarehouseEvent extends DailyEventState {
    private int otherBoxCount = 0;

    public WarehouseEvent(Model m) {
        super(m);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to warehouse",
                "I know a person who has some trouble find something in a warehouse");
    }

    @Override
    protected void doEvent(Model model) {
        CharacterAppearance workerAppearance = PortraitSubView.makeRandomPortrait(Classes.None);
        showExplicitPortrait(model, workerAppearance, "Worker");
        println("You pass by a warehouse. Outside is a young " + manOrWoman(workerAppearance.getGender()) +
                ", " + heOrShe(workerAppearance.getGender()) + " looks exhausted.");
        leaderSay("Are you all right?");
        portraitSay("Just a little flustered. Been at it for hours.");
        leaderSay("What's going on?");
        portraitSay("This warehouse is packed with boxes, but I'm trying to find a special one. " +
                "It's easy to spot, it's white, but there's so much stuff in there I can't get it out!");
        leaderSay("Why don't you just pull boxes out here until you can get the one you want?");
        portraitSay("No can do. If the boss finds out I'm leaving goods out in the open, " +
                "he'll give me a good thrashing. I just need to figure out how to move things around in there.");
        leaderSay("Maybe I can help?");
        portraitSay("Would you? I'll give you a reward if you can get the white box to the door. " +
                "I don't care if you move the others ones. " +
                "Oh, and don't even bother with the gray ones, they're super heavy.");
        print("Help the worker with the boxes in the warehouse? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("On second thought, we have places to be.");
            portraitSay("Okay, I better get back to work. I just need to catch my breath first.");
            return;
        }
        Warehouse warehouse = new Warehouse();
        WarehouseSubView subView = new WarehouseSubView(model, warehouse);
        CollapsingTransition.transition(model, subView);
        do {
            waitUntil(subView, WarehouseSubView::hasMovesToHandle);
            if (!subView.handleMove()) {
                printQuote("Worker", "Are you finished?");
                int choice = multipleOptionArrowMenu(model, 24,34, List.of("Start over", "Quit"));
                if (choice == 1) {
                    break;
                } else {
                    leaderSay(MyRandom.sample(List.of("I think I messed up. Can I do over?",
                            "Not yet. I'm just starting over.")));
                    subView = new WarehouseSubView(model, new Warehouse(warehouse.getTemplate()));
                    CollapsingTransition.transition(model, subView);
                }
            }
            if (subView.checkForRemove()) {
                printQuote("Worker", "What's this? This isn't the one I wanted.");
                leaderSay("Sorry...");
                model.getLog().waitForAnimationToFinish();
                subView = new WarehouseSubView(model, new Warehouse(warehouse.getTemplate()));
                CollapsingTransition.transition(model, subView);
            }
        } while (!subView.gameWon());

        if (!subView.gameWon()) {
            leaderSay("Bah, this is impossible. I give up.");
            printQuote("Worker", "Yeah... what a mess.");
            println("You leave the warehouse.");
            return;
        }
        printQuote("Worker", "There it is, the special box!");
        print("Press enter to continue.");
        waitForReturn();
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, workerAppearance, "Worker");
        portraitSay("I can't believe you actually got it out.");
        leaderSay("Piece of cake!");
        portraitSay("Here's your reward. I'm sorry I don't have more to give you.");
        println("You get 15 gold.");
        model.getParty().addToGold(15);
        leaderSay("Thanks. Try keeping the warehouse tidy from now on.");
        portraitSay("I'll try. Goodbye.");
        leaderSay("Bye for now.");
    }
}
