package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public abstract class GigantoidAppearance extends CharacterAppearance {

    public GigantoidAppearance(Race r) {
        super(r, false, MyColors.WHITE);
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
}
