package model.items.special;

import model.classes.Skill;
import model.items.weapons.FishingPole;
import util.MyPair;

import java.util.List;

public class GoodFishingPole extends FishingPole {

    @Override
    public String getName() {
        return "Good " + super.getName();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Survival, 1));
    }
}
