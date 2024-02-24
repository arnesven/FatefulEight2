package model.items.clothing;

import model.classes.Skill;
import util.MyPair;

import java.util.List;

public class RustyRingMail extends RingMail {

    @Override
    public String getName() {
        return "Rusty Ring Mail";
    }

    @Override
    public int getCost() {
        return 20;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Acrobatics, -1),
                        new MyPair<>(Skill.Persuade, -1),
                        new MyPair<>(Skill.Sneak, -1));
    }
}
