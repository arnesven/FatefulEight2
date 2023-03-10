package model.states.events;

import model.Model;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.MountainCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class AltarEvent extends DailyEventState {
    public AltarEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party reaches the summit of the mountain. There is an altar here. " +
                "Someone has perform some sort of sacrificial ritual here. Not only is the " +
                "victim dead but there are " +
                "also some dead cultists scattered about the site.");
        model.getParty().randomPartyMemberSay(model, List.of("This does not look like it ended well."));
        int dieRoll = MyRandom.rollD10();
        List<Enemy> enemies = new ArrayList<>();
        if (dieRoll <= 3) {
            println("Imp: \"Eeeeh, he he he he... More of you come to play huh?\"");
            enemies.add(new ImpEnemy('A'));
        } else if (dieRoll <= 5) {
            println("Fiend: \"More weaklings to slaughter!\"");
            enemies.add(new FiendEnemy('A'));
        } else if (dieRoll <= 7) {
            println("Succubus: \"Come my dear, let me caress you...\"");
            enemies.add(new SuccubusEnemy('A'));
        } else {
            println("Daemon: \"Bwahahaha! Fire will consume you all.\"");
            enemies.add(new DaemonEnemy('A'));
        }
        runCombat(enemies);
    }
}
