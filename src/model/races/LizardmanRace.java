package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class LizardmanRace extends Race {
    protected LizardmanRace() {
        super("Lizardman", MyColors.DARK_GREEN, 0, 0, 20, new Skill[0],
                "Lizardmen are reptilian sentient creatures who live in remote " +
                        "areas like mountains and jungles. They are tall, scaly skinned humanoids with large tails. The color of their " +
                        "skin and horns and other protrusions can vary significantly between different individuals." +
                        "Although they are skilled traders and craftsmen they mostly " +
                        "keep to themselves and avoid common folk.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return null;
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return null;
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        return 0;
    }
}
