package model.combat.loot;

import model.Model;
import util.MyRandom;

import java.util.List;

public class DemonicLoot extends StandardCombatLoot {
    public DemonicLoot(Model model) {
        super(model, MyRandom.randInt(2, 8));
        getItems().add(MyRandom.sample(
                List.of(model.getItemDeck().getRandomJewelry(),
                        model.getItemDeck().getRandomSpell(),
                        model.getItemDeck().getRandomWand())));
    }
}
