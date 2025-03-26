package model.combat.loot;

import model.Model;
import model.items.MysteriousMap;
import model.items.Prevalence;
import model.items.weapons.Musket;
import model.states.dailyaction.PirateShop;
import util.MyRandom;

public class PirateLoot extends PersonCombatLoot {
    public PirateLoot(Model model) {
        super(model);
        for (int i = 0; i < getItems().size(); ++i) {
            if (!(getItems().get(i) instanceof MysteriousMap)) {
                getItems().set(i, model.getItemDeck().draw(PirateShop.getPirateItems(), 1,
                        Prevalence.unspecified, 0.0).get(0));
            }
        }
        if (MyRandom.randInt(50) == 0) {
            getItems().add(new Musket());
        }
    }
}
