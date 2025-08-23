package model.states.events;

import model.Model;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class QuadGoonsEvent extends DailyEventState {
    public QuadGoonsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Another group of Quad Goons have caught up to you. They attack!");
        List<Character> groups = new ArrayList<>(List.of('A', 'B', 'C', 'D', 'E'));
        List<Enemy> enemies = new ArrayList<>();

        for (int numberOfEnemies = MyRandom.randInt(8, 12); enemies.size() < numberOfEnemies ; ) {
            QuadGoonEnemy e = makeEnemy(enemies, groups);
            if (e.getEnemyGroup() != '!') {
                enemies.add(e);
            }
        }
        runCombat(enemies);
    }

    private QuadGoonEnemy makeEnemy(List<Enemy> enemies, List<Character> groups) {
        int dieRoll = MyRandom.rollD10();
        switch (dieRoll) {
            case 1:
                return new QuadGoonImolator(findGroup(QuadGoonImolator.class, enemies, groups));
            case 2:
                return new QuadGoonNecromancer(findGroup(QuadGoonNecromancer.class, enemies, groups));
            case 3:
                return new QuadGoonMesmer(findGroup(QuadGoonMesmer.class, enemies, groups));
            case 4:
                return new QuadGoonJuggernaught(findGroup(QuadGoonJuggernaught.class, enemies, groups));
            case 5:
                return new QuadGoonBrawler(findGroup(QuadGoonBrawler.class, enemies, groups));
            case 6:
                return new QuadGoonHunter(findGroup(QuadGoonHunter.class, enemies, groups));
            case 7:
            case 8:
                return new QuadGoonFencer(findGroup(QuadGoonFencer.class, enemies, groups));
            case 9:
            default:
                return new QuadGoonMedic(findGroup(QuadGoonMedic.class, enemies, groups));
        }
    }

    private char findGroup(Class<? extends QuadGoonEnemy> quadGoonClass, List<Enemy> enemies,
                           List<Character> groups) {
        Enemy e = MyLists.find(enemies, en -> en.getClass().equals(quadGoonClass));
        if (e != null) {
            return e.getEnemyGroup();
        }
        if (!groups.isEmpty()) {
            return groups.removeFirst();
        }
        return '!';
    }
}
