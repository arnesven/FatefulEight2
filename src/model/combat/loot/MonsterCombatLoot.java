package model.combat.loot;

import model.Model;
import util.MyRandom;

public class MonsterCombatLoot extends StandardCombatLoot {
    private final int rations;
    private final int ingredients;

    public MonsterCombatLoot(Model model) {
        super(model);
        this.rations = MyRandom.randInt(2);
        this.ingredients = MyRandom.randInt(2);
    }

    @Override
    public int getRations() {
        return rations;
    }

    @Override
    public int getIngredients() {
        return ingredients;
    }
}
