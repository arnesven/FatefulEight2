package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.enemies.SoldierEnemy;
import model.enemies.SoldierLeaderEnemy;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class CompanyEvent extends DailyEventState {
    public CompanyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.CAP, "Soldiers");
        println("The party encounters a large company of soldiers. They " +
                "seem friendly and so you decide to share your " +
                "evening with them. The company has an abundance of " +
                "beer, but lack food and hope that you can share some " +
                "of your rations. Later on, they get rowdy and want the " +
                "party members to join in their degrading games.");

        if (model.getParty().size() > 1) {
            GameCharacter gc1 = MyRandom.sample(model.getParty().getPartyMembers());
            GameCharacter gc2 = null;
            do {
                gc2 = MyRandom.sample(model.getParty().getPartyMembers());
            } while (gc1 == gc2);
            model.getParty().partyMemberSay(model, gc1, "We shouldn't have set up camp next to this rowdy bunch.");
            model.getParty().partyMemberSay(model, gc2, "Needless to say, they aren't good company.");
        }
        int soldiers = model.getParty().partyStrength() / 12 + 2;
        int loss = Math.min(soldiers + 1, model.getParty().getFood());
        print("Do you accept (Y) losing " + loss + " rations and indulging in their tiresome games or risk (N) angering the company of soldiers? ");
        if (yesNoInput()) {
            model.getParty().addToFood(-loss);
            MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-1));
            println("You grudgingly hand over your packs of food.");
            println("Each party member exhausts 1 SP.");
        } else {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < soldiers - 2; i++){
                enemies.add(new SoldierEnemy('A'));
            }
            enemies.add(new SoldierLeaderEnemy('B'));
            enemies.add(new SoldierLeaderEnemy('B'));
            runCombat(enemies);
            possiblyGetHorsesAfterCombat("soldiers", soldiers);
        }
    }
}
