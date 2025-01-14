package model.races;

import model.characters.appearance.SlenderNeck;
import model.characters.appearance.TorsoNeck;
import model.classes.Skill;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class Halfling extends Race {
    public Halfling() {
        super("Halfling", MyColors.PEACH, -2, 2, 10, new Skill[]{
                Skill.Entertain, Skill.Security, Skill.Sneak
        }, "Halflings are small human-like creatures who are prevalent in plains, farmlands and in towns. " +
                "They are considerably less hardy than other races but are often quick on their feet. Halflings " +
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
    public Shoulders makeShoulders(boolean gender) {
        return new NarrowShoulders(gender);
    }

    @Override
    public TorsoNeck makeNeck(boolean gender) {
        return new SlenderNeck();
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        if (race.id() == id()) {
            return STRONG_POSITIVE_ATTITUDE;
        }
        return SLIGHT_DISLIKE_ATTITUDE;
    }

    @Override
    public String getShortDescription() {
        return "small human-like creatures, with curly hair. They're agile and witty.";
    }
}
