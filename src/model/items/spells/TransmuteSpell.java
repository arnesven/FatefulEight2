package model.items.spells;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.items.Item;
import model.states.GameState;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransmuteSpell extends ImmediateSpell {
    private static final Sprite SPRITE = new ItemSprite(0, 8, MyColors.BROWN, MyColors.WHITE, MyColors.DARK_GRAY);
    private static List<TransmuteAction> transmuteTable = makeTransmuteTable();
    private TransmuteAction selectedSource;
    private MyPair<String, Double> selectedTarget;
    private int amount;

    public TransmuteSpell() {
        super("Transmute", 18, MyColors.WHITE, 8, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TransmuteSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.println("What would you like to transmute?");
        List<String> options = new ArrayList<>();
        for (TransmuteAction ta : transmuteTable) {
            if (ta.getResource(model.getParty()) > 0) {
                for (MyPair<String, Double> target : ta.targets) {
                    int maximumResult = calculateMaximumYield(ta.getResource(model.getParty()), target);
                    if (maximumResult > 0) {
                        options.add(makeString(ta, target) + " (" + maximumResult + ")");
                    }
                }
            }
        }
        if (options.isEmpty()) {
            state.println("You do not have enough resources to transmute anything.");
            return false;
        }
        options.add("Cancel");
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 38 - options.size()*2, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = options.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturnSilently();
        if (selected[0].equals("Cancel")) {
            return false;
        }
        for (TransmuteAction ta : transmuteTable) {
            for (MyPair<String, Double> target : ta.targets) {
                if (selected[0].contains(makeString(ta, target))) {
                    this.selectedSource = ta;
                    this.selectedTarget = target;
                }
            }
        }
        if (selectedSource == null || this.selectedTarget == null) {
            throw new IllegalStateException("Could not find source or target for transmute spell!");
        }
        int max = calculateMaximumYield(selectedSource.getResource(model.getParty()), selectedTarget);
        int amount = 0;
        state.println("With your available " + selectedSource.source + " you can make at most " + max + " " + selectedTarget.first + ".");
        do {
            try {
                state.print("How many units of " + selectedTarget.first + " do you want? ");
                amount = Integer.parseInt(state.lineInput());
                if (amount > max || amount < 0) {
                    error(state, max);
                } else {
                    break;
                }
            } catch (NumberFormatException nfe) {
                error(state, max);
            }
        } while (true);
        if (amount > 0) {
            this.amount = amount;
            return true;
        }
        return false;
    }

    private String makeString(TransmuteAction ta, MyPair<String, Double> target) {
        return ta.source + " " + ((char) 0xB0) + " " + target.first;
    }

    private int calculateMaximumYield(int source, MyPair<String, Double> target) {
        return (int)(Math.floor(source * target.second));
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        int resourceSpent = (int)Math.ceil(amount / selectedTarget.second);
        state.println(caster.getName() + " transmuted " + resourceSpent + " " +
                selectedSource.source + " into " + amount + " " + selectedTarget.first + "!");
        selectedSource.decrease(model.getParty(), resourceSpent);
        addToResource(model.getParty(), selectedTarget.first, amount);
    }

    private void addToResource(Party party, String resource, int amount) {
        if (resource.equalsIgnoreCase("food")) {
            party.addToFood(amount);
        } else if (resource.equalsIgnoreCase("ingredients")) {
            party.getInventory().addToIngredients(amount);
        } else if (resource.equalsIgnoreCase("materials")) {
            party.getInventory().addToMaterials(amount);
        } else if (resource.equalsIgnoreCase("gold")) {
            party.addToGold(amount);
        } else {
            throw new IllegalStateException("Unrecognized resource type '" + resource + "'.");
        }
    }

    private void error(GameState state, int max) {
        state.println("Please enter an integer between 0 and " + max + ".");
    }

    @Override
    public String getDescription() {
        return "Lets the caster convert one type of resource into another.";
    }

    private abstract static class TransmuteAction implements Serializable {

        private final String source;
        private final List<MyPair<String, Double>> targets;

        public TransmuteAction(String source, List<MyPair<String, Double>> targets) {
            this.source = source;
            this.targets = targets;
        }

        public abstract int getResource(Party party);

        public abstract void decrease(Party party, int resourceSpent);
    }


    private static List<TransmuteAction> makeTransmuteTable() {
        List<TransmuteAction> list = new ArrayList<>();
        list.add(new TransmuteAction("Food", List.of(
                new MyPair<>("Ingredients", (1.0 / 8.5)),
                new MyPair<>("Materials", (1.0 / 11.5)),
                new MyPair<>("Gold", (1.0 / 7.5)))) {
            @Override
            public int getResource(Party party) {
                return party.getFood();
            }

            @Override
            public void decrease(Party party, int resourceSpent) {
                party.addToFood(-resourceSpent);
            }
        });

        list.add(new TransmuteAction("Ingredients", List.of(
                new MyPair<>("Food", 2.5),
                new MyPair<>("Materials", 1.5),
                new MyPair<>("Gold", 3.5))) {
            @Override
            public int getResource(Party party) {
                return party.getInventory().getIngredients();
            }

            @Override
            public void decrease(Party party, int resourceSpent) {
                party.getInventory().addToIngredients(-resourceSpent);
            }
        });

        list.add(new TransmuteAction("Materials", List.of(
                new MyPair<>("Food", 1.5),
                new MyPair<>("Ingredients", 1.5),
                new MyPair<>("Gold", 4.5)
        )) {
            @Override
            public int getResource(Party party) {
                return party.getInventory().getMaterials();
            }

            @Override
            public void decrease(Party party, int resourceSpent) {
                party.getInventory().addToMaterials(-resourceSpent);
            }
        });

        list.add(new TransmuteAction("Gold", List.of(
                new MyPair<>("Food", 2.0),
                new MyPair<>("Ingredients", 1.0/4.0),
                new MyPair<>("Materials", 1.0/4.0)
        )) {
            @Override
            public int getResource(Party party) {
                return party.getGold();
            }

            @Override
            public void decrease(Party party, int resourceSpent) {
                party.addToGold(-resourceSpent);
            }
        });
        return list;
    }
}
