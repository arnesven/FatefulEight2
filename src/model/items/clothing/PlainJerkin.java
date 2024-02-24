package model.items.clothing;

import model.classes.Skill;
import util.MyPair;

import java.util.List;

public class PlainJerkin extends FancyJerkin {

    @Override
    public String getName() {
        return "Plain Jerkin";
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Persuade, 1), new MyPair<>(Skill.SeekInfo, 1));
    }
}
