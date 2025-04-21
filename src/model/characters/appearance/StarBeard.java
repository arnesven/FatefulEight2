package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class StarBeard extends Beard {

    private final MikosBeard inner;

    public StarBeard(MyColors color) {
        super(6, 0x42);
        this.inner = new MikosBeard(color);
    }

    @Override
    public void apply(AdvancedAppearance advancedAppearance, Race race) {
        inner.apply(advancedAppearance, race);
    }
}
