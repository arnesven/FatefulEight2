package model.items.clothing;

import model.classes.Skill;
import util.MyPair;
import java.util.ArrayList;
import java.util.List;

public abstract class SuperHeavyArmorClothing extends HeavyArmorClothing {
    public SuperHeavyArmorClothing(String name, int cost, int ap) {
        super(name, cost, ap);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> result = new ArrayList<>(super.getSkillBonuses());
        result.add(new MyPair<>(Skill.Sneak, -1));
        return result;
    }
}
