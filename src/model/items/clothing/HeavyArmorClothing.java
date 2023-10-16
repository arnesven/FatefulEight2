package model.items.clothing;

import model.classes.Skill;
import util.MyPair;

import java.util.List;

public abstract class HeavyArmorClothing extends Clothing {
    public HeavyArmorClothing(String name, int cost, int ap) {
        super(name, cost, ap, true);
    }


    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Acrobatics, -1));
    }

    @Override
    public int getWeight() {
        return 5000;
    }
}
