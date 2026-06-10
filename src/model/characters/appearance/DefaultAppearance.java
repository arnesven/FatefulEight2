package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class DefaultAppearance extends CharacterAppearance {
    public DefaultAppearance(boolean gender) {
        super(Race.NORTHERN_HUMAN, gender, MyColors.ORANGE);
    }

    @Override
    protected int getMouth() {
        return 0;
    }

    @Override
    protected int getEye() {
        return 0;
    }

    @Override
    public int getNose() {
        return 0;
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }

    @Override
    public boolean hairOnTop() {
        return false;
    }

    @Override
    public CharacterAppearance copy() {
        return new DefaultAppearance(getGender());
    }
}
