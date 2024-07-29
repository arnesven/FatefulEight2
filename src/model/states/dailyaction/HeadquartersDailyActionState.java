package model.states.dailyaction;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.headquarters.Headquarters;
import model.items.Item;
import model.states.GameState;
import model.states.HeadquartersEveningState;
import model.states.ShopState;
import model.states.TransferItemState;
import util.MyLists;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.HeadquartersSubView;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeadquartersDailyActionState extends GameState {
    private final AdvancedDailyActionState previousState;
    private static final Point MENU_LOCATION = new Point(24, 19);

    public HeadquartersDailyActionState(Model model, AdvancedDailyActionState state) {
        super(model);
        this.previousState = state;
    }

    @Override
    public GameState run(Model model) {
        HeadquartersSubView subView = new HeadquartersSubView(model);
        CollapsingTransition.transition(model, subView);
        print("You step into your headquarters. ");
        if (headquartersHasCharacters(model)) {
            println(MyRandom.sample(model.getParty().getHeadquarters().getCharacters()).getFirstName() + " welcomes you back.");
        } else {
            println("It's empty.");
        }
        do {
            List<String> options = new ArrayList<>(List.of("Transfer resource", "Transfer items", "Leave HQ"));
            if (previousState.isEvening()) {
                options.add("Rest");
            }
            if (model.getParty().size() > 1 || headquartersHasCharacters(model)) {
                options.add(0,"Transfer character");
            }
            int choice = multipleOptionArrowMenu(model, MENU_LOCATION.x, MENU_LOCATION.y, options);
            if (options.get(choice).contains("resource")) {
                transferResource(model);
            } else if (options.get(choice).contains("character")) {
                transferCharacter(model, this, subView);
            } else if (options.get(choice).contains("items")) {
                transferItems(model, this, subView);
            } else if (options.get(choice).contains("Rest")) {
                return new HeadquartersEveningState(model);
            } else {
                break;
            }
        } while (true);
        return previousState;
    }

    private void transferItems(Model model, GameState state, SubView previous) {
        List<Item> stashItems = model.getParty().getHeadquarters().getItems();
        TransferItemState shop = new TransferItemState(model, "HEADQUARTERS", stashItems);
        if (stashItems.isEmpty()) {
            shop.setSellingMode(model);
        }
        shop.run(model);
        CollapsingTransition.transition(model, previous);
    }

    private boolean headquartersHasCharacters(Model model) {
        return !model.getParty().getHeadquarters().getCharacters().isEmpty();
    }

    private boolean headquartersFull(Model model) {
        return model.getParty().getHeadquarters().getMaxCharacters() ==
                model.getParty().getHeadquarters().getCharacters().size();
    }

    private void transferCharacter(Model model, GameState state, HeadquartersSubView subView) {
        boolean pickup;
        if (canDoPickup(model) && canDoDropOff(model)) {
            state.print("Would you like to leave (Y) or pick up (N) a character? ");
            pickup = !state.yesNoInput();
        } else if (canDoPickup(model)) {
            pickup = true;
        } else if (canDoDropOff(model)) {
            pickup = false;
        } else {
            state.println("You can neither pick up or leave characters at headquarters.");
            if (headquartersFull(model)) {
                state.println("(Headquarters can not hold more than " + model.getParty().getHeadquarters().getMaxCharacters() + " characters.)");
            }
            if (!headquartersHasCharacters(model)) {
                state.println("(No characters at headquarters.)");
            }
            if (model.getParty().isFull()) {
                state.println("(Your party is full.)");
            }
            if (model.getParty().size() == 1) {
                state.println("(You cannot leave your last party member.)");
            }
            return;
        }

        if (pickup) {
            print("Which character do you want to pick up from headquarters? ");
            subView.selectCharacterEnabled(true);
            state.waitForReturnSilently();
            GameCharacter selected = subView.getSelectedCharacter();
            subView.selectCharacterEnabled(false);
            state.leaderSay("Hello there " + selected.getFirstName() + ".");
            PortraitSubView portraitSubView = new PortraitSubView(subView, selected.getAppearance(), selected.getName());
            model.setSubView(portraitSubView);
            portraitSubView.portraitSay(model, this,
                    "Oh hello, " + model.getParty().getLeader().getFirstName() + ". How can I help?");
            print("Are you sure you want to pick up " + selected.getFirstName() + " from headquarters? (Y/N) ");
            if (!yesNoInput()) {
                leaderSay("Never mind.");
                model.getLog().waitForAnimationToFinish();
                model.setSubView(subView);
                return;
            }
            state.leaderSay("I want you to come with me.");
            portraitSubView.portraitSay(model, state, MyRandom.sample(
                    List.of("Great", "I'm ready", "That's good. I was getting bored.", "Finally!", "Really? Yay!",
                            "Fair enough.", "Okay with me.", "Right-oh.", "Off we go then.")));
            state.println(selected.getName() + " came back to the party.");
            model.getLog().waitForAnimationToFinish();
            model.setSubView(subView);
            model.getParty().getHeadquarters().getCharacters().remove(selected);
            model.getParty().add(selected, false);
            subView.updateCharacters(model);
        } else {
            do {
                print("Which party member do you want to leave at headquarters? ");
                GameCharacter selected = model.getParty().partyMemberInput(model, state,
                        model.getParty().getRandomPartyMember(model.getParty().getLeader()));
                if (selected == model.getParty().getLeader()) {
                    state.println("You cannot leave your leader.");
                } else {
                    if (model.getParty().getTamedDragons().get(selected) != null) {
                        print("Warning: leaving " + selected.getName() +
                                " at headquarters will release " + selected.getFirstName() + "'s tamed dragon. Is that OK? (Y/N) ");
                        if (!yesNoInput()) {
                            break;
                        }
                    } else {
                        print("Are you sure you want to leave " + selected.getFirstName() + " at headquarters? (Y/N) ");
                        if (!yesNoInput()) {
                            break;
                        }
                    }
                    state.leaderSay("I want you to stay at headquarters for a while " + selected.getFirstName() + ".");
                    model.getParty().partyMemberSay(model, selected,
                            List.of("Sure.", "Fine.", "Okay.", "No problem.", "That's OK with me.",
                            "Alright.", "Oki-doki.", "Okay, bye.", "I understand.", "My pleasure."));
                    state.println(selected.getName() + " has left the party and will remain at headquarters until picked up.");
                    model.getLog().waitForAnimationToFinish();
                    model.getParty().remove(selected, false, false, 0);
                    model.getParty().getHeadquarters().getCharacters().add(selected);
                    subView.updateCharacters(model);
                    break;
                }
            } while (true);
        }
    }

    private boolean canDoDropOff(Model model) {
        return model.getParty().size() > 1 && !headquartersFull(model);
    }

    private boolean canDoPickup(Model model) {
        return !model.getParty().isFull() && headquartersHasCharacters(model);
    }

    private void transferResource(Model model) {
        List<TransferMenu> transferMenus = new ArrayList<>(List.of(new GoldTransferMenu(),
                new FoodTransferMenu(), new IngredientsTransferMenu(), new MaterialsTransferMenu()));
        do {
            println("Which resource would you like to transfer to or from headquarters? ");
            List<String> options = MyLists.transform(transferMenus, TransferMenu::getName);
            options.add("Return");
            int choice = multipleOptionArrowMenu(model, MENU_LOCATION.x, MENU_LOCATION.y, options);
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
