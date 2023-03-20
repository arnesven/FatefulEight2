package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.potions.Potion;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlchemySpell extends ImmediateSpell {
    private static final Sprite SPRITE = new ItemSprite(10, 8, MyColors.BEIGE, MyColors.GREEN);
    private Potion selectedPotion;

    public AlchemySpell() {
        super("Alchemy", 5, MyColors.GREEN, 6, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new AlchemySpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        model.getTutorial().alchemy(model);
        if (model.getParty().getInventory().getPotions().isEmpty()) {
            state.println(caster.getName() + " was preparing to cast Alchemy, but you do not have any potions.");
            return false;
        }

        Set<String> setOfPotions = new HashSet<>();
        for (Potion p : model.getParty().getInventory().getPotions()) {
            int cost = p.getCost() / 2;
            if (cost <= model.getParty().getInventory().getIngredients()) {
                setOfPotions.add(p.getName() + " (" + p.getCost() / 2 + ")");
            }
        }
        if (setOfPotions.isEmpty()) {
            state.println(caster.getName() + " was preparing to cast Alchemy, but you do not have enough ingredients.");
            return false;
        }

        state.println("What potion would you like to attempt to make with Alchemy?");
        List<String> options = new ArrayList<>(setOfPotions);
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 28, 24 - options.size()*2, ArrowMenuSubView.NORTH_WEST) {
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
                break;
            }
        }
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        int cost = selectedPotion.getCost()/2;
        state.println(caster.getName() + " used up " + cost + " ingredients to brew a " + selectedPotion.getName() + ".");
        model.getParty().getInventory().addToIngredients(-cost);
        model.getParty().getInventory().addItem(selectedPotion.copy());
        model.getParty().partyMemberSay(model, caster, List.of("Bubble bubble!", "Ahh, what an aroma.",
                "I'm cooking!", "It took a little time, but now it's done.", "Let's save this for later."));
    }

    @Override
    public String getDescription() {
        return "Lets the caster concoct potions from gathered ingredients.";
    }
}
