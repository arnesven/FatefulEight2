package model.combat;

import model.Model;
import util.MyRandom;

public class FrogmanLoot extends StandardCombatLoot {

    private final int ingredients;

    public FrogmanLoot(Model model) {
        super(model);
        this.ingredients = MyRandom.randInt(1, 3);
    }

    @Override
    public int getIngredients() {
        return ingredients;
    }
}
