package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class Halfling extends Race {
    public Halfling() {
        super("Halfling", MyColors.PEACH, -2, 2, new Skill[]{
                Skill.Entertain, Skill.Security, Skill.Sneak
        }, "Halflings are small human-like creatures who are prevalent in plains, farmlands and in towns. " +
                "They are considerably less hardy than other races but are often quick on their feed. Halflings " +
                "often have curly hair, both on their heads and on their feet. Halflings take up all kinds of " +
                "professions, but are often found to be Foresters, Artisans, Marksmen, Bards or Spies.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x74, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x84, hairColor);
    }

    @Override
    public boolean isShort() {
        return true;
    }

    @Override
    public Shoulders getShoulders() {
        return Shoulders.NARROW;
    }

    @Override
    public boolean isThickNeck() {
        return false;
    }
}
