package model.combat.loot;

import model.Model;
import util.MyRandom;

public class BossCombatLoot extends StandardCombatLoot {
    public BossCombatLoot(Model model) {
        super(model);
        getItems().add(model.getItemDeck().getRandomItem(0.95));
        setGold(MyRandom.rollD10() + getGold());
    }
}
