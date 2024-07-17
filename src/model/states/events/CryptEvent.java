package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.combat.loot.CombinedLoot;
import model.combat.loot.StandardCombatLoot;
import model.map.CaveHex;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

import java.util.List;

public class CryptEvent extends DailyEventState {

    private final SubView caveSubView;
    private DailyEventState innerEvent;

    public CryptEvent(Model model) {
        super(model);
        this.caveSubView = new CaveHex(0, 0).getImageSubView();
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to crypt", "I'm pretty sure there's a crypt not far from here. " +
                "It's an interesting landmark, but kind of spooky");
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters a crypt.");
        randomSayIfPersonality(PersonalityTrait.brave, List.of(model.getParty().getLeader()), "Perhaps we should take a look inside?");
        randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()), "Looks kind of spooky.");
        print("Do you wish to enter it? (Y/N) ");
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
            if (!innerEvent.haveFledCombat()) {
                findLoot(model);
            }
        } else if (dieRol < 8) {
            innerEvent = new SkeletonCryptEvent(model);
            innerEvent.doTheEvent(model);
            if (!innerEvent.haveFledCombat()) {
                findLoot(model);
            }
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
