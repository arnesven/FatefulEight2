package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.combat.CombatAdvantage;
import model.enemies.Enemy;
import model.enemies.GhostEnemy;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyRandom;
import view.combat.DungeonTheme;

import java.util.ArrayList;
import java.util.List;

public class GhostCryptEvent extends DailyEventState {
    public GhostCryptEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        boolean ambush = MyRandom.flipCoin();
        println("The party " + (ambush?"is ambushed by":"encounters") + " some ghosts!");
        randomSayIfPersonality(PersonalityTrait.cowardly, new ArrayList<>(), "G-g-g-ghosts? Too scary!");
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new GhostEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new GhostEnemy('A'));
        }
        runCombat(result, new DungeonTheme(), true, ambush ? CombatAdvantage.Enemies : CombatAdvantage.Neither);
    }
}
