package model.states.events;

import model.Model;
import model.combat.StandardCombatLoot;

import java.util.List;

public class DeadBodyEvent extends RiverEvent {
    public DeadBodyEvent(Model model) {
        super(model);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return false;
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters a dead body. This person has" +
                " been dead for a while. There are remains " +
                "of clothing and the stink of rotting flesh. In some places, " +
                "bones are visible. The party ransacks the body and the " +
                "pack beside it.");
        model.getParty().randomPartyMemberSay(model, List.of("This guy isn't going mind.", "Yuck...",
                "Gross!", "Wow, that's a powerful smell!", "Disgusting!#"));
        StringBuilder bldr = new StringBuilder("5 rations");
        int gold = 0;
        for (int i = 0; i < 3; ++i) {
            StandardCombatLoot loot = new StandardCombatLoot(model);
            if (!loot.getText().equals("")) {
                bldr.append(", " + loot.getText());
            }
            loot.giveYourself(model.getParty());
            gold += loot.getGold();
        }
        if (gold > 0) {
            bldr.append(" and " + gold + " gold");
        }
        model.getParty().addToFood(5);
        println("The party finds " + bldr + ".");
        model.getParty().randomPartyMemberSay(model, List.of("Hey, free stuff!",
                "I'm sure it will come in handy.", "Ashes to ashes...",
                "Rest in peace fellow adventurer."));
    }
}
