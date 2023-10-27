package model.states.events;

import model.Model;
import model.combat.CombinedLoot;
import model.combat.StandardCombatLoot;
import model.map.CaveHex;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

public class CryptEvent extends DailyEventState {

    private final SubView caveSubView;
    private DailyEventState innerEvent;

    public CryptEvent(Model model) {
        super(model);
        this.caveSubView = new CaveHex(0, 0).getImageSubView();
    }

    @Override
    protected void doEvent(Model model) {
        print("The party encounters a crypt. Do you wish to enter it? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        CollapsingTransition.transition(model, caveSubView);
        int dieRol = MyRandom.rollD10();
        if (dieRol < 4) {
            innerEvent = new NecromancerRitual(model);
            innerEvent.doTheEvent(model);
        } else if (dieRol < 6) {
            innerEvent = new GhostCryptEvent(model);
            innerEvent.doTheEvent(model);
            findLoot(model);
        } else if (dieRol < 8) {
            innerEvent = new SkeletonCryptEvent(model);
            innerEvent.doTheEvent(model);
            findLoot(model);
        } else {
            println("The crypt appears to be empty, apart from some dusty old coffins.");
            givePartyLoots(model, 1, 3);
        }
    }

    private void findLoot(Model model) {
        CollapsingTransition.transition(model, caveSubView);
        println("Now that the crypt has been cleared out of monsters, you take your time to " +
                "ransack the graves.");
        givePartyLoots(model, 4, 7);
    }

    private void givePartyLoots(Model model, int a, int b) {
        CombinedLoot combinedLoot = new CombinedLoot();
        for (int i = MyRandom.randInt(a, b); i > 0; --i) {
            StandardCombatLoot loot = new StandardCombatLoot(model);
            combinedLoot.add(loot);
        }
        if (!combinedLoot.isNothing()) {
            println("The party finds " + combinedLoot.getText() + ".");
            combinedLoot.giveYourself(model.getParty());
            leaderSay("Some people call this 'grave robbing'. I like to think of it as 'recycling'.");
        }
    }

    @Override
    public boolean haveFledCombat() {
        if (innerEvent != null) {
            return innerEvent.haveFledCombat();
        }
        return false;
    }
}
