package model.states.events;

import model.Model;
import model.classes.Skill;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.enemies.ViperEnemy;
import model.states.CombatEvent;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class VipersEvent extends DailyEventState {
    public VipersEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party stumbles into a nest of vipers.");
        if (model.getParty().size() == 1) {
            boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Acrobatics, 6);
            if (result) {
                println("But you nimbly spring out of the nest and are on your way.");
                model.getParty().randomPartyMemberSay(model, List.of("Phew!"));
                return;
            }
        }
        model.getParty().randomPartyMemberSay(model, List.of("What infernal hissing!"));
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(3, model.getParty().partyStrength() / (new ViperEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(new ViperEnemy('A'));
        }
        runCombat(enemies);
    }
}
