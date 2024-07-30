package model.headquarters;

import model.Model;
import model.horses.Horse;
import model.states.GameState;
import util.MyLists;
import view.subviews.HeadquartersSubView;

import java.util.List;

public class TransferHorsesHeadquartersAction extends HeadquartersAction {
    private final HeadquartersSubView subView;

    public TransferHorsesHeadquartersAction(Model model, HeadquartersSubView subView) {
        super(model, "Transfer horses");
        this.subView = subView;
    }

    @Override
    public GameState run(Model model) {
        transferHorses(model, this, subView);
        return null;
    }

    private void transferHorses(Model model, GameState state, HeadquartersSubView subView) {
        boolean pickup = false;
        if (canPickUpHorses(model) && canDropOffHorses(model)) {
            print("Do you want to leave horses (Y) or pick some up (N)? ");
            pickup = !yesNoInput();
        } else if (canPickUpHorses(model)) {
            pickup = true;
        } else if (canDropOffHorses(model)) {
            pickup = false;
        } else {
            state.println("You cannot transfer any horses.");
            if (!model.getParty().canBuyMoreHorses()) {
                state.println("(Your party cannot handle more horses.)");
            }
            if (!model.getParty().getHorseHandler().isEmpty()) {
                state.println("(You have no horses to drop off.)");
            }
            if (headquartersHorsesFull(model)) {
                state.println("(Headquarters cannot stable any more horses.)");
            }
            if (model.getParty().getHeadquarters().getHorses().isEmpty()) {
                state.println("(Headquarters does not have any horses).");
            }
            return;
        }

        state.println("What horse do you want to " + (pickup ? "pick up" : "leave") + "? ");
        List<String> options = pickup ? MyLists.transform(model.getParty().getHeadquarters().getHorses(), Horse::getName)
                : MyLists.transform(model.getParty().getHorseHandler(), Horse::getName);
        int choice = multipleOptionArrowMenu(model, 24, 6, options);

        if (pickup) {
            Horse selected = model.getParty().getHeadquarters().getHorses().get(choice);
            model.getParty().getHeadquarters().getHorses().remove(selected);
            model.getParty().getHorseHandler().addHorse(selected);
            state.println("You picked up a " + selected.getName() + "!");
        } else {
            Horse selected = model.getParty().getHorseHandler().get(choice);
            model.getParty().getHorseHandler().removeHorse(selected);
            model.getParty().getHeadquarters().getHorses().add(selected);
            state.println("You stabled " + selected.getName() + " at headquarters. Bye " + selected.getName() + "!");
        }
    }

    private boolean canDropOffHorses(Model model) {
        return !headquartersHorsesFull(model) && model.getParty().getHorseHandler().size() > 0;
    }

    private boolean canPickUpHorses(Model model) {
        return model.getParty().canBuyMoreHorses() && model.getParty().getHeadquarters().getHorses().size() > 0;
    }

    private boolean headquartersHorsesFull(Model model) {
        return model.getParty().getHeadquarters().getHorses().size() == model.getParty().getHeadquarters().getMaxHorses();
    }
}
