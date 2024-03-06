package model.combat.loot;

import model.Party;

import java.util.ArrayList;
import java.util.List;

public class CombinedLoot extends CombatLoot {

    private List<CombatLoot> innerLoots = new ArrayList<>();

    @Override
    public String getText() {
        int gold = 0;
        int rations = 0;
        int materials = 0;
        int ingredients = 0;
        StringBuilder bldr = new StringBuilder();
        for (CombatLoot loot : innerLoots) {
            gold += loot.getGold();
            rations += loot.getRations();
            materials += loot.getMaterials();
            ingredients += loot.getIngredients();
            String text = loot.getText().replace("\n", ", ");
            if (!text.equals("")) {
                bldr.append(", ").append(text);
            }
        }

        if (gold > 0) {
            bldr.append(", ").append(gold).append(" gold");
        }
        if (rations > 0) {
            bldr.append(", ").append(rations).append(" food");
        }
        if (materials > 0) {
            bldr.append(", ").append(materials).append(" materials");
        }
        if (ingredients > 0) {
            bldr.append(", ").append(ingredients).append(" ingredients");
        }
        if (bldr.length() > 2) {
            return bldr.substring(2, bldr.length());
        }
        return bldr.toString();
    }

    @Override
    protected void specificGiveYourself(Party party) {
        for (CombatLoot loot : innerLoots) {
            loot.giveYourself(party);
        }
    }

    public void add(CombatLoot loot) {
        innerLoots.add(loot);
    }

    public boolean isNothing() {
        return getText().equals("");
    }
}
