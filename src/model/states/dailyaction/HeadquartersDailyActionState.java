package model.states.dailyaction;

import model.Model;
import model.Party;
import model.headquarters.Headquarters;
import model.states.DailyActionState;
import model.states.GameState;
import model.states.HeadquatersEveningState;
import util.MyLists;
import view.subviews.CollapsingTransition;
import view.subviews.HeadquartersSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeadquartersDailyActionState extends GameState {
    private final AdvancedDailyActionState previousState;
    private Point menuLocation = new Point(24, 24);

    public HeadquartersDailyActionState(Model model, AdvancedDailyActionState state) {
        super(model);
        this.previousState = state;
    }

    @Override
    public GameState run(Model model) {
        SubView subView = new HeadquartersSubView();
        CollapsingTransition.transition(model, subView);
        do {
            List<String> options = new ArrayList<>(List.of("Transfer resource", "Leave HQ"));
            if (previousState.isEvening()) {
                options.add(0, "Rest");
            }
            int choice = multipleOptionArrowMenu(model, menuLocation.x, menuLocation.y, options);
            if (options.get(choice).contains("Transfer")) {
                transferResource(model);
            } else if (options.get(choice).contains("Rest")) {
                return new HeadquatersEveningState(model);
            } else {
                break;
            }
        } while (true);
        return previousState;
    }

    private void transferResource(Model model) {
        List<TransferMenu> transferMenus = new ArrayList<>(List.of(new GoldTransferMenu(),
                new FoodTransferMenu(), new IngredientsTransferMenu(), new MaterialsTransferMenu()));
        do {
            println("Which resource would you like to transfer to or from headquarters? ");
            List<String> options = MyLists.transform(transferMenus, TransferMenu::getName);
            options.add("Return");
            int choice = multipleOptionArrowMenu(model, menuLocation.x, menuLocation.y, options);
            if (options.get(choice).contains("Return")) {
                return;
            }
            transferMenus.get(choice).run(model, this);
        } while (true);
    }

    private abstract static class TransferMenu {
        public String name;

        public TransferMenu(String name) {
            this.name = name;
        }

        protected abstract int depositMax(Model model);
        protected abstract int withdrawalMax(Model model);
        protected abstract void addToParty(Party party, int amount);
        protected abstract void addToHq(Headquarters hq, int amount);

        public String getName() {
            return name;
        }

        public void run(Model model, GameState state) {
            boolean deposit;
            if (withdrawalMax(model) == 0) {
                deposit = true;
            } else if (depositMax(model) == 0) {
                deposit = false;
            } else {
                state.print("Would you like to deposit (Y) or withdraw (N) " + getName().toLowerCase() + "? ");
                deposit = state.yesNoInput();
            }
            do {
                state.print("How much " + getName().toLowerCase() + " would you like to " + (deposit ? "deposit" : "withdraw") + "? ");
                try {
                    int amount = Integer.parseInt(state.lineInput());
                    if (amount < 0) {
                        state.println("Please enter a positive integer.");
                    } else if (amount == 0) {
                        state.println("Cancelled " + (deposit ? "deposition" : "withdrawal") + " of " + getName().toLowerCase() + ".");
                        break;
                    } else if (deposit) {
                        if (amount > depositMax(model)) {
                            state.println("You do not have that much " + getName().toLowerCase() + ".");
                        } else {
                            addToParty(model.getParty(), -amount);
                            addToHq(model.getParty().getHeadquarters(), amount);
                            state.println("Deposited " + amount + " " + getName().toLowerCase() + " to stash in headquarters.");
                            break;
                        }
                    } else { // withdrawal
                        if (amount > withdrawalMax(model)) {
                            state.println("The headquarter stash does not have that much " + getName().toLowerCase() + ".");
                        } else {
                            addToHq(model.getParty().getHeadquarters(), -amount);
                            addToParty(model.getParty(), amount);
                            state.println("Withdrew " + amount + " " + getName().toLowerCase() + " from the stash in headquarters.");
                            break;
                        }
                    }
                } catch (NumberFormatException nfe) {
                    state.println("Please enter a positive integer.");
                }
            } while (true);
        }

    }

    private static class GoldTransferMenu extends TransferMenu {
        public GoldTransferMenu() {
            super("Gold");
        }

        @Override
        protected int depositMax(Model model) {
            return model.getParty().getGold();
        }

        @Override
        protected int withdrawalMax(Model model) {
            return model.getParty().getHeadquarters().getGold();
        }

        @Override
        protected void addToParty(Party party, int amount) {
            party.addToGold(amount);
        }

        @Override
        protected void addToHq(Headquarters hq, int amount) {
            hq.addToGold(amount);
        }
    }

    private static class FoodTransferMenu extends TransferMenu {
        public FoodTransferMenu() {
            super("Food");
        }

        @Override
        protected int depositMax(Model model) {
            return model.getParty().getFood();
        }

        @Override
        protected int withdrawalMax(Model model) {
            return model.getParty().getHeadquarters().getFood();
        }

        @Override
        protected void addToParty(Party party, int amount) {
            party.addToFood(amount);
        }

        @Override
        protected void addToHq(Headquarters hq, int amount) {
            hq.addToFood(amount);
        }
    }

    private static class IngredientsTransferMenu extends TransferMenu {
        public IngredientsTransferMenu() {
            super("Ingredients");
        }

        @Override
        protected int depositMax(Model model) {
            return model.getParty().getInventory().getIngredients();
        }

        @Override
        protected int withdrawalMax(Model model) {
            return model.getParty().getHeadquarters().getIngredients();
        }

        @Override
        protected void addToParty(Party party, int amount) {
            party.getInventory().addToIngredients(amount);
        }

        @Override
        protected void addToHq(Headquarters hq, int amount) {
            hq.addToIngredients(amount);
        }
    }

    private static class MaterialsTransferMenu extends TransferMenu {
        public MaterialsTransferMenu() {
            super("Materials");
        }

        @Override
        protected int depositMax(Model model) {
            return model.getParty().getInventory().getMaterials();
        }

        @Override
        protected int withdrawalMax(Model model) {
            return model.getParty().getHeadquarters().getMaterials();
        }

        @Override
        protected void addToParty(Party party, int amount) {
            party.getInventory().addToMaterials(amount);
        }

        @Override
        protected void addToHq(Headquarters hq, int amount) {
            hq.addToMaterials(amount);
        }
    }
}
