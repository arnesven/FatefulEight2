package model.states.fishing;

import model.Model;
import model.characters.GameCharacter;
import model.items.Inventory;
import model.items.UsableItem;
import util.MyRandom;

public abstract class Fish extends UsableItem {

    private static final double WEIGHT_PER_RATION = 450.0;
    private final int weight;
    private final int difficulty;

    public Fish(String name, int cost, int weightLower, int weightHigher, int difficulty) {
        super(name, cost);
        this.weight = MyRandom.randInt(weightLower, weightHigher);
        this.difficulty = difficulty;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addFish(this);
    }

    @Override
    public String getShoppingDetails() {
        return ", Rations: " + getRations();
    }

    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public String getSound() {
        return "wood-small";
    }

    @Override
    public String getUsageVerb() {
        return "Convert";
    }

    public int getRations() {
        return (int)(Math.max(1.0, weight / WEIGHT_PER_RATION));
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return true;
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        model.getParty().addToFood(getRations());
        return gc.getFirstName() + " converted the " + getName() + " into " + getRations() + " rations.";
    }
}
