package model.combat;

import model.Model;
import util.MyRandom;

public class PersonCombatLoot extends StandardCombatLoot {
    private final int materials;

    public PersonCombatLoot(Model model) {
        super(model);
        this.materials = MyRandom.randInt(2);
        this.setGold(getGold() + MyRandom.randInt(2));
    }

    @Override
    public int getMaterials() {
        return materials;
    }
}
