package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import model.items.StartingItem;
import util.MyPair;

import java.util.List;

public class PlainJerkin extends FancyJerkin implements StartingItem {

    @Override
    public String getName() {
        return "Plain Jerkin";
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Persuade, 1), new MyPair<>(Skill.SeekInfo, 1));
    }

    @Override
    public Item copy() {
        return new PlainJerkin();
    }
}
