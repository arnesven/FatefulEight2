package model.combat;

import model.Model;
import util.MyRandom;

public class PersonCombatLoot extends StandardCombatLoot {
    private final int materials;
    private int obols;

    public PersonCombatLoot(Model model) {
        super(model);
        this.materials = MyRandom.randInt(2);
        this.setGold(getGold() + MyRandom.randInt(2));
        this.obols = MyRandom.randInt(0, 20);
        if (obols < 15) {
            obols = 0;
        } else {
            obols += MyRandom.randInt(0, 10);
        }
    }

    @Override
    public int getMaterials() {
        return materials;
    }

    @Override
    public int getObols() {
        return obols;
    }
}
