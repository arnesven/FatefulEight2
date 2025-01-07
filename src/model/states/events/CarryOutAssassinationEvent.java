package model.states.events;

import model.Model;
import model.classes.Skill;
import model.items.potions.PoisonPotion;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.tasks.AssassinationDestinationTask;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class CarryOutAssassinationEvent extends DailyEventState {
    private final AssassinationDestinationTask task;

    public CarryOutAssassinationEvent(Model model, AssassinationDestinationTask task) {
        super(model);
        this.task = task;
    }

    @Override
    protected void doEvent(Model model) {
        String dwelling = task.getWrit().getDestinationShortDescription();
        String victimName = task.getWrit().getName();
        UrbanLocation townOrCastle = (UrbanLocation) model.getWorld().getHex(task.getPosition()).getLocation();
        setCurrentTerrainSubview(model);
        if (!task.getWrit().gotClue() || !task.getWrit().isLocationFound()) {
            if (promptForSearch(model, dwelling, victimName, townOrCastle)) {
                return;
            }
        }
        arriveAtDwelling(model, dwelling, victimName);
    }

    private boolean promptForSearch(Model model, String dwelling, String victimName, UrbanLocation townOrCastle) {
        print("Do you want to ask around for " + victimName +
                " (Y) or do you try to find " + hisOrHer(task.getWrit().getGender()) + " " +
                dwelling + " directly (N)? ");
        if (yesNoInput()) {
            boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 8);
            if (!result) {
                println("You ask around for " + victimName + ", but nobody seems to be able to give you any " +
                        "useful information.");
            } else if (!task.getWrit().gotClue()) {
                boolean alsoFindLocation = MyRandom.flipCoin();
                if (alsoFindLocation) {
                    print("While you are not able to learn the location of " + victimName + "'s " +
                            dwelling + ", you do find out ");
                } else {
                    print("You find the location of " + victimName + "'s home and ");
                    task.getWrit().setLocationFound(true);
                }
                task.getWrit().setGotClue(true);
                println("that " + heOrShe(task.getWrit().getGender()) + " is " +
                        task.getWrit().getOccupationDescription() + " here in " + townOrCastle.getPlaceName() +
                        " and that " + heOrShe(task.getWrit().getGender()) + " " +
                        task.getWrit().getAdditionalInformation() + ".");
            } else {
                println("You discover the location of " + victimName + "'s " + dwelling + "!");
                task.getWrit().setLocationFound(true);
            }
        } else if (!task.getWrit().isLocationFound()) {
            int difficulty = task.getWrit().gotClue() ? 6 : 8;
            boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, difficulty);
            if (!result) {
                if (task.getWrit().gotClue()) {
                    println("Even with everything you've learned about " + victimName +
                            ", you still cannot find out the location of " + hisOrHer(task.getWrit().getGender()) +
                            " home.");
                } else {
                    println("With nothing more than the short description in the writ, you are unable to locate the " +
                            dwelling + ".");
                }
            } else {
                task.getWrit().setLocationFound(true);
            }
        }
        return !task.getWrit().isLocationFound();
    }

    private void arriveAtDwelling(Model model, String dwelling, String victimName) {
        println("You arrive at the " + dwelling + ", where you expect to find " +
                "the target of the writ of execution, " + victimName + ".");

        MyPair<String, String> objectAtDoor = generateObjectAtDoor();
        println("You notice a " + objectAtDoor.first + " has been left on the porch near the door.");
        if (!hasPoison(model)) {
            leaderSay("Hmm... If only we had some poison...");
        }
        print("How do you proceed? ");
        List<String> options = new ArrayList<>(List.of("Enter home", "Lay in wait", "Sneak into home"));
        if (hasPoison(model)) {
            options.add("Poison " + objectAtDoor.second);
        }
        options.add("Leave");
        int choice = multipleOptionArrowMenu(model, 24, 22, options);
        println("");
        AssassinationEndingEvent subEvent = task.getWrit().makeEndingEvent(model);
        AssassinationEndingEvent.Ending ending;
        if (choice == 0) {
            ending = subEvent.enterHome(model, task);
        } else if (choice == 1) {
            ending = subEvent.layInWait(model, task);
        } else if (choice == 2) {
            ending = subEvent.sneakIntoHome(model, task);
        } else if (options.get(choice).contains("Poison")) {
            ending = subEvent.usePoison(model, task, objectAtDoor);
        } else {
            ending = subEvent.leave(model, task);
        }

        switch (ending) {
            case failure:
                task.setFailed(true);
                JournalEntry.printJournalUpdateMessage(model);
                break;
            case success:
                task.setCompleted(true);
                JournalEntry.printJournalUpdateMessage(model);
                break;
            case unresolvedEndOfDay:
                break;
        }
        model.getParty().unbenchAll();
    }

    private MyPair<String, String> generateObjectAtDoor() {
        String objectAtDoor = MyRandom.sample(List.of("a basket of apples",
                "a bottle of milk",
                "a basket of bread"));
        String[] parts = objectAtDoor.split(" ");
        return new MyPair<>(objectAtDoor, parts[parts.length-1]);
    }

    private boolean hasPoison(Model model) {
        return MyLists.find(model.getParty().getInventory().getAllItems(),
                it -> it instanceof PoisonPotion) != null;
    }
}
