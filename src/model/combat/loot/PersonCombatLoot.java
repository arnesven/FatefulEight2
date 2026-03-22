package model.combat.loot;

import model.Model;
import model.items.Item;
import model.items.Prevalence;
import model.items.spells.Spell;
import model.items.weapons.Weapon;
import util.MyRandom;

import java.util.List;

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
        obols = (int)Math.round(getGoldFactor(model) * obols);
        if ((obols == 0 || getItems().isEmpty()) && MyRandom.flipCoin()) {
            List<Item> items = model.getItemDeck().draw(1, Prevalence.common, 0.0);
            getItems().add(items.getFirst());
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
