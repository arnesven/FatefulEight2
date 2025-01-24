package model.items.spells;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.PotionRecipe;
import model.items.Prevalence;
import model.items.potions.Potion;
import model.items.potions.UnstablePotion;
import model.states.GameState;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.AlchemySubView;
import view.subviews.ArrowMenuSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlchemySpell extends ImmediateSpell {
    private static final Sprite SPRITE = new ItemSprite(10, 8, MyColors.BEIGE, MyColors.GREEN, MyColors.WHITE);
    private Potion selectedPotion;
    private int ingredientCost = 0;
    private boolean distill = false;

    public AlchemySpell() {
        super("Alchemy", 5, MyColors.GREEN, 6, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public Item copy() {
        return new AlchemySpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        model.getTutorial().alchemy(model);
        if (model.getParty().getInventory().getPotions().isEmpty() && model.getParty().getPotionRecipes().isEmpty()) {
            state.println(caster.getName() + " was preparing to cast Alchemy, but you do not have any potions or recipes.");
            return false;
        }

        SubView previousSubView = model.getSubView();
        AlchemySubView subView = new AlchemySubView();
        model.setSubView(subView);

        do {
            subView.unsetContents();
            state.println(caster.getName() + " is preparing to cast Alchemy.");
            if (!selectBrewOrDistill(model, state, subView)) {
                return false;
            }

            Set<String> setOfPotions = findSetOfPotions(model);
            if (setOfPotions.isEmpty()) {
                state.println(caster.getName() + " was preparing to cast Alchemy, but you do not have enough ingredients.");
                model.setSubView(previousSubView);
                return false;
            }

            setSelectedPotionAndCost(model, state, setOfPotions);
            if (this.selectedPotion == null) {
                model.setSubView(previousSubView);
                return false;
            }
            subView.setContents(selectedPotion, ingredientCost);
            state.print("Are you sure you want to ");
            if (distill) {
                state.print("use up " + selectedPotion.getName() + " to recover " + ingredientCost + " ingredients");
            } else {
                state.print("spend " + ingredientCost + " ingredients to brew " + selectedPotion.getName());
            }
            state.print("? (Y/N) ");
        } while (!state.yesNoInput());
        return true;
    }

    private void setSelectedPotionAndCost(Model model, GameState state, Set<String> setOfPotions) {
        state.println("What potion would you like to attempt to " + (distill ? "distill" : "make") + " with Alchemy?");
        List<String> options = new ArrayList<>(setOfPotions);
        options.add("Cancel");
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 36 - options.size()*2, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = options.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturnSilently();
        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (selected[0].contains(p.getName())) {
                this.selectedPotion = p;
                this.ingredientCost = standardCostForPotion(p);
                break;
            }
        }
        for (PotionRecipe recipe : model.getParty().getPotionRecipes()) {
            if (selected[0].contains(recipe.getBrewable().getName())) {
                this.selectedPotion = recipe.getBrewable();
                this.ingredientCost = recipeCost(recipe.getBrewable());
                break;
            }
        }
    }

    private Set<String> findSetOfPotions(Model model) {
        Set<String> setOfPotions = new HashSet<>();
        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (standardCostForPotion(p) <= model.getParty().getInventory().getIngredients() || distill) {
                setOfPotions.add(nameAndStandardBrewingCost(p));
            }
        }
        if (!distill) {
            for (PotionRecipe recipe : model.getParty().getPotionRecipes()) {
                Potion p = recipe.getBrewable();
                setOfPotions.remove(nameAndStandardBrewingCost(p));
                setOfPotions.add(p.getName() + " (" + recipeCost(recipe.getBrewable()) + ")");
            }
        }
        return setOfPotions;
    }

    private boolean selectBrewOrDistill(Model model, GameState state, AlchemySubView subView) {
        final boolean[] cancelled = {false};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), List.of("Brew", "Distill", "Cancel"),
                24, 26, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                distill = cursorPos == 1;
                if (cursorPos == 2) {
                    cancelled[0] = true;
                }
            }
        });
        state.waitForReturnSilently();
        model.setSubView(subView);
        subView.setDistill(distill);
        return !cancelled[0];
    }

    private String nameAndStandardBrewingCost(Potion p) {
        return p.getName() + " (" + standardCostForPotion(p) + ")";
    }

    private int standardCostForPotion(Potion p) {
        return Math.max(1, p.getCost() / 2);
    }

    private int recipeCost(Potion p) {
        return Math.max(1, p.getCost() / 3);
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        if (distill) {
            state.println(caster.getName() + " distilled " + selectedPotion.getName() + " and recovered " + ingredientCost + " ingredients.");
            model.getParty().getInventory().remove(selectedPotion);
            model.getParty().getInventory().addToIngredients(ingredientCost);
            if (MyRandom.rollD6() == 6 &&
                    !MyLists.any(model.getParty().getPotionRecipes(),
                            r -> r.getBrewable().getName().equals(selectedPotion.getName()))) {
                state.println(caster.getName() + " also learned the recipe of " + selectedPotion.getName() + "!");
                model.getParty().getInventory().add(new PotionRecipe(selectedPotion));
                model.getParty().partyMemberSay(model, caster, List.of("I've got it!", "Eureka!", "That's it!",
                        "Finally!", "I understand now.", "Of course, it makes sense now."));
            }
        } else {
            int cost = ingredientCost;
            state.println(caster.getName() + " used up " + cost + " ingredients to brew a " + selectedPotion.getName() + ".");
            model.getParty().getInventory().addToIngredients(-cost);
            model.getParty().getInventory().addItem(selectedPotion.copy());
            GameStatistics.incrementPotionsBrewed();
        }
        model.getParty().partyMemberSay(model, caster, List.of("Bubble bubble!", "Ahh, what an aroma.",
                "I'm cooking!", "It took a little time, but now it's done.", "Let's save this for later.",
                "Mmmm... magical.", "Potions, potions, potions..."));
        askToCastAgain(model, state, caster, this);
    }

    @Override
    protected void applyFailureEffect(Model model, GameState state, GameCharacter caster) {
        if (distill) {
            state.println(caster.getName() + " was unable to recover any ingredients from the "
                    + selectedPotion.getName() + ".");
            model.getParty().getInventory().remove(selectedPotion);
        } else {
            model.getParty().getInventory().addToIngredients(-ingredientCost);
            if (ingredientCost > 4 && MyRandom.rollD6() == 1) {
                state.println(caster.getName() + " used up " + ingredientCost + " ingredients... the results were unexpected!");
                UnstablePotion potion = new UnstablePotion();
                potion.addYourself(model.getParty().getInventory());
                state.println("You got an " + potion.getName() + ".");
            } else {
                state.println(caster.getName() + " used up " + ingredientCost + " ingredients but was unable to " +
                        "brew anything useful.");

            }
        }
        askToCastAgain(model, state, caster, this);
    }

    private static void askToCastAgain(Model model, GameState state, GameCharacter caster, AlchemySpell alchemySpell) {
        state.print("Do you want to continue using " + alchemySpell.getName() + "? (Y/N) ");
        if (state.yesNoInput()) {
            alchemySpell.castYourself(model, state, caster);
        }
    }

    @Override
    public String getDescription() {
        return "Lets the caster concoct potions from gathered ingredients.";
    }
}
