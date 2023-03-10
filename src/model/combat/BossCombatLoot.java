package model.combat;

import model.Model;
import util.MyRandom;

public class BossCombatLoot extends StandardCombatLoot {
    public BossCombatLoot(Model model) {
        super(model);
        getItems().add(model.getItemDeck().getRandomItem());
        setGold(MyRandom.rollD10() + getGold());
    }
}
